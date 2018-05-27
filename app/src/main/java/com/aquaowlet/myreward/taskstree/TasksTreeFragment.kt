/*
 * Created by Eric Hongming Lin on 20/05/18 3:05 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 2:26 AM
 */

package com.aquaowlet.myreward.taskstree

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.data.Task
import com.unnamed.b.atv.model.TreeNode
import kotlinx.android.synthetic.main.fragment_tasks_tree.view.*
import kotlinx.android.synthetic.main.item_task.view.*
import java.util.*

class TasksTreeFragment : Fragment() {

    private var tasksTreeViewModel: TasksTreeViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_tasks_tree, null, false)
        val taskTreeContainerView = rootView.layout_tasks_tree_container
        val root = TreeNode.root()
        val tasksTreeView = TasksTreeViewHolder.getInstance(context!!).taskTreeView
        tasksTreeView.setRoot(root)

        tasksTreeViewModel = ViewModelProviders.of(this).get(TasksTreeViewModel::class.java)

        tasksTreeViewModel!!.getAllParentChildren().observe(this, Observer {
            val queue = LinkedList<TreeNode?>()
            var newNode: TreeNode?


            // Clear the whole tree.
            while (root.children.isNotEmpty()) {
                tasksTreeView.removeNode(root.children[0])
            }

            for (i in it!!) {
                // Current task has children.
                if (i.children.isNotEmpty()) {
                    // Find task whose parentId matches current task.
                    for (j in it) {
                        if (j.current.parentId == i.current.id) {
                            i.current.addChild(j.current)
                        }
                    }
                }

                // Current task is a root task.
                if (i.current.parentId.isEmpty()) {
                    // Create new tree node for current task.
                    newNode = TreeNode(i.current).setViewHolder(TasksTreeItemViewHolder(context))
                    if (i.current.children.isNotEmpty()) {
                        // Current node has children, set the collapse icon to the node.
                        newNode?.viewHolder?.view?.icon_collapse?.visibility = View.VISIBLE
                        // Add current node to the queue to process its children recursively.
                        queue.addLast(newNode)
                    }
                    // Add the tree node to the root of the tree view.
                    tasksTreeView.addNode(root, newNode)
                }
            }

            while (queue.isNotEmpty()) {
                for (task in (queue.first!!.value as Task).children) {
                    // Create the child tree node.
                    newNode = TreeNode(task).setViewHolder(TasksTreeItemViewHolder(context))
                    if (task.children.isNotEmpty()) {
                        // Current node has children, set the collapse icon to the node.
                        newNode?.viewHolder?.view?.icon_collapse?.visibility = View.VISIBLE
                        // Add current node to the queue to process its children recursively.
                        queue.addLast(newNode)
                    }
                    // Add the child node to current node in the tree view.
                    tasksTreeView.addNode(queue.first, newNode)
                    newNode!!.viewHolder?.view?.text_tree_node_indent?.width = (newNode.level - 1) * 20
                }
                // Remove first node.
                queue.removeFirst()
            }
        })

        taskTreeContainerView.addView(tasksTreeView.view)

        return rootView
    }
}