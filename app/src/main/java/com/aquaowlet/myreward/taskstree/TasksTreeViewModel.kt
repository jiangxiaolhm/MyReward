/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 28/05/18 2:50 AM
 */

package com.aquaowlet.myreward.taskstree

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.data.TaskCurrentAndChildren
import com.aquaowlet.myreward.data.local.TasksRepository

class TasksTreeViewModel(context: Application) : AndroidViewModel(context) {

    private val tasksRepository = TasksRepository.getInstance(context)
//    private val allTasks: LiveData<List<Task>>
    private val allCurrentAndChildren: LiveData<List<TaskCurrentAndChildren>>

    init {
//        allTasks = tasksRepository.getAllTasks()
        allCurrentAndChildren = tasksRepository.getAllParentChildren()
    }

//    fun getAllTasks(): LiveData<List<Task>> {
//        return allTasks
//    }

    fun getAllParentChildren(): LiveData<List<TaskCurrentAndChildren>> {
        return allCurrentAndChildren
    }

    fun insert(task: Task) {
        tasksRepository.insert(task)
    }

    fun buildTreeView(it: TaskCurrentAndChildren) {

    }

}