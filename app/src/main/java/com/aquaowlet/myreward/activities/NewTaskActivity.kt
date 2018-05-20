/*
 * Created by Eric Hongming Lin on 20/05/18 3:05 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 2:03 AM
 */

package com.aquaowlet.myreward.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.data.Task
import java.text.SimpleDateFormat
import java.util.*

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

        val startAtEditText: EditText = findViewById(R.id.new_task_start_at)

        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                val myFormat = "dd MMM yyyy h:mm" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
                startAtEditText.setText(sdf.format(cal.time))
            }

            TimePickerDialog(
                    this@NewTaskActivity,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false).show()
        }

        startAtEditText.setOnClickListener {
            DatePickerDialog(
                    this@NewTaskActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

    }

}
