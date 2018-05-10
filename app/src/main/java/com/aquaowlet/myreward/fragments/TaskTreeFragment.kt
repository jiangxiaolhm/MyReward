package com.aquaowlet.myreward.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.holders.TaskTreeItemHolder
import com.aquaowlet.myreward.models.Task
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import kotlinx.android.synthetic.main.fragment_task_tree.view.*
import java.util.*

class TaskTreeFragment : Fragment() {

    private var taskTreeView: AndroidTreeView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_task_tree, null, false)
        val taskTreeContainerView = rootView.layout_task_tree_container

        val root = TreeNode.root()

        val parent = TreeNode(Task("parent", Date())).setViewHolder(TaskTreeItemHolder(context))
        val child0 = TreeNode(Task("child 0", Date())).setViewHolder(TaskTreeItemHolder(context))
        val child1 = TreeNode(Task("child 1", Date())).setViewHolder(TaskTreeItemHolder(context))
        val child2 = TreeNode(Task("child 2", Date())).setViewHolder(TaskTreeItemHolder(context))
        val child3 = TreeNode(Task("child 3", Date())).setViewHolder(TaskTreeItemHolder(context))
        parent.addChildren(child0, child1)
        root.addChild(parent)
        child0.addChildren(child2, child3)
        taskTreeView = AndroidTreeView(context, root)
        taskTreeView?.setUseAutoToggle(false)
        taskTreeView?.setDefaultNodeClickListener { node, _ ->
            if (!node.isLeaf) {
                taskTreeView?.toggleNode(node)
            }
        }

        taskTreeContainerView.addView(taskTreeView?.view)

        return rootView
    }
}
