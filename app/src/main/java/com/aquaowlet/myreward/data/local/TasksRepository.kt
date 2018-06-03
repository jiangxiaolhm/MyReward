/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 21/05/18 4:29 PM
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

    fun getTaskById(id: String): LiveData<Task?> {
        return tasksDao.getTaskById(id)
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return allTasks
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