package com.aquaowlet.myreward.holders

import android.content.Context
import com.unnamed.b.atv.view.AndroidTreeView

class TaskTreeViewHolder private constructor(context: Context?) {

    val taskTreeView = AndroidTreeView(context)

    init {
        taskTreeView.setUseAutoToggle(false)
        taskTreeView.setDefaultNodeClickListener { node, _ ->
            if (!node.isLeaf) {
                taskTreeView.toggleNode(node)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TaskTreeViewHolder? = null

        fun getInstance(context: Context?): TaskTreeViewHolder {
            if (INSTANCE == null) {
                synchronized(TaskTreeViewHolder::class) {
                    if (INSTANCE == null) {
                        INSTANCE = TaskTreeViewHolder(context)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}