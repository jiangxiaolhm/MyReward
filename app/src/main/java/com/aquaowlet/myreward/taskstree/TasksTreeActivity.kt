/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 4:25 AM
 */

package com.aquaowlet.myreward.taskstree

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.addedittask.AddEditTaskActivity
import com.aquaowlet.myreward.util.Constant
import kotlinx.android.synthetic.main.activity_tasks_tree.*

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
}
