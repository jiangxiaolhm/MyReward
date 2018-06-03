/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 2/06/18 5:44 AM
 */

package com.aquaowlet.myreward.data.local

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Convert between timestamp in database and Date object in code.
 */
class Converters {

    /**
     * Convert timestamp to Date object.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    /**
     * Convert Date object to timestamp.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}