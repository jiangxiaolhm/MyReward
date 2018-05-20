/*
 * Created by Eric Hongming Lin on 20/05/18 3:05 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 19/05/18 6:19 PM
 */

package com.aquaowlet.myreward.taskstree

import android.content.Context
import com.unnamed.b.atv.view.AndroidTreeView

class TasksTreeViewHolder private constructor(context: Context) {

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
        private var INSTANCE: TasksTreeViewHolder? = null

        fun getInstance(context: Context): TasksTreeViewHolder {
            if (INSTANCE == null) {
                synchronized(TasksTreeViewHolder::class) {
                    if (INSTANCE == null) {
                        INSTANCE = TasksTreeViewHolder(context)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}