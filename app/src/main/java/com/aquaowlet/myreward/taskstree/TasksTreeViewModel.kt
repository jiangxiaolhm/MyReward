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
import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.data.TaskParentChildren
import com.aquaowlet.myreward.data.local.TasksRepository
import com.unnamed.b.atv.model.TreeNode

class TasksTreeViewModel(context: Application) : AndroidViewModel(context) {

    private val tasksRepository = TasksRepository(context)
    private val allTasks: LiveData<List<Task>>
    private val allParentChildren: LiveData<List<TaskParentChildren>>
    private var tasksTreeNodes: List<TreeNode> = arrayListOf()
    private val tasks: ObservableList<Task> = ObservableArrayList<Task>()

    init {
        allTasks = tasksRepository.getAllTasks()
        allParentChildren = tasksRepository.getAllParentChildren()
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return allTasks
    }

    fun getAllParentChildren(): LiveData<List<TaskParentChildren>> {
        return allParentChildren
    }

    fun insert(task: Task) {
        tasksRepository.insert(task)
    }

}