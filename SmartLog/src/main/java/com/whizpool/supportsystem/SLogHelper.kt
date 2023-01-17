package com.whizpool.supportsystem

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.whizpool.supportsystem.enums.SLogLevel
import com.whizpool.supportsystem.utils.*
import com.whizpool.supportsystem.utils.SLogFileUtils.bufferedWriter
import com.whizpool.supportsystem.utils.SLogFileUtils.getDirForLogs
import com.whizpool.supportsystem.utils.SLogFileUtils.getDirForZip
import com.whizpool.supportsystem.utils.SLogFileUtils.getPathUri
import com.whizpool.supportsystem.utils.SLogFileUtils.makeChildDirectory
import com.whizpool.supportsystem.utils.SLogFileUtils.readTextFromFiles
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.joda.time.DateTime
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.math.min


object SLogHelper : LifecycleEventObserver {
    var logFileName: String? = null
    private var logFile: File? = null
    private var logTime: DateTime? = null
    var TAG = "LOG TAG"
    private const val MAX_LOG_LENGTH = 4000
    var mail: String? = null
    var subject: String? = null
    var isProtected: Boolean = false
    var passowrd: String = "123456"
    var textColor: Int? = null
    var typeface: Typeface? = null
    var buttonTextColor: Int? = null
    var titleName: String? = null
    var mainDialogBackgroundColor: Int? = null
    var buttonIcon: Pair<Drawable, Boolean>? = null
    var editTextBackground: Drawable? = null
    var sendButtonBackgroundColor: Int? = null
    var skipButtonBackgroundColor: Int? = null
    var dialogHandleColor: Int? = null
    var separatorColor: Int? = null
    var textSize: Float? = null
    var buttonTextSize: Float? = null
    var hideReportDialog: Boolean = false

    var additionalFiles: List<Uri>? = null

    val ioScope = CoroutineScope(Dispatchers.IO)

    private var defaultLogLevel: SLogLevel = SLogLevel.DEBUG

    private lateinit var versionName: String
    private var versionCode: Long = 0L

    private val singleThreadExecutor = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private lateinit var rootDirForLogs: File

    fun initialize(context: Context) {
        rootDirForLogs = context.getDirForLogs()
        versionName = context.getVersionName()
        versionCode = context.buildNumber()

        deleteLogs(true)
//        deleteOldLogs(true)
    }

    fun deleteLogs(forcefullyDelete: Boolean = false) {
        ioScope.launch {
            SLogFileUtils.deleteFiles(forcefullyDelete)
        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////


    fun log(
        tag: String = TAG,
        text: String = "",
        logLevel: SLogLevel = defaultLogLevel,
        shouldSave: Boolean = true,
        exception: Exception? = null,
    ) {

        val tagToLog = tag.ifBlank { "Null Tag" }

//        val text = text.ifEmpty { "Null Message" }

        exception?.let {
            Log.e(tag, text, it)
        } ?: kotlin.run {
            finalLog(tagToLog, text, logLevel)
        }


        // Getting current time for adding in log
        logTime = DateTime.now()
        // Creating a directory and a log file inside it for adding logs of today.
        val logFileName = "${logTime?.getFormattedDate(LOG_FILE_DATE_FORMAT)}.log"
        logFile = File(rootDirForLogs, logFileName)


        if (logFile?.exists() == false) {
            logFile?.createNewFile()

            val deviceInfo = StringBuilder().apply {
                append("App Version: $versionName ($versionCode)")
                append("\n")
                append("OS Version: ${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})")
                append("\n")
                append("Device Manufacturer: ${Build.MANUFACTURER}")
                append("\n")
                append("Device Model: ${Build.MODEL}")
                append("\n\n")
            }

            writeLogInFile(deviceInfo.toString())
        }

        if (shouldSave)
            writeLogInFile("$tagToLog: $text", exception)
    }


    private val mutex = Mutex()

    @Suppress("BlockingMethodInNonBlockingContext")
    private fun writeLogInFile(text: String, exception: Exception? = null) {
        // THROW EXCEPTION IF rootDirForLogs IS NOT INITIALIZED

        ioScope.launch {
            mutex.withLock {
                val stackTraceBuilder = StringBuilder()
                exception?.run {

                    stackTraceBuilder
                        .append(javaClass.name)
                        .append(':')
                        .append(" ")
                        .append(message)
                        .append('\n')

                    stackTrace.forEachIndexed { index, item ->

                        stackTraceBuilder
                            .append(if (index == 0) '\t' else "\t\t")
                            .append(item.toString())
                            .append(if (index != stackTrace.lastIndex) '\n' else "")
                    }
                }


                val timestamp = logTime?.getFormattedDate(LOG_TIMESTAMP_FORMAT)

                val logToWrite =
                    "$timestamp : $text ${if (stackTraceBuilder.isNotEmpty()) "\n$stackTraceBuilder" else ""}"


                // It can happen that while writing logs, user ran out of space. If that's the case, we will remove old logs
                // and write log again. That's why loop is used here.
                var stayInLoop: Boolean
                do {
                    try {

                        logFile?.bufferedWriter(shouldAppend = true)?.use {
                            it.append(logToWrite)
                            it.newLine()
                        }

                        stayInLoop = false
                    } catch (e: IOException) {
                        when {
                            e.isOutOfSpaceError() -> {
                                // Exception is no space left, then forcefully delete previous logs and try writing again. If
                                // deleteOldLogs() returned true then we can stay in loop, but if it couldn't delete any log and
                                // returned false then skip this log and break the loop.
                                stayInLoop = SLogFileUtils.deleteFiles(true)
                            }
                            e.message?.contains("stream closed", true) == true -> {
                                // If stream is closed, then do nothing, even if the log is missed. Just exit the loop.
                                stayInLoop = false
                            }
                            else -> {
                                // If exception was other than no space left, then we don't know why app crashed. We will let
                                // the app crash, so it is logged in Crashlytics and we can investigate it further.
                                // Also note that we're not logging it as a non-fatal crash because if we do and let app keep
                                // running, app will pollute Crashlytics with a lot of crashes.
                                throw e
                            }
                        }
                    } finally {
                        try {
                        } catch (e: Exception) {
                        }
                    }
                } while (stayInLoop)

                // Some crashes were logged in Crashlytics, where following line was throwing IllegalStateException. That's
                // why it is kept in try-catch block.
                try {
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun finalLog(tag: String, text: String, logLevel: SLogLevel) {
        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = text.length
        while (i < length) {
            var newline = text.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {

                val end = min(newline, i + MAX_LOG_LENGTH)
                when (logLevel) {
                    SLogLevel.DEBUG -> {
                        Log.d(tag, text.substring(i, end))
                    }
                    SLogLevel.INFO -> {
                        Log.i(tag, text.substring(i, end))
                    }

                    SLogLevel.WARN -> {
                        Log.w(tag, text.substring(i, end))
                    }

                    SLogLevel.ERROR -> {
                        Log.e(tag, text.substring(i, end))
                    }
                }
                i = end
            } while (i < newline)
            i++
        }
    }


    internal fun sendReport(context: Context) {

        if (hideReportDialog) {
            ioScope.launch {
                sendLog(context)
            }
            return
        }


        val reportDialog = SLDialog(context)

        reportDialog.alertDialogCallbacks = object : SLDialog.AlertDialogCallbacks {

            override fun sendButtonClick(message: String) {

                ioScope.launch {
                    sendLog(context, message)
                }
            }

            override fun skipButtonClick() {

                ioScope.launch {
                    sendLog(context, "")
                }
            }
        }

        reportDialog
            .setDialogTitle(titleName)
            .setDialogBackgroundColor(mainDialogBackgroundColor)
            .setTextColor(textColor)
            .setButtonTextColor(buttonTextColor)
            .setInputDrawable(editTextBackground)
            .setSendButtonBackgroundColor(sendButtonBackgroundColor)
            .setFont(typeface)
            .setButtonIcon(buttonIcon?.first, buttonIcon?.second == true)
            .setDialogHandleColor(dialogHandleColor)
            .setSeparatorColor(separatorColor)
            .setTitleTextSize(textSize)
            .setButtonTextSize(buttonTextSize)
            .showAlert()
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun sendLog(context: Context, message: String = ""): String? {

        return coroutineScope {

            val directory = context.getDirForLogs()

            val text = readTextFromFiles(directory).await() ?: return@coroutineScope null

            Log.d("LOG_FILE", "readLog: $text")

            val zipDirectory = context.getDirForZip()

            val logFile =
                zipDirectory.makeChildDirectory(logFileName
                    ?: context.getString(R.string.final_log_name), false)

            try {

                logFile.createNewFile()

                logFile.bufferedWriter(true).use {
                    it.append(text)
                    it.newLine()
                }

                val information = JSONObject().getDeviceInfo(context)

                val jsonFile =
                    zipDirectory.makeChildDirectory(context.getString(R.string.additional_file_name),
                        false)

                jsonFile.createNewFile()

                jsonFile.bufferedWriter(true).use {
                    it.append("$information")
                    it.newLine()
                }

                val directoryZip =
                    zipDirectory.makeChildDirectory(context.getString(R.string.bug_zip_file_name,
                        context.getAppName()), false)

                zipDirectory.makeZipFile(directoryZip, isProtected, passowrd)

                val zipFileUri = context.getPathUri(directoryZip)

                val list = buildList<Uri> {
                    add(zipFileUri)
                    additionalFiles?.let {
                        addAll(it)
                    }
                }
                context.shareFileOnGmail(list, message, mail, subject)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            text
        }
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event.targetState == Lifecycle.State.DESTROYED)
            ioScope.cancel()
    }

}