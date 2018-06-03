/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 4/06/18 2:28 AM
 */

package com.aquaowlet.myreward.data.local

import android.arch.lifecycle.LiveData
import android.content.Context
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.data.TaskCurrentAndChildren

/**
 * Concrete implementations to manage the task data with the database.
 */
class TasksRepository private constructor(context: Context) {

    private val tasksDao: TasksDao
    private val taskCurrentAndChildrenDao: TaskCurrentAndChildrenDao
    private val allTasks: LiveData<List<Task>>
    private val allCurrentAndChildren: LiveData<List<TaskCurrentAndChildren>>

    init {
        val appDatabase = AppDatabase.getInstance(context)
        tasksDao = appDatabase.tasksDao()
        allTasks = tasksDao.getAllTasks()
        taskCurrentAndChildrenDao = appDatabase.taskCurrentAndChildren()
        allCurrentAndChildren = taskCurrentAndChildrenDao.getTaskCurrentAndChildren()
    }

    /**
     * Get all current and children relationships.
     */
    fun getAllCurrentAndChildren(): LiveData<List<TaskCurrentAndChildren>> {
        return allCurrentAndChildren
    }

    /**
     * Insert a new task.
     */
    fun insert(task: Task) {
        Thread(Runnable {
            tasksDao.insert(task)
        }).start()
    }

    /**
     * Update a task.
     */
    fun update(task: Task) {
        Thread(Runnable {
            tasksDao.update(task)
        }).start()
    }

    /**
     * Delete a task.
     */
    fun delete(task: Task) {
        Thread(Runnable {
            tasksDao.delete(task)
        }).start()
    }


    companion object {
        @Volatile
        private var INSTANCE: TasksRepository? = null

        /**
         * Get or create a TasksRepository with singleton pattern.
         */
        fun getInstance(context: Context): TasksRepository {
            if (INSTANCE == null) {
                synchronized(TasksRepository::class) {
                    if (INSTANCE == null) {
                        INSTANCE = TasksRepository(context)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}