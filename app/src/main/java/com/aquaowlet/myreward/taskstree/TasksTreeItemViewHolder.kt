/*
 * Created by Eric Hongming Lin on 20/05/18 3:05 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 2:03 AM
 */

package com.aquaowlet.myreward.taskstree

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.util.Constant
import com.unnamed.b.atv.model.TreeNode
import kotlinx.android.synthetic.main.item_task.view.*

class TasksTreeItemViewHolder(context: Context?) : TreeNode.BaseNodeViewHolder<Task>(context) {

    override fun createNodeView(node: TreeNode?, value: Task?): View {
        // The tree node is not null.
        node!!
        // Inflate the tasks tree item
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_task, null, false)
        view.text_task_name.text = value?.name
        view.icon_overflow.setOnClickListener {
            Log.d(Constant.TESTING, "icon_overflow is clicked")
        }
        node.setLongClickListener { theNode, theValue ->
            theValue as Task
            theNode.viewHolder.view.startDrag(
                    null,
                    View.DragShadowBuilder(theNode.viewHolder.view),
                    theNode,
                    0
            )
            true
        }
        view.setOnDragListener(TasksTreeItemDragEventListener(node))
        return view
    }

    override fun toggle(active: Boolean) {
        if (active) {
            view.icon_collapse.iconText = context.resources.getString(R.string.ic_expand)
        } else {
            view.icon_collapse.iconText = context.resources.getString(R.string.ic_collapse)
        }
    }
}