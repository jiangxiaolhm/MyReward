/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 4/06/18 2:32 AM
 */

package com.aquaowlet.myreward.taskstree.item

import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.util.Constant
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import com.unnamed.b.atv.view.TreeNodeWrapperView


/**
 * Process the event when the task tree node is dragged and dropped.
 */
class TasksTreeItemDragEventListener(
        private val taskTreeNode: TreeNode
) : View.OnDragListener {

    private val taskTreeView: AndroidTreeView = taskTreeNode.viewHolder.treeView

    companion object {
        const val MOVE_ABOVE_CURRENT_NODE = 0
        const val MOVE_INTO_SUBTREE_OR_SAME_NODE = 999
        const val MOVE_BELOW_CURRENT_NODE = 1
    }

    @Suppress("DEPRECATION")
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
        // Get the swipe layout.
        val swipeLayout = v.getChildAt(1) as ViewGroup
        val taskView = swipeLayout.getChildAt(1)

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
                // When the dragged node and current are the same node or the dragged node is the ancestor node of current, prevent the insertion.
                if (draggedTaskNode != taskTreeNode && !isAncestor(draggedTaskNode, taskTreeNode)) {
                    if (dropLocation != MOVE_INTO_SUBTREE_OR_SAME_NODE) {
                        // Insert the dragged node above or below current node according the drop location.
                        insertNodeAdjacent(draggedTaskNode, dropLocation)
                    } else {
                        // The dragged task node should not be a child of the current task tree node.
                        // Because the dragged task node is already in the subtree of the current task tree node.
                        if (draggedTaskNode.parent != taskTreeNode) {
                            // Insert the dragged into current node's subtree.
                            insertNodeIntoSubtree(draggedTaskNode)
                        }
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

    /**
     * If the first tree node is the ancestor of the second tree node.
     */
    private fun isAncestor(ancestor: TreeNode, descendant: TreeNode): Boolean {

        if (descendant.parent === ancestor) {
            return true
        } else if (descendant.parent != null) {
            return isAncestor(ancestor, descendant.parent)
        }

        return false
    }

    /**
     * Insert the dragged node to the adjacent of the current node.
     */
    private fun insertNodeAdjacent(draggedTaskNode: TreeNode, dropLocation: Int) {

        // Remove the dragged node from it parent and modify its parent node accordingly.
        removeDraggedNodeFromParent(draggedTaskNode)

        val parentNode = taskTreeNode.parent
        // The drop point node insertPoint in its current' children list for insert the dragged node
        val insertPoint = parentNode.children.indexOf(taskTreeNode) + dropLocation

        // Temporally store removed nodes after the insert point.
        val removedNodes = mutableListOf<TreeNode>()
        for (i in insertPoint until parentNode.children.size) {
            removedNodes.add(parentNode.children[insertPoint])
            taskTreeView.removeNode(parentNode.children[insertPoint])
        }

        // Insert the dragged node to the insert point.
        taskTreeView.addNode(taskTreeNode.parent, draggedTaskNode)
        updateTreeNodeIndent(draggedTaskNode)

        // Insert removed nodes after the inserted dragged node.
        for (i in 0 until removedNodes.size) {
            taskTreeView.addNode(parentNode, removedNodes[i])
        }
    }

    /**
     * Add the dragged task node to the last of the current node.
     */
    private fun insertNodeIntoSubtree(draggedTaskNode: TreeNode) {
        // Remove the dragged node from it parent and modify its parent node accordingly.
        removeDraggedNodeFromParent(draggedTaskNode)
        taskTreeView.addNode(taskTreeNode, draggedTaskNode)
        updateTreeNodeIndent(draggedTaskNode)
        val swipeLayout = getTreeNodeRootView(taskTreeNode).getChildAt(1) as ViewGroup
        val treeNodeContentLayout = swipeLayout.getChildAt(1) as ViewGroup
        val treeNodeCollapseIcon = treeNodeContentLayout.getChildAt(0)
        treeNodeCollapseIcon.visibility = View.VISIBLE
    }

    /**
     * Remove the dragged tree node to be insert to other location later.
     */
    private fun removeDraggedNodeFromParent(draggedTaskNode: TreeNode) {
        taskTreeView.removeNode(draggedTaskNode)
        if (draggedTaskNode.parent.children.isEmpty()) {
            val swipeLayout = getTreeNodeRootView(taskTreeNode).getChildAt(1) as ViewGroup
            val treeNodeContentLayout = swipeLayout.getChildAt(1) as ViewGroup
            val treeNodeCollapseIcon = treeNodeContentLayout.getChildAt(0)
            treeNodeCollapseIcon.visibility = View.INVISIBLE
        }
    }

    /**
     * Get the root layout of the tree node from the its wrapper provided by the AndroidTreeView.
     */
    private fun getTreeNodeRootView(node: TreeNode): ViewGroup {
        // Get the tree node outer wrapper view.
        val treeNodeWrapperView = node.viewHolder.view as TreeNodeWrapperView
        // Get the root swipe layout of the tree node
        return treeNodeWrapperView.nodeContainer.getChildAt(0) as ViewGroup
    }

    /**
     * Update the tree node indent and its descendants recursively.
     */
    private fun updateTreeNodeIndent(node: TreeNode) {
        // Get the indent from the root layout.
        val treeNodeIndentTextView = getTreeNodeRootView(node).getChildAt(0) as TextView
        // Set indent according to its level.
        treeNodeIndentTextView.width = (node.level - 1) * 20
        for (child in node.children) {
            updateTreeNodeIndent(child)
        }
    }
}