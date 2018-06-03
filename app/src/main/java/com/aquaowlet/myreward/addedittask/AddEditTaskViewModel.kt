/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 4/06/18 2:40 AM
 */

package com.aquaowlet.myreward.addedittask

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.data.local.TasksRepository
import com.aquaowlet.myreward.util.Helper
import java.util.*

/**
 * Exposes the data to be used in the add or edit task screen.
 */
class AddEditTaskViewModel(context: Application) : AndroidViewModel(context) {

    private val tasksRepository = TasksRepository.getInstance(context)

    var task: Task? = null
    var parent: Task? = null

    var parentName = ObservableField<String>()
    var name = ObservableField<String>()
    var reward = ObservableField<String>()
    var punishment = ObservableField<String>()
    var startAt = ObservableField<String>()
    var dueAt = ObservableField<String>()
    var repeatable = ObservableBoolean(false)
    var selectedPeriod = ObservableField<String>()
    var customPeriod = ObservableField<String>()
    var description = ObservableField<String>()

    /**
     * Setup variables to be displayed on the screen.
     */
    fun setupAddEditTask(task: Task?, parent: Task?) {
        this.task = task
        this.parent = parent
        parentName.set(parent?.name)
        name.set(task?.name)
        reward.set(task?.reward)
        punishment.set(task?.punishment)
        startAt.set(Helper.formatDateToString(task?.startAt))
        dueAt.set(Helper.formatDateToString(task?.dueAt))
        if (task != null) {
            repeatable.set(task.repeatable)
            if (task.period > 0) {
                customPeriod.set(task.period.toString())
            }
        }

    }

    /**
     * Add a new task to the database.
     */
    fun addTask() {

        val periodInt = Helper.parseStringToIntOrZero(customPeriod.get()!!)

        val newTask = Task(
                name.get()!!,
                reward.get()!!,
                punishment.get()!!,
                Helper.parseStringToDate(startAt.get()!!),
                Helper.parseStringToDate(dueAt.get()!!),
                repeatable.get(),
                periodInt,
                description.get()!!
        )

        parent?.addChild(newTask)

        tasksRepository.insert(newTask)
    }

    /**
     * Update the task.
     */
    fun editTask() {

        task!!.name = name.get()!!
        task!!.reward = reward.get()!!
        task!!.punishment = punishment.get()!!
        task!!.startAt = Helper.parseStringToDate(startAt.get()!!)
        task!!.dueAt = Helper.parseStringToDate(dueAt.get()!!)
        task!!.repeatable = repeatable.get()
        when (selectedPeriod.get()!!) {
            getApplication<Application>().getString(R.string.task_period_every_day) -> {
                task!!.period = Task.EVERY_DAY
            }
            getApplication<Application>().getString(R.string.task_period_every_week) -> {
                task!!.period = Task.EVERY_WEEK
            }
            getApplication<Application>().getString(R.string.task_period_every_month) -> {
                task!!.period = Task.EVERY_MONTH
            }
            getApplication<Application>().getString(R.string.task_period_every_year) -> {
                task!!.period = Task.EVERY_YEAR
            }
            getApplication<Application>().getString(R.string.task_period_custom_days) -> {
                task!!.period = Helper.parseStringToIntOrZero(customPeriod.get()!!)
            }
        }
        task!!.description = description.get()!!

        tasksRepository.update(task!!)
    }

    /**
     * The name is required.
     */
    fun validateTaskName(): Boolean {
        return name.get()!!.isNotEmpty()
    }

    /**
     * The start date should not be after the due date.
     */
    fun validateStartAndDueDate(startAtString: String, dueAtString: String): Boolean {

        val startDate: Date?
        val dueDate: Date?

        if (startAtString.isNotEmpty() && dueAtString.isNotEmpty()) {

            startDate = Helper.parseStringToDate(startAtString)
            dueDate = Helper.parseStringToDate(dueAtString)

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

}