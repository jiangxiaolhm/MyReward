/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 4:47 PM
 */

package com.aquaowlet.myreward.data.local

import android.arch.persistence.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}