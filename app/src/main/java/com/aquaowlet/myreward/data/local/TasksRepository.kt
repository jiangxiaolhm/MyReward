/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 21/05/18 4:29 PM
 */

package com.aquaowlet.myreward.data.local

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.AsyncTask
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.data.TaskCurrentAndChildren

class TasksRepository private constructor(context: Context) {

    private val tasksDao: TasksDao
    private val mTaskCurrentAndChildrenDao: TaskCurrentAndChildrenDao
    private val allTasks: LiveData<List<Task>>
    private val mAllCurrentAndChildren: LiveData<List<TaskCurrentAndChildren>>

    init {
        val appDatabase = AppDatabase.getInstance(context)
        tasksDao = appDatabase.tasksDao()
        allTasks = tasksDao.getAllTasks()
        mTaskCurrentAndChildrenDao = appDatabase.taskCurrentAndChildren()
        mAllCurrentAndChildren = mTaskCurrentAndChildrenDao.getTaskParentChildren()
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return allTasks
    }

    fun getAllParentChildren(): LiveData<List<TaskCurrentAndChildren>> {
        return mAllCurrentAndChildren
    }

    fun insert(task: Task) {
        InsertAsyncTask(tasksDao).execute(task)
    }

    class InsertAsyncTask(dao: TasksDao) : AsyncTask<Task, Void, Void>() {
        private val tasksDao = dao

        override fun doInBackground(vararg params: Task?): Void? {
            tasksDao.insertTask(params[0]!!)
            return null
        }

    }

    companion object {
        @Volatile
        private var INSTANCE: TasksRepository? = null

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