/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 4/06/18 2:30 AM
 */

package com.aquaowlet.myreward.taskstree.item

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.addedittask.AddEditTaskActivity
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.taskdetails.TaskDetailsActivity
import com.aquaowlet.myreward.taskstree.TasksTreeViewModel
import com.aquaowlet.myreward.util.Constant
import com.daimajia.swipe.SwipeLayout
import com.unnamed.b.atv.model.TreeNode
import kotlinx.android.synthetic.main.item_task.view.*

/**
 * Create and display the tree node on the screen and handle events.
 */
class TasksTreeItemViewHolder(
        context: Context?,
        private val tasksTreeViewModel: TasksTreeViewModel
) : TreeNode.BaseNodeViewHolder<Task>(context) {

    init {
        tView = tasksTreeViewModel.tasksTreeView
    }

    @SuppressLint("InflateParams")
    @Suppress("DEPRECATION")
    override fun createNodeView(node: TreeNode?, value: Task?): View {

        // The tree node is not null.
        node!!
        value!!

        // Inflate the tasks tree item
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_task, null, false)

        when (tasksTreeViewModel.filterId) {
            R.id.todo_filter -> {
                if (value.status != Task.STATUS_TODO || value.archived) {
                    view.visibility = View.GONE
                }
            }
            R.id.unarchived_filter -> {
                if (value.archived) {
                    view.visibility = View.GONE
                }
            }
        }

        view.swipe.showMode = SwipeLayout.ShowMode.LayDown
        view.swipe.addDrag(SwipeLayout.DragEdge.Right, null)

        when (value.status) {
            Task.STATUS_TODO -> {

                view.icon_complete.visibility = View.GONE
                view.swipe.addDrag(SwipeLayout.DragEdge.Left, view.bottom_view)
                view.icon_to_complete.setOnClickListener {
                    view.swipe.close()
                    tasksTreeViewModel.complete(value)
                    Toast.makeText(context, context.getString(R.string.task_complete_message), Toast.LENGTH_SHORT).show()
                }
            }
            Task.STATUS_COMPLETE -> {
                view.icon_complete.visibility = View.VISIBLE
                view.icon_complete.iconText = context.getString(R.string.ic_check_circle_outline)
            }
            Task.STATUS_OVERDUE -> {
                view.icon_complete.visibility = View.VISIBLE
                view.icon_complete.iconText = context.getString(R.string.ic_info_outline)
                view.icon_complete.iconColor = ColorStateList.valueOf(context.resources.getColor(R.color.color_alert))
            }
        }

        when (value.type) {
            Task.TYPE_TASK -> {
                view.icon_reward.visibility = if (value.status == Task.STATUS_TODO && value.reward.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

                view.icon_punishment.visibility = if (value.status == Task.STATUS_TODO && value.punishment.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            Task.TYPE_REWARD -> {

            }
            Task.TYPE_PUNISHMENT -> {

            }
        }

        view.swipe.setOnLongClickListener {
            node.viewHolder.view.startDrag(
                    null,
                    View.DragShadowBuilder(node.viewHolder.view),
                    node,
                    0
            )
            true
        }

        view.icon_collapse.setOnClickListener {
            if (!node.isLeaf) {
                tView.toggleNode(node)
            }
        }

        view.text_task_name.text = value.name

        view.icon_archived.visibility = if (value.archived) {
            View.VISIBLE
        } else {
            View.GONE
        }

        view.icon_overflow.setOnClickListener {
            showPopupMenu(it)
        }

        view.setOnDragListener(TasksTreeItemDragEventListener(node))
        return view
    }

    override fun toggle(active: Boolean) {
        val task = mNode.value as Task
        task.expanded = active

        if (active) {
            view.icon_collapse.iconText = context.resources.getString(R.string.ic_expand)
        } else {
            view.icon_collapse.iconText = context.resources.getString(R.string.ic_collapse)
        }
    }

    /**
     * Show the collapse icon in the tree node.
     */
    fun showCollapse() {
        view.icon_collapse.visibility = View.VISIBLE
    }

    /**
     * Update the indent of the tree node with its level in the tree.
     */
    fun updateIndent() {
        view.text_tree_node_indent?.width = (mNode.level - 1) * 20
    }

    /**
     * Create and show popup menu with icons and texts.
     */
    private fun showPopupMenu(view: View) {

        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.menu_tree_node_overflow)
        popupMenu.setOnMenuItemClickListener {

            var result = true
            when (it.itemId) {
                R.id.view_details_action -> {
                    val intent = Intent(context, TaskDetailsActivity::class.java)
                    intent.putExtra(Constant.INTENT_TYPE, Constant.INTENT_VIEW_DETAILS)
                    intent.putExtra(Constant.INTENT_TASK, (mNode.value as Task))
                    context.startActivity(intent)
                }
                R.id.add_child_action -> {
                    val intent = Intent(context, AddEditTaskActivity::class.java)
                    intent.putExtra(Constant.INTENT_TYPE, Constant.INTENT_ADD_TASK)
                    intent.putExtra(Constant.INTENT_PARENT_TASK, (mNode.value as Task))
                    context.startActivity(intent)
                }
                R.id.archive_action -> {

                    tasksTreeViewModel.archive(mNode.value as Task, true)
                    var undo = false
                    val snackBar = Snackbar.make(view, context.getString(R.string.alert_dialog_archive_task_message), Snackbar.LENGTH_INDEFINITE)
                    snackBar.setAction(context.getString(R.string.undo), {
                        undo = true
                        snackBar.dismiss()
                    }).addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            if (undo) {
                                tasksTreeViewModel.archive(mNode.value as Task, false)
                            }
                        }
                    }).show()

                    Thread(Runnable {
                        Thread.sleep(5000)
                        snackBar.dismiss()
                    }).start()

                }
                R.id.delete_action -> {

                    tasksTreeViewModel.delete(mNode.value as Task)
                    var undo = false
                    val snackBar = Snackbar.make(view, context.getString(R.string.alert_dialog_delete_task_message), Snackbar.LENGTH_INDEFINITE)
                    snackBar.setAction(context.getString(R.string.undo), {
                        undo = true
                        snackBar.dismiss()
                    }).addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            if (undo) {
                                tasksTreeViewModel.restore(mNode.value as Task)
                            }
                        }
                    }).show()

                    Thread(Runnable {
                        Thread.sleep(5000)
                        snackBar.dismiss()
                    }).start()
                }
                else -> {
                    Log.e(Constant.ERROR, "Unknown menu item clicked.")
                    result = false
                }
            }
            result
        }

        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.e("Main", "Error showing menu icons.", e)
        } finally {
            popupMenu.show()
        }

    }

}