package com.aquaowlet.myreward.util

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Util {

    private val simpleDateFormat = SimpleDateFormat(Constant.DATETIME_FORMAT, Locale.ENGLISH)

    /**
     * Parse string to date, catch ParseException.
     */
    fun parseStringToDate(string: String): Date? {

        var date: Date? = null

        try {
            date = simpleDateFormat.parse(string)
        } catch (e: ParseException) {
            Log.d(Constant.DEBUG, "The string \"$string\" can not be parsed..")
        }

        return date
    }

    /**
     * Format date to string or null.
     */
    fun formatDateToString(date: Date?): String? {

        if (date != null) {
            return simpleDateFormat.format(date)
        }

        return null
    }

    /**
     * Return the int value of the string or 0.
     */
    fun parseStringToIntOrZero(string: String): Int {

        var i = 0

        try {
            i = string.toInt()
        } catch (e: NumberFormatException) {
            Log.d(Constant.DEBUG, "The string \"$string\" can not be parsed to Int.")
        }

        return i
    }

}