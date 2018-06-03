/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 4/06/18 2:21 AM
 */

package com.aquaowlet.myreward.taskstree

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.data.TaskCurrentAndChildren
import com.aquaowlet.myreward.data.local.TasksRepository
import com.aquaowlet.myreward.taskstree.item.TasksTreeItemViewHolder
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import java.util.*

/**
 * Process logic and data on the tasks tree screen.
 */
class TasksTreeViewModel(context: Application) : AndroidViewModel(context) {

    private val tasksRepository = TasksRepository.getInstance(context)
    val allCurrentAndChildren: LiveData<List<TaskCurrentAndChildren>>

    private val rootNode = TreeNode.root()
    val tasksTreeView = AndroidTreeView(context)

    var filterId: Int = R.id.unarchived_filter

    init {
        allCurrentAndChildren = tasksRepository.getAllCurrentAndChildren()
        tasksTreeView.setRoot(rootNode)
        tasksTreeView.setUseAutoToggle(false)
    }

    /**
     * Complete a task or set the task overdue.
     */
    fun complete(task: Task) {
        updateTreeView()
        if (task.dueAt != null && task.dueAt!!.before(Date())) {
            // The task is complete but overdue.
            task.status = Task.STATUS_OVERDUE
            if (task.punishment.isNotEmpty()) {
                val punishment = Task(task.punishment)
                punishment.type = Task.TYPE_PUNISHMENT
                tasksRepository.insert(punishment)
            }
        } else {
            // The task has not due date or complete before due date.
            task.status = Task.STATUS_COMPLETE
            if (task.reward.isNotEmpty()) {
                val reward = Task(task.reward)
                reward.type = Task.TYPE_REWARD
                tasksRepository.insert(reward)
            }
        }
        tasksRepository.update(task)
    }

    /**
     * Archive a task and its children.
     */
    fun archive(task: Task, archived: Boolean) {
        updateTreeView()
        val queue = LinkedList<Task>()
        queue.add(task)

        while (queue.isNotEmpty()) {
            queue.first.archived = archived
            queue.addAll(queue.first.children)
            tasksRepository.update(queue.first)
            queue.removeFirst()
        }
    }

    /**
     * Delete a task and its children.
     */
    fun delete(task: Task) {
        updateTreeView()
        val queue = LinkedList<Task>()
        queue.add(task)

        while (queue.isNotEmpty()) {
            queue.addAll(queue.first.children)
            tasksRepository.delete(queue.first)
            queue.removeFirst()
        }
    }

    /**
     * Restore a task and its children.
     */
    fun restore(task: Task) {
        updateTreeView()
        val queue = LinkedList<Task>()
        queue.add(task)

        while (queue.isNotEmpty()) {
            queue.addAll(queue.first.children)
            tasksRepository.insert(queue.first)
            queue.removeFirst()
        }
    }

    /**
     * Setup the tree view with the task current and children relationships.
     */
    fun setupTreeView(tasks: List<TaskCurrentAndChildren>) {
        // The queue is used to traverse the tree with level traversal.
        val queue = LinkedList<TreeNode>()
        var newNode: TreeNode

        // Clear the whole tree.
        while (rootNode.children.isNotEmpty()) {
            tasksTreeView.removeNode(rootNode.children[0])
        }

        for (task in tasks) {
            // Current task has children.
            if (task.children.isNotEmpty()) {
                // Find task whose parentId matches current task.
                for (child in tasks) {
                    if (child.current.parentId == task.current.id) {
                        task.current.addChild(child.current)
                    }
                }
            }

            // Current task is a root task.
            if (task.current.parentId.isEmpty()) {
                // Update index in the parent.
                task.current.indexInParent = rootNode.children.size
                // Create new tree node for current task.
                newNode = TreeNode(task.current).setViewHolder(TasksTreeItemViewHolder(getApplication(), this@TasksTreeViewModel))
                if (task.current.children.isNotEmpty()) {
                    // Current node has children, set the collapse icon to the node.
                    val viewHolder = newNode.viewHolder as TasksTreeItemViewHolder
                    viewHolder.showCollapse()
                    // Add current node to the queue to process its children recursively.
                    queue.addLast(newNode)
                }

                // Add the tree node to the root of the tree view.
                tasksTreeView.addNode(rootNode, newNode)
            }
        }

        while (queue.isNotEmpty()) {

            for (task in (queue.first!!.value as Task).children) {
                // Create the child tree node.
                newNode = TreeNode(task).setViewHolder(TasksTreeItemViewHolder(getApplication(), this@TasksTreeViewModel))
                val viewHolder = newNode.viewHolder as TasksTreeItemViewHolder
                if (task.children.isNotEmpty()) {
                    // Current node has children, set the collapse icon to the node.
                    viewHolder.showCollapse()
                    // Add current node to the queue to process its children recursively.
                    queue.addLast(newNode)
                }
                // Add the child node to current node in the tree view.
                tasksTreeView.addNode(queue.first, newNode)
                viewHolder.updateIndent()
            }

            // Restore the expended status.
            val taskValue = queue.first!!.value as Task
            if (taskValue.expanded) {
                tasksTreeView.expandNode(queue.first)
            } else {
                tasksTreeView.collapseNode(queue.first)
            }

            // Remove first node.
            queue.removeFirst()
        }
    }

    /**
     * Update task from the tree view to the database with level traversal.
     */
    fun updateTreeView() {
        val queue = LinkedList<TreeNode>()

        queue.add(rootNode)
        while (queue.isNotEmpty()) {

            val taskNode = queue.first
            val taskValue = queue.first.value as Task?
            var childTaskValue: Task

            for (i in taskNode.children.indices) {
                childTaskValue = taskNode.children[i].value as Task
                childTaskValue.expanded = taskNode.children[i].isExpanded
                childTaskValue.indexInParent = i
                if (taskNode != rootNode) {
                    childTaskValue.parentId = taskValue?.id!!
                } else {
                    childTaskValue.parentId = ""
                }
                tasksRepository.update(childTaskValue)
                queue.addLast(taskNode.children[i])
            }
            queue.removeFirst()
        }
    }

    /**
     * Change filter and update the tree view.
     */
    fun changeFilter(filterId: Int) {

        if (this.filterId != filterId) {
            this.filterId = filterId
            updateTreeView()
        }

    }

}