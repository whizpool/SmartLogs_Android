package com.whizpool.supportsystem.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.text.format.Formatter
import android.util.TypedValue
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.app.ShareCompat
import androidx.core.content.res.ResourcesCompat
import com.whizpool.supportsystem.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.EncryptionMethod
import org.json.JSONObject
import java.io.File
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext


fun Context.getAppName(): String = applicationInfo.loadLabel(packageManager).toString()


@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
private fun hasAndroid9() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

fun Context.getVersionName(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName
}

fun Context.buildNumber(): Long {
    return packageManager.getPackageInfo(packageName, 0).run {
        if (hasAndroid9()) longVersionCode else versionCode.toLong()
    }
}

fun Exception.isOutOfSpaceError(): Boolean {
    return message?.contains("No space left on device", true) == true
}


fun Context.getAvailableBytes(): String? {
//    val stat = StatFs(Environment.getExternalStorageDirectory().path)
//    val bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
    val bytesAvailable = File(getExternalFilesDir(null).toString()).freeSpace
    return Formatter.formatFileSize(this, bytesAvailable)
//    return bytesAvailable / (1024 * 1024)
}

fun getDeviceInfo(context: Context): JSONObject {

    return JSONObject().apply {
        put("App_Version", "${context.getVersionName()} (${context.buildNumber()})")
        put("OS_Version", "${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})")
        put("Device_Manufacturer", Build.MANUFACTURER)
        put("Device_Model", Build.MODEL)
        put("Space_Available", "${context.getAvailableBytes()}")
    }
}


fun File.makeZipFile(
    pathWhereSave: File,
    isProtected: Boolean = false,
    pass: String = "123456",

    ) {
    if (isProtected) {
        val zipParameters = ZipParameters().apply {
            isEncryptFiles = true
            encryptionMethod = EncryptionMethod.ZIP_STANDARD
        }
        val filesToAdd = this.listFiles()?.toMutableList()
        ZipFile(pathWhereSave, pass.toCharArray()).apply {
            addFiles(filesToAdd, zipParameters)
        }

    } else {
        ZipFile(pathWhereSave).addFolder(File(this.toURI()))
    }
}

fun ShareCompat.IntentBuilder.addStreamsToIntentBuilder(streams: List<Uri>): ShareCompat.IntentBuilder {
    streams.forEach {
        addStream(it)
    }
    return this
}

fun Context.shareFileOnGmail(
    filesUri: List<Uri>,
    message: String,
    mail: String?,
    subject: String?,
) {

    ShareCompat.IntentBuilder(this)
        .setType("vnd.android.cursor.dir/rmail")
        .addEmailTo(mail ?: "")
        .setSubject(subject ?: getString(R.string.email_subject, getAppName()))
        .setText(message)
        .setChooserTitle(getString(R.string.chooser_title))
//        .setStream(filesUri)
        .addStreamsToIntentBuilder(filesUri)
        .intent.apply {
            setPackage("com.google.android.gm")
            startActivity(this)
        }
}


fun Context.getMyColor(@ColorRes color: Int) = ResourcesCompat.getColor(resources, color, theme)

fun Context.getMyDrawable(@DrawableRes drawable: Int) =
    ResourcesCompat.getDrawable(resources, drawable, theme)

fun Context.getMyFont(@FontRes font: Int) = ResourcesCompat.getFont(this, font)

fun Context.spToPx(sp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
        sp, resources.displayMetrics)


interface CoroutineCallback<RESULT> {

    companion object {

        @JvmOverloads
        fun <R> call(
            callback: CoroutineCallback<R>,
            dispatcher: CoroutineDispatcher = Dispatchers.Default,
        ): Continuation<R> {
            return object : Continuation<R> {
                override val context: CoroutineContext
                    get() = dispatcher

                override fun resumeWith(result: Result<R>) {
                    callback.onComplete(result.getOrNull(), result.exceptionOrNull())
                }

            }
        }
    }

    fun onComplete(result: RESULT?, error: Throwable?)
}