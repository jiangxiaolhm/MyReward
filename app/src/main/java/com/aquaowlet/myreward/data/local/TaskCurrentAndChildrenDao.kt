/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 27/05/18 9:44 PM
 */

package com.aquaowlet.myreward.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.aquaowlet.myreward.data.TaskCurrentAndChildren

@Dao
interface TaskCurrentAndChildrenDao {

    @Query("SELECT * FROM tasks ORDER BY index_in_parent")
    fun getTaskParentChildren(): LiveData<List<TaskCurrentAndChildren>>
}