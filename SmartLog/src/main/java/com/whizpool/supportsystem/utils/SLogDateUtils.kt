package com.whizpool.supportsystem.utils

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

const val LOG_FILE_DATE_FORMAT = "yyyy-MM-dd"
const val LOG_TIMESTAMP_FORMAT = "yyyy-MM-dd kk:mm:ss.SSS"


fun DateTime.getFormattedDate(format: String, locale: Locale = Locale.US): String {
    return this.toDate().getFormattedDate(format, locale)
}

fun Date.getFormattedDate(format: String, locale: Locale = Locale.US): String {
    return SimpleDateFormat(format, locale).format(this)
}