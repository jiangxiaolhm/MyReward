/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 4/06/18 2:36 AM
 */

package com.aquaowlet.myreward.taskstree

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.addedittask.AddEditTaskActivity
import com.aquaowlet.myreward.util.Constant
import kotlinx.android.synthetic.main.activity_tasks_tree.*

/**
 * It is the entry activity of the application.
 * Contain the tasks tree fragment to display the tree view and show the fab button and filter menu to add and filter tasks.
 */
class TasksTreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_tree)

        setTitle(R.string.title_all_tasks)

        fab_add_task.setOnClickListener {
            val intent = Intent(this@TasksTreeActivity, AddEditTaskActivity::class.java)
            intent.putExtra(Constant.INTENT_TYPE, Constant.INTENT_ADD_TASK)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tasks_tree, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.filter_action -> {
                val popupMenu = PopupMenu(this, findViewById(R.id.filter_action))
                popupMenu.inflate(R.menu.menu_task_filter)
                popupMenu.setOnMenuItemClickListener {

                    when (it.itemId) {
                        R.id.todo_filter -> {
                            setTitle(R.string.menu_task_filter_todo)
                        }
                        R.id.unarchived_filter -> {
                            setTitle(R.string.menu_task_filter_unarchived)
                        }
                        R.id.archived_filter -> {
                            setTitle(R.string.menu_task_filter_archived)
                        }
                    }

                    (fragment as TasksTreeFragment).tasksTreeViewModel?.changeFilter(it.itemId)
                    true
                }
                popupMenu.show()
            }
        }

        return true
    }
}
