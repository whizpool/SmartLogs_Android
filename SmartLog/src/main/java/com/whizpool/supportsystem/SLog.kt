package com.whizpool.supportsystem

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.ColorInt
import com.whizpool.supportsystem.enums.SLogLevel
import com.whizpool.supportsystem.utils.SLogFileUtils.KEEP_OLD_LOGS_UP_TO_DAYS

object SLog {

    fun setDefaultTag(tagName: String) = apply {
        SLogHelper.TAG = tagName
    }

    fun setLogFileName(name: String) = apply {
        SLogHelper.logFileName = name
    }

    fun deleteOldLogs() = apply {
        SLogHelper.deleteLogs(true)
    }

    fun setDaysForLog(numberOfDays: Int) = apply {
        KEEP_OLD_LOGS_UP_TO_DAYS = numberOfDays
    }

    fun setPassword(passwordValue: String) = apply {
        SLogHelper.isProtected = true
        SLogHelper.passowrd = passwordValue
    }

    fun setDialogTypeface(typeface: Typeface?) = apply {
        SLogHelper.typeface = typeface
    }

    fun addAttachment(files: List<Uri>) = apply {
        SLogHelper.additionalFiles = files
    }

    fun setTitle(title: String) = apply {
        SLogHelper.titleName = title
    }

    fun setEmail(mail: String) = apply {
        SLogHelper.mail = mail
    }

    fun setSubjectToEmail(text: String) = apply {
        SLogHelper.subject = text
    }

    fun setTitleFontSize(size: Float) = apply {
        SLogHelper.textSize = size
    }

    fun setSendButtonFontSize(size: Float) = apply {
        SLogHelper.buttonTextSize = size
    }

    fun setMainBackgroundColor(@ColorInt mainDialogBackgroundColor: Int) = apply {
        SLogHelper.mainDialogBackgroundColor = mainDialogBackgroundColor
    }

    fun setTextViewBackgroundColor(@ColorInt textColorValue: Int) = apply {
        SLogHelper.textColor = textColorValue
    }

    fun setSendBtnImage(buttonIcon: Drawable, shouldNull: Boolean) = apply {
        SLogHelper.buttonIcon = Pair(buttonIcon, shouldNull)
    }

    fun setSendButtonBackgroundColor(@ColorInt sendButtonBackgroundColor: Int) = apply {
        SLogHelper.sendButtonBackgroundColor = sendButtonBackgroundColor
    }


    fun dialogEditTextBackground(editTextBackground: Drawable?) = apply {
        SLogHelper.editTextBackground = editTextBackground
    }

    fun setDialogButtonTextColor(@ColorInt color: Int) = apply {
        SLogHelper.buttonTextColor = color
    }

    fun setLineColor(@ColorInt color: Int) = apply {
        SLogHelper.separatorColor = color
    }

    fun setKnobColor(@ColorInt color: Int) = apply {
        SLogHelper.dialogHandleColor = color
    }

    fun hideReportDialogue(shouldHide: Boolean) = apply {
        SLogHelper.hideReportDialog = shouldHide
    }


    fun build(context: Context) {
        SLogHelper.initialize(context)
    }

    @JvmOverloads
    fun sendReport(context: Context, files: List<Uri>? = null) {
        SLogHelper.additionalFiles = files
        SLogHelper.sendReport(context)
    }


    @JvmOverloads
    fun summaryLog(
        tag: String = SLogHelper.TAG,
        text: String = "",
        shouldSave: Boolean = true,
        exception: Exception? = null,
    ) {
        SLogHelper.log(tag, text, SLogLevel.ERROR, shouldSave, exception)
    }


    @JvmOverloads
    fun detailLogs(
        tag: String = SLogHelper.TAG,
        text: String = "",
        shouldSave: Boolean = false,
        exception: Exception? = null,
    ) {
        SLogHelper.log(tag, text, SLogLevel.DEBUG, shouldSave, exception)
    }


    @JvmOverloads
    fun logInfo(
        tag: String = SLogHelper.TAG,
        text: String = "",
        shouldSave: Boolean = true,
        exception: Exception? = null,
    ) {
        SLogHelper.log(tag, text, logLevel = SLogLevel.INFO, shouldSave, exception)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////


    @JvmOverloads
    fun logWarn(
        tag: String = SLogHelper.TAG,
        text: String = "",
        shouldSave: Boolean = true,
        exception: Exception? = null,
    ) {
        SLogHelper.log(tag, text, logLevel = SLogLevel.WARN, shouldSave, exception)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////


    @JvmOverloads
    fun logError(
        tag: String = SLogHelper.TAG,
        text: String = "",
        shouldSave: Boolean = true,
        exception: Exception? = null,
    ) {
        SLogHelper.log(tag, text, logLevel = SLogLevel.ERROR, shouldSave, exception = exception)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

}