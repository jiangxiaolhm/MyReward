/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 9:13 PM
 */

package com.aquaowlet.myreward.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.aquaowlet.myreward.data.Task

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("DELETE FROM Tasks WHERE id = :taskId")
    fun deleteTaskById(taskId: String)

    @Query("DELETE FROM Tasks")
    fun deleteTasks()
}