/*
 * Created by Eric Hongming Lin on 20/05/18 3:05 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 1:57 AM
 */

package com.aquaowlet.myreward.taskstree

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.data.TaskParentAndChildren
import com.aquaowlet.myreward.data.local.TasksRepository

class TasksTreeViewModel(context: Application) : AndroidViewModel(context) {

    private val tasksRepository = TasksRepository(context)
    private val allTasks: LiveData<List<Task>>
    private val mAllParentAndChildren: LiveData<List<TaskParentAndChildren>>

    init {
        allTasks = tasksRepository.getAllTasks()
        mAllParentAndChildren = tasksRepository.getAllParentChildren()
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return allTasks
    }

    fun getAllParentChildren(): LiveData<List<TaskParentAndChildren>> {
        return mAllParentAndChildren
    }

    fun insert(task: Task) {
        tasksRepository.insert(task)
    }

}