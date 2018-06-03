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
import android.arch.persistence.room.Transaction
import com.aquaowlet.myreward.data.TaskCurrentAndChildren

/**
 * Interact with database to manage the current task to its child tasks relationship.
 */
@Dao
interface TaskCurrentAndChildrenDao {

    /**
     * Identify a query to be used to get all tasks and their corresponding children.
     * The results are ordered with the index in their parents' children list.
     */
    @Transaction
    @Query("SELECT * FROM tasks ORDER BY index_in_parent")
    fun getTaskCurrentAndChildren(): LiveData<List<TaskCurrentAndChildren>>
}