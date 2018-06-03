/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 4/06/18 2:40 AM
 */

package com.aquaowlet.myreward.taskdetails

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.util.Helper

/**
 * Get the task data to be shown in the task details screen.
 */
class TaskDetailsViewModel(context: Application) : AndroidViewModel(context) {

    var task: Task? = null

    var parentName = ObservableField<String>()
    var name = ObservableField<String>()
    var reward = ObservableField<String>()
    var punishment = ObservableField<String>()
    var startAt = ObservableField<String>()
    var dueAt = ObservableField<String>()
    var repeatable = ObservableField<String>()
    var period = ObservableField<String>()
    var description = ObservableField<String>()

    /**
     * Setup the task attributes to be displayed on the screen
     */
    fun setupTaskDetails(task: Task) {
        this.task = task
        parentName.set(task.parent?.name)
        name.set(task.name)
        reward.set(task.reward)
        punishment.set(task.punishment)
        startAt.set(Helper.formatDateToString(task.startAt))
        dueAt.set(Helper.formatDateToString(task.dueAt))
        if (task.repeatable) {
            repeatable.set(getApplication<Application>().getString(R.string.yes))

            when (task.period) {
                Task.EVERY_DAY -> {
                    period.set(getApplication<Application>().getString(R.string.task_period_every_day))
                }
                Task.EVERY_WEEK -> {
                    period.set(getApplication<Application>().getString(R.string.task_period_every_week))
                }
                Task.EVERY_MONTH -> {
                    period.set(getApplication<Application>().getString(R.string.task_period_every_month))
                }
                Task.EVERY_YEAR -> {
                    period.set(getApplication<Application>().getString(R.string.task_period_every_year))
                }
                else -> {
                    period.set(task.period.toString() + getApplication<Application>().getString(R.string.task_period_days_suffix))
                }
            }

        } else {
            repeatable.set(getApplication<Application>().getString(R.string.no))
        }
        description.set(task.description)

    }

}