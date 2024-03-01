package com.whizpool.supportsystem.utils

import android.content.Context
import androidx.core.content.FileProvider
import com.whizpool.supportsystem.SLogHelper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import java.util.Date

object SLogFileUtils {

    private const val LOG_FILE_ROOT_DIR_NAME = "Logs"
    private const val ZIP_FILE_DIRECTORY = "NewZip"
    var KEEP_OLD_LOGS_UP_TO_DAYS = 7 // TO BE PROVIDED

    private var rootDirForLogs: File? = null
    private var zipFileDirectory: File? = null


    fun Context.getDirForLogs() =
        rootDirForLogs ?: File(filesDir, LOG_FILE_ROOT_DIR_NAME).apply { mkdirs() }.also {
            rootDirForLogs = it
        }

    fun Context.getDirForZip() =
        zipFileDirectory ?: File(filesDir, ZIP_FILE_DIRECTORY).apply { mkdirs() }.also {
            zipFileDirectory = it
        }


    fun File.makeChildDirectory(
        childDirectoryName: String,
        makeNewDirectory: Boolean = true,
    ) =
        File(this, childDirectoryName).apply {
            if (this.exists()) {
                deleteRecursively()
            }
            if (makeNewDirectory)
                mkdirs()
        }


    fun Context.getPathUri(file: File) = FileProvider.getUriForFile(
        this,
        "$packageName.${SLogHelper.fileProviderSuffix}",
        file
    )

    suspend fun deleteFiles(
        forcefullyDelete: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ): Boolean {


        return withContext(Dispatchers.Default) {

            val logDirectories = rootDirForLogs?.listFiles()

            val logDirectoriesSize = logDirectories!!.size

            // If there is only one log directory then don't delete anything and return.
            if (logDirectoriesSize <= 1) {
                return@withContext false
            }

            // If total directories are less than 7, then don't delete any thing. But if requested to forcefully delete,
            // then skip this check and proceed forward.
            if (logDirectoriesSize <= KEEP_OLD_LOGS_UP_TO_DAYS && !forcefullyDelete) {
                return@withContext false
            }

            // Sort the directories in alphabetical order (in ascending dates)
            logDirectories.sortWith { o1, o2 -> o1.compareTo(o2) }

            for ((index, file) in logDirectories.withIndex()) {

                ensureActive()
                // Delete file
                file.deleteRecursively()

                // After deleting it, check how many drafts are left. If they are less than or equal to 7 then return.
                if (logDirectoriesSize - (index + 1) <= KEEP_OLD_LOGS_UP_TO_DAYS) {
                    break
                }
            }

            onComplete?.let {
//                withContext(Dispatchers.Main) {
                it.invoke()
//                }
            }
            return@withContext true
        }
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun readTextFromFiles(file: File): Deferred<String?> {

        val text = java.lang.StringBuilder()

        return coroutineScope {

            deleteFiles() // deleting the files if exceed from limit

            async {
                val listFiles = file.listFiles()?.apply {
                    sortBy { it.lastModified() }
                } ?: return@async null

                for (element in listFiles) {
                    try {
                        text.append(
                            "******************************* ${
                                Date(element.lastModified()).getFormattedDate(LOG_FILE_DATE_FORMAT)
                            } *******************************\n"
                        )
                        text.append("\n")
                        element.bufferedReader().use {
                            text.append(it.readText())
                            text.append('\n')
                        }

                        text.append('\n')

                    } catch (e: IOException) {
                    }
                }
                text.toString()
            }
        }
    }

    /**
     * Constructs a new  FileOutputStream of this file and returns it as a result.
     */
    private fun File.outputStream(shouldAppend: Boolean = false): FileOutputStream {
        return FileOutputStream(this, shouldAppend)
    }


    /**
     * Returns a new [FileWriter] for writing the content of this file.
     */
    private fun File.writer(
        shouldAppend: Boolean = false,
        charset: Charset = Charsets.UTF_8,
    ): OutputStreamWriter =
        outputStream(shouldAppend).writer(charset)

    /**
     * Returns a new [BufferedWriter] for writing the content of this file.
     *
     * @param bufferSize necessary size of the buffer.
     */
    fun File.bufferedWriter(
        shouldAppend: Boolean = false,
        charset: Charset = Charsets.UTF_8,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
    ): BufferedWriter =
        writer(shouldAppend, charset).buffered(bufferSize)
}