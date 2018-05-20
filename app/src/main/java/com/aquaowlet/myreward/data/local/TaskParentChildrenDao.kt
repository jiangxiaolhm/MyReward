/*
 * Created by Eric Hongming Lin on 20/05/18 10:59 PM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 10:59 PM
 */

package com.aquaowlet.myreward.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.aquaowlet.myreward.data.TaskParentChildren

@Dao
interface TaskParentChildrenDao {

    @Query("SELECT * FROM tasks")
    fun getTaskParentChildren(): LiveData<List<TaskParentChildren>>
}