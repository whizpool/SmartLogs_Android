package com.whizpool.supportsystem

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.ColorInt
import com.whizpool.supportsystem.enums.LogLevel
import com.whizpool.supportsystem.utils.FileUtils.KEEP_OLD_LOGS_UP_TO_DAYS

object LogBuilder {

    fun setDefaultTag(tagName: String) = apply {
        SLog.TAG = tagName
    }

    fun setLogFileName(name: String) = apply {
        SLog.logFileName = name
    }

    fun deleteOldLogs(forcefullyDelete: Boolean = false) = apply {
        SLog.deleteLogs(forcefullyDelete)
    }

    fun setDaysForLog(numberOfDays: Int) = apply {
        KEEP_OLD_LOGS_UP_TO_DAYS = numberOfDays
    }

    fun setPassword(passwordValue: String) = apply {
        SLog.isProtected = true
        SLog.passowrd = passwordValue
    }

    fun setDialogTypeface(typeface: Typeface?) = apply {
        SLog.typeface = typeface
    }

    fun addAttachment(files: List<Uri>) = apply {
        SLog.additionalFiles = files
    }

    fun setTitle(title: String) = apply {
        SLog.titleName = title
    }

    fun setEmail(mail: String) = apply {
        SLog.mail = mail
    }

    fun setSubjectToEmail(text: String) = apply {
        SLog.subject = text
    }

    fun setTitleFontSize(size: Float) = apply {
        SLog.textSize = size
    }

    fun setSendButtonFontSize(size: Float) = apply {
        SLog.buttonTextSize = size
    }

    fun setMainBackgroundColor(@ColorInt mainDialogBackgroundColor: Int) = apply {
        SLog.mainDialogBackgroundColor = mainDialogBackgroundColor
    }

    fun setTextViewBackgroundColor(@ColorInt textColorValue: Int) = apply {
        SLog.textColor = textColorValue
    }

    fun setSendBtnImage(buttonIcon: Drawable, shouldNull: Boolean) = apply {
        SLog.buttonIcon = Pair(buttonIcon, shouldNull)
    }

    fun setSendButtonBackgroundColor(@ColorInt sendButtonBackgroundColor: Int) = apply {
        SLog.sendButtonBackgroundColor = sendButtonBackgroundColor
    }

//    fun dialogSkipButtonBackgroundColor(@ColorInt skipButtonBackgroundColor: Int) = apply {
//        SLog.skipButtonBackgroundColor = skipButtonBackgroundColor
//    }

    fun dialogEditTextBackground(editTextBackground: Drawable) = apply {
        SLog.editTextBackground = editTextBackground
    }

    fun setDialogButtonTextColor(@ColorInt color: Int) = apply {
        SLog.buttonTextColor = color
    }

    fun setLineColor(@ColorInt color: Int) = apply {
        SLog.separatorColor = color
    }

    fun setKnobColor(@ColorInt color: Int) = apply {
        SLog.dialogHandleColor = color
    }

//    @JvmOverloads
//    fun dialogCustomization(
//        @ColorInt
//        mainDialogBackgroundColor: Int?,
//        @ColorInt
//        textColorValue: Int? = null,
//        cancelIconDrawable: Drawable? = null,
//        editTextBackground: Drawable? = null,
//        @ColorInt
//        sendButtonBackgroundColor: Int? = null,
//        @ColorInt
//        skipButtonBackgroundColor: Int? = null,
//    ) = apply {
//
//        SLog.mainDialogBackgroundColor = mainDialogBackgroundColor
//        SLog.textColor = textColorValue
//        SLog.buttonIcon = cancelIconDrawable
//        SLog.editTextBackground = editTextBackground
//        SLog.sendButtonBackgroundColor = sendButtonBackgroundColor
//        SLog.skipButtonBackgroundColor = skipButtonBackgroundColor
//    }

    fun build(context: Context) {
        SLog.initialize(context)
    }

    @JvmOverloads
    fun sendReport(context: Context, files: List<Uri>? = null) {
        SLog.additionalFiles = files
        SLog.showReportDialog(context)
    }


    @JvmOverloads
    fun summaryLog(
        tag: String = SLog.TAG,
        text: String = "",
        shouldSave: Boolean = true,
        exception: Exception? = null,
    ) {
        SLog.log(tag, text, logLevel = LogLevel.DEBUG, shouldSave, exception)
    }


    @JvmOverloads
    fun detailLogs(
        tag: String = SLog.TAG,
        text: String = "",
        shouldSave: Boolean = true,
        exception: Exception? = null,
    ) {
        SLog.log(tag, text, LogLevel.DEBUG, shouldSave, null)
        SLog.log(tag, text, LogLevel.ERROR, shouldSave, exception)
    }


    @JvmOverloads
    fun logInfo(
        tag: String = SLog.TAG,
        text: String = "",
        shouldSave: Boolean = true,
        exception: Exception? = null,
    ) {
        SLog.log(tag, text, logLevel = LogLevel.INFO, shouldSave, exception)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////


    @JvmOverloads
    fun logWarn(
        tag: String = SLog.TAG,
        text: String = "",
        shouldSave: Boolean = true,
        exception: Exception? = null,
    ) {
        SLog.log(tag, text, logLevel = LogLevel.WARN, shouldSave, exception)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////


    @JvmOverloads
    fun logError(
        tag: String = SLog.TAG,
        text: String = "",
        shouldSave: Boolean = true,
        exception: Exception? = null,
    ) {
        SLog.log(tag, text, logLevel = LogLevel.ERROR, shouldSave, exception = exception)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////


}