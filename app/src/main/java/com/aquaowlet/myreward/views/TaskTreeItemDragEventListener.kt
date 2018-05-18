package com.aquaowlet.myreward.views

import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.holders.TaskTreeViewHolder
import com.aquaowlet.myreward.util.Constant
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView

class TaskTreeItemDragEventListener(taskNode: TreeNode?) : View.OnDragListener {

    private val mTaskTreeNode = taskNode
    private var mTaskTreeView: AndroidTreeView? = null

    companion object {
        const val MOVE_ABOVE_CURRENT_NODE = 0
        const val MOVE_INTO_SUBTREE_OR_SAME_NODE = 999
        const val MOVE_BELOW_CURRENT_NODE = 1
    }

    override fun onDrag(v: View?, event: DragEvent?): Boolean {

        // The View and DragEvent should not be null.
        v!!
        event!!

        // Get the drop point location in the task tree node.
        // Default drop point is the middle 1/2 area. The dragged node should be inserted into the subtree of current node.
        var dropLocation = MOVE_INTO_SUBTREE_OR_SAME_NODE
        if (event.y < (v.bottom * 0.25)) {
            // If the drop point is in the upper 1/4 area, move dragged node above current node.
            dropLocation = MOVE_ABOVE_CURRENT_NODE

        } else if (event.y > (v.bottom * 0.75)) {
            // If the drop point is in the bottom 1/4 area, move dragged node below current node.
            dropLocation = MOVE_BELOW_CURRENT_NODE
        }

        // Get the task view in the tree node.
        v as ViewGroup
        val taskView = v.getChildAt(1)
        if (taskView.id != R.id.layout_tree_node) {
            Log.e(Constant.ERROR, "The taskView from current view at index 1 is not the layout_tree_node.")
            return false
        }

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                return true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                return true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                val previousBackgroundSetting = taskView.background
                taskView.background =
                        when (dropLocation) {
                            MOVE_INTO_SUBTREE_OR_SAME_NODE -> taskView.context.resources.getDrawable(R.drawable.tree_node_drop_middle)
                            MOVE_ABOVE_CURRENT_NODE -> taskView.context.resources.getDrawable(R.drawable.tree_node_drop_upper)
                            MOVE_BELOW_CURRENT_NODE -> taskView.context.resources.getDrawable(R.drawable.tree_node_drop_bottom)
                            else -> taskView.context.resources.getDrawable(R.drawable.tree_node)
                        }
                if (previousBackgroundSetting != taskView.background) {
                    taskView.invalidate()
                }
                return true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                taskView.background = taskView.context.resources.getDrawable(R.drawable.tree_node)
                taskView.invalidate()
                return true
            }
            DragEvent.ACTION_DROP -> {
                val draggedTaskNode = event.localState as TreeNode
                mTaskTreeView = TaskTreeViewHolder.getInstance(v.context).taskTreeView
                if (mTaskTreeNode != null) {
                    if (draggedTaskNode === mTaskTreeNode) {
                        // Current node and the dragged node are the same node.
                        // Do nothing.
                    } else if (draggedTaskNode.parent === mTaskTreeNode.parent) {
                        // Current node and the dragged node are sibling nodes in a tree.
                        // Change their order.
                        if (dropLocation != MOVE_INTO_SUBTREE_OR_SAME_NODE) {
                            insertNode(draggedTaskNode, dropLocation)
                        } else {
                            // In the middle
                            // TODO insert into the subtree
                        }

                    } else if (draggedTaskNode.parent === mTaskTreeNode || mTaskTreeNode.parent === draggedTaskNode) {
                        Log.d(Constant.TESTING, "Parent and child")
                    } else {
                        Log.d(Constant.TESTING, "Other relationship")
                    }

                }
                return true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                taskView.background = taskView.context.resources.getDrawable(R.drawable.tree_node)
                taskView.invalidate()
                return true
            }
            else -> {
                Log.d(Constant.ERROR, "Here is a unhanded DragEvent")
                return true
            }
        }

    }

    private fun isAncestor(ancestor: TreeNode, descendant: TreeNode): Boolean {

        if (descendant.parent === ancestor) {
            return true
        } else if (descendant.parent != null) {
            return isAncestor(ancestor, descendant.parent)
        }

        return false
    }

    private fun insertNode(draggedTaskNode: TreeNode, dropLocation: Int) {
        // Remove the dragged node from its parent.
        mTaskTreeView!!.removeNode(draggedTaskNode)

        val parentNode = mTaskTreeNode!!.parent
        // The drop point node insertPoint in its parent' children list for insert the dragged node
        val insertPoint = parentNode.children.indexOf(mTaskTreeNode) + dropLocation

        // Temporally store removed nodes after the insert point.
        val removedNodes = mutableListOf<TreeNode>()
        for (i in insertPoint until parentNode.children.size) {
            removedNodes.add(parentNode.children[insertPoint])
            mTaskTreeView!!.removeNode(parentNode.children[insertPoint])
        }

        // Insert the dragged node to the insert point.
        mTaskTreeView!!.addNode(mTaskTreeNode.parent, draggedTaskNode)

        // Insert removed nodes after the inserted dragged node.
        for (i in 0 until removedNodes.size) {
            mTaskTreeView!!.addNode(parentNode, removedNodes[i])
        }
    }
}