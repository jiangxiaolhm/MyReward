/*
 * Created by Eric Hongming Lin on 28/05/18 3:07 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 28/05/18 3:07 AM
 */

package com.aquaowlet.myreward.addedittask

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import android.widget.EditText
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.data.local.TasksRepository
import com.aquaowlet.myreward.util.Constant
import com.aquaowlet.myreward.util.Util
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskViewModel(context: Application) : AndroidViewModel(context) {

    private val tasksRepository = TasksRepository.getInstance(context)

    val task = Task("xxx")

    var name = ObservableField<String>("The name")
    var reward = ObservableField<String>("Reward")
    var punishment = ObservableField<String>()
    var startAt = ObservableField<String>()
    var dueAt = ObservableField<String>()
    var repeatable = ObservableBoolean(false)
    var customPeriod = ObservableField<String>()
    var description = ObservableField<String>()

    fun addTask() {

        var period = 0
        if (repeatable.get()) {
            try {
                period = customPeriod.get()!!.toInt()
            } catch (e: NumberFormatException) {
                Log.d(Constant.DEBUG, "The customPeriod can not be parsed to Int in AddEditTaskActivity.")
            }

        }

        val newTask = Task(
                name.get()!!,
                reward.get()!!,
                punishment.get()!!,
                parseStringToDate(startAt.get()!!),
                parseStringToDate(dueAt.get()!!),
                repeatable.get(),
                period,
                description.get()!!
        )

        tasksRepository.insert(newTask)
    }

    fun editTask(task: Task) {

    }

    /**
     * The name is required.
     */
    fun validateTaskName(): Boolean {
        return name.get()!!.isNotEmpty()
    }

    /**
     * The date should be null or after today.
     */
    fun validateDate(string: String): Boolean {

        val date = parseStringToDate(string)

        if (date != null && date.before(Date())) {
            return false
        }

        return true
    }

    /**
     * The start date should not be after the due date.
     */
    fun validateStartAndDueDate(startAtString: String, dueAtString: String): Boolean {

        val startDate: Date?
        val dueDate: Date?

        if (startAtString.isNotEmpty() && dueAtString.isNotEmpty()) {

            startDate = parseStringToDate(startAtString)
            dueDate = parseStringToDate(dueAtString)

            if (startDate != null && dueDate != null) {
                if (startDate.after(dueDate)) {
                    return false
                }
            } else {
                return false
            }
        }
        return true
    }

    /**
     * The custom period is required.
     */
    fun validateCustomPeriod(): Boolean {
        return customPeriod.get()!!.isNotEmpty()
    }

    /**
     * Parse string to date, catch ParseException.
     */
    private fun parseStringToDate(string: String): Date? {

        var date: Date? = null

        try {
            date = Util.simpleDateFormat.parse(string)
        } catch (e: ParseException) {
            Log.d(Constant.DEBUG, "The \"$string\" string can not be parsed in AddEditTaskActivity.")
        }

        return date
    }

}