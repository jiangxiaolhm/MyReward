package com.aquaowlet.myreward.views

import android.graphics.Color
import android.util.Log
import android.view.DragEvent
import android.view.View
import com.aquaowlet.myreward.models.Task
import com.aquaowlet.myreward.util.Constant
import com.unnamed.b.atv.model.TreeNode

class TaskTreeItemDragEventListener(node: TreeNode?) : View.OnDragListener {

    private val mNode = node

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

                val task = event.localState

                if (task is Task) {
                    Log.d(Constant.TESTING, "Get from local state" + task.name)
                }

                Log.d(Constant.TESTING, "The drop node" + (mNode?.value as Task).name)

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