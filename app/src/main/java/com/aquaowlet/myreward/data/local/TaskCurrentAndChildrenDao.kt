/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 2/06/18 6:21 AM
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