package com.aquaowlet.myreward.views

import android.graphics.Color
import android.util.Log
import android.view.DragEvent
import android.view.View
import com.aquaowlet.myreward.holders.TaskTreeViewHolder
import com.aquaowlet.myreward.models.Task
import com.aquaowlet.myreward.util.Constant
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView

class TaskTreeItemDragEventListener(taskNode: TreeNode?) : View.OnDragListener {

    private val mTaskTreeNode = taskNode
    private var mTaskTreeView: AndroidTreeView? = null

    override fun onDrag(v: View?, event: DragEvent?): Boolean {

        when (event?.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                v?.setBackgroundColor(Color.BLUE)
                v?.invalidate()
                return true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                v?.setBackgroundColor(Color.GREEN)
                v?.invalidate()
                return true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                return true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                v?.setBackgroundColor(Color.BLUE)
                v?.invalidate()
                return true
            }
            DragEvent.ACTION_DROP -> {

//                val taskTreeItem = event.clipData.getItemAt(0)

                val draggedTaskNode = event.localState

                Log.d(Constant.TESTING, "${v?.left} ${v?.right} ${v?.top} ${v?.bottom}")
//
//                var locationOnScreen = IntArray(2)
//                v?.getLocationOnScreen(locationOnScreen)
//                if (locationOnScreen != null) {
//                    Log.d(Constant.TESTING, "location on screen x: ${locationOnScreen[0]} y: ${locationOnScreen[1]}")
//                }
//                var locationInWindow = IntArray(2)
//                v?.getLocationInWindow(locationInWindow)
//                if (locationInWindow != null) {
//                    Log.d(Constant.TESTING, "location in window x: ${locationInWindow[0]} y: ${locationInWindow[1]}")
//                }

//                Log.d(Constant.TESTING, "event x: ${event.x} y: ${event.y}")

//                if (draggedTaskNode is TreeNode) {
                draggedTaskNode as TreeNode
                mTaskTreeView = TaskTreeViewHolder.getInstance(v?.context).taskTreeView
                if (mTaskTreeNode != null) {
                    if (draggedTaskNode === mTaskTreeNode) {
                        Log.d(Constant.TESTING, "Same node")
                    } else if (draggedTaskNode.parent === mTaskTreeNode.parent) {
                        Log.d(Constant.TESTING, "Brother nodes")
//                        mTaskTreeView?.removeNode(draggedTaskNode)
//                        draggedTaskNode.parent.children.remove(draggedTaskNode)
//                        val insertPoint = mTaskTreeNode.children.indexOf(mTaskTreeNode) + 1
//                        mTaskTreeNode.parent.children.add(insertPoint, draggedTaskNode)

                        mTaskTreeView?.removeNode(draggedTaskNode)
                        mTaskTreeView?.addNode(mTaskTreeNode.parent, draggedTaskNode)

//                        draggedTaskNode.parent.deleteChild(draggedTaskNode)
//                        mTaskTreeNode.parent.addChild(draggedTaskNode)
//                        mTaskTreeNode.parent.viewHolder.view.invalidate()


                    } else if (draggedTaskNode.parent === mTaskTreeNode || mTaskTreeNode.parent === draggedTaskNode) {
                        Log.d(Constant.TESTING, "Parent and child")
                    } else {
                        Log.d(Constant.TESTING, "Other relationship")
                    }

                }
                Log.d(Constant.TESTING, "Dragged Task Node name " + (draggedTaskNode.value as Task).name)
//                }

                Log.d(Constant.TESTING, "Current Task Node name " + (mTaskTreeNode?.value as Task).name)

//                Log.d(Constant.TESTING, "Action Drop ${taskTreeItem.text}")
                return true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                if (event.result) {
                    Log.d(Constant.TESTING, "handled")
                } else {
                    Log.d(Constant.TESTING, "not handled")
                }
                return true
            }
            else -> {
                Log.d(Constant.TESTING, "error")
                return true
            }
        }

    }
}