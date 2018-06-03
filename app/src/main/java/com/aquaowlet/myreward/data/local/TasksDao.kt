/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 2/06/18 5:52 AM
 */

package com.aquaowlet.myreward.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.aquaowlet.myreward.data.Task

/**
 * Interact with database to manage task data.
 */
@Dao
interface TasksDao {

    /**
     * Get all tasks.
     */
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): LiveData<List<Task>>

    /**
     * Get a task by its id.
     */
    @Query("SELECT * FROM Tasks WHERE id = :id")
    fun getTaskById(id: String): LiveData<Task?>

    /**
     * Insert a new task or replace with the new task.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    /**
     * Update a exist task.
     */
    @Update
    fun update(task: Task)

    /**
     * Delete a task.
     */
    @Delete
    fun delete(task: Task)

    /**
     * Delete a task by a id.
     */
    @Query("DELETE FROM Tasks WHERE id = :id")
    fun deleteTaskById(id: String)

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM Tasks")
    fun deleteTasks()
}