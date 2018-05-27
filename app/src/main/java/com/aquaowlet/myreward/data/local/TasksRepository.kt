/*
 * Created by Eric Hongming Lin on 20/05/18 3:27 PM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 3:27 PM
 */

package com.aquaowlet.myreward.data.local

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.AsyncTask
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.data.TaskCurrentAndChildren

class TasksRepository(context: Context) {

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
//        tasksDao.insertTask(task)
    }

    class InsertAsyncTask(dao: TasksDao) : AsyncTask<Task, Void, Void>() {
        private val tasksDao = dao

        override fun doInBackground(vararg params: Task?): Void? {
            tasksDao.insertTask(params[0]!!)
            return null
        }

    }

}