/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 28/05/18 2:38 AM
 */

package com.aquaowlet.myreward.taskstree

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.aquaowlet.myreward.R
import kotlinx.android.synthetic.main.fragment_tasks_tree.view.*

/**
 * Present the tasks tree view.
 */
class TasksTreeFragment : Fragment() {

    private var tasksTreeViewModel: TasksTreeViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_tasks_tree, container, false)
        val taskTreeContainerView = rootView.layout_tasks_tree_container

        // Create the ViewModel.
        tasksTreeViewModel = ViewModelProviders.of(this@TasksTreeFragment).get(TasksTreeViewModel::class.java)

        // When the LiveData change, update the tree view with the tasks and their children.
        tasksTreeViewModel!!.allCurrentAndChildren.observe(this@TasksTreeFragment, Observer {
            if (it != null) {
                tasksTreeViewModel!!.setupTreeView(it)
            }
        })

        taskTreeContainerView.addView(tasksTreeViewModel!!.tasksTreeView.view)




        return rootView
    }

    override fun onPause() {
        super.onPause()

        tasksTreeViewModel!!.updateTreeView()
    }
}