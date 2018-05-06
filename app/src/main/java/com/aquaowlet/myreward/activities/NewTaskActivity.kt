package com.aquaowlet.myreward.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.models.Task

/**
 * Activity class used to provide a form to create a new task to the user's task list.
 */
class NewTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        setTitle(R.string.title_new_task)

        val task: Task = Task()
        task.name = "Do something"

    }
}
