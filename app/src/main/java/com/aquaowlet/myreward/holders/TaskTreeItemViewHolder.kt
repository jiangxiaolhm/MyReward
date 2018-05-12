package com.aquaowlet.myreward.holders

import android.content.ClipData
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.models.Task
import com.aquaowlet.myreward.util.Constant
import com.aquaowlet.myreward.views.TaskTreeItemDragEventListener
import com.unnamed.b.atv.model.TreeNode
import kotlinx.android.synthetic.main.item_task.view.*

class TaskTreeItemViewHolder(context: Context?) : TreeNode.BaseNodeViewHolder<Task>(context) {

    override fun createNodeView(node: TreeNode?, value: Task?): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_task, null, false)

        if (node != null) {
            view.placeholder.width = (node.level - 1) * 20
            view.text_task_name.text = value?.name
            if (!node.isLeaf) {
                view.icon_collapse.iconText = context.resources.getString(R.string.ic_collapse)
            }
            view.icon_overflow.setOnClickListener {
                Log.d(Constant.TESTING, "icon_overflow is clicked")
            }

            node.setLongClickListener { node, value ->
                Log.d(Constant.TESTING, "LONG CLICK")

                if (value is Task) {
                    Log.d(Constant.TESTING, "value name ${value.name}")

                    val dragData = ClipData.newPlainText("TASK_NAME", value.name)


                    node.viewHolder.view.startDrag(
                            null,
                            View.DragShadowBuilder(node.viewHolder.view),
                            value,
                            0
                    )
                }

                true
            }

            view.setOnDragListener(TaskTreeItemDragEventListener(node))
        }

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