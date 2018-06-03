/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 4/06/18 2:38 AM
 */

package com.aquaowlet.myreward.taskstree

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.PopupMenu
import com.aquaowlet.myreward.R
import kotlinx.android.synthetic.main.fragment_tasks_tree.view.*

/**
 * Present the tasks tree view and setup the ViewModel to transfer data from database to the tree view.
 */
class TasksTreeFragment : Fragment() {

    var tasksTreeViewModel: TasksTreeViewModel? = null

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