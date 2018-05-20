/*
 * Created by Eric Hongming Lin on 20/05/18 3:05 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 2:26 AM
 */

package com.aquaowlet.myreward.taskstree

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.databinding.FragmentTasksTreeBinding
import com.unnamed.b.atv.model.TreeNode
import kotlinx.android.synthetic.main.fragment_tasks_tree.view.*

class TasksTreeFragment : Fragment() {

    var tasksTreeViewModel: TasksTreeViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

//        val binding = DataBindingUtil.inflate<FragmentTasksTreeBinding>(inflater, R.layout.fragment_tasks_tree, container, false)

        val rootView = inflater.inflate(R.layout.fragment_tasks_tree, null, false)
//        val rootView = binding.root
//        if (binding.viewmodel != null) {
//            println("is null")
//            binding.viewmodel!!.
//        }

        val taskTreeContainerView = rootView.layout_tasks_tree_container

        val root = TreeNode.root()

        tasksTreeViewModel = ViewModelProviders.of(this).get(TasksTreeViewModel::class.java)
//        println("The size is : " + tasksTreeViewModel?.getAllTasks()?.value?.size)
        tasksTreeViewModel!!.getAllTasks().observe(this, Observer {
            for (item in it!!) {
                println(item.name)
//                item.isTreeNode = false
//                val taskTreeNode = TreeNode(item).setViewHolder(TasksTreeItemViewHolder(context))
            }
        })

        tasksTreeViewModel!!.getAllParentChildren().observe(this, Observer {
            println("The parent and children size: " + it?.size)
            for (item in it!!) {
                println("parent name: " + item.parent.name)
                println("children size: " + item.children.size)
                for (child in item.children) {
                    println("parent name: " + child.name)
                }
            }
        })


//        tasksTreeViewModel!!.insert(Task("test1"))
//        println("The size is : " + tasksTreeViewModel?.getAllTasks()?.value?.size)
//        tasksTreeViewModel!!.insert(Task("test2"))
//        println("The size is : " + tasksTreeViewModel?.getAllTasks()?.value?.size)

//        tasksTreeViewModel!!.getAllTasks().observe

//        binding.viewmodel = tasksTreeViewModel

//        val alltasks = AppDatabase.getInstance(context!!).tasksDao().getAllTasks()
//        println("size : " + alltasks.size)
//        for (item in alltasks) {
//            println("" + item.name + " " + item.id)
//        }

        val parent = TreeNode(Task("parent")).setViewHolder(TasksTreeItemViewHolder(context))
        val child0 = TreeNode(Task("child 0")).setViewHolder(TasksTreeItemViewHolder(context))
        val child1 = TreeNode(Task("child 1")).setViewHolder(TasksTreeItemViewHolder(context))
        val child2 = TreeNode(Task("child 2")).setViewHolder(TasksTreeItemViewHolder(context))
        val child3 = TreeNode(Task("child 3")).setViewHolder(TasksTreeItemViewHolder(context))
        val child4 = TreeNode(Task("child 4")).setViewHolder(TasksTreeItemViewHolder(context))
        parent.addChildren(child0, child1)
        root.addChild(parent)
        child0.addChildren(child2, child3, child4)

        val taskTreeView = TasksTreeViewHolder.getInstance(context!!).taskTreeView
        taskTreeView.setRoot(root)

        taskTreeContainerView.addView(taskTreeView.view)

        return rootView
    }
}