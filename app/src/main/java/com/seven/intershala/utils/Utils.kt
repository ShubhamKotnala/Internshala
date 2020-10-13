package com.seven.intershala.utils

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object getFormattedDate {
        fun getConvertedDate(date: String?): String {
            val parser = SimpleDateFormat("yyyyMMdd HHmmss", Locale.ENGLISH)
            val formatter =  SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH)
            val output = formatter.format(parser.parse(date))

            return output
        }
    }
}