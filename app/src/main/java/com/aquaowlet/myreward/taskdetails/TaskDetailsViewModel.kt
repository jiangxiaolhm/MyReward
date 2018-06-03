package com.aquaowlet.myreward.taskdetails

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.data.local.TasksRepository
import com.aquaowlet.myreward.util.Util

/**
 * Get the task data to be shown in the task details screen.
 */
class TaskDetailsViewModel(context: Application) : AndroidViewModel(context) {

    private val tasksRepository = TasksRepository.getInstance(context)

    var name = ObservableField<String>("HHH")
    var reward = ObservableField<String>()
    var punishment = ObservableField<String>()
    var startAt = ObservableField<String>()
    var dueAt = ObservableField<String>()
    var repeatable = ObservableField<String>()
    var period = ObservableField<String>()
    var periodCustomDays = ObservableField<String>()
    var description = ObservableField<String>()

    fun setupTaskDetails(task: Task) {
        name.set(task.name)
        reward.set(task.reward)
        punishment.set(task.punishment)
        if (task.startAt != null) {
            startAt.set(Util.simpleDateFormat.format(task.startAt))
        }
        if (task.dueAt != null) {
            dueAt.set(Util.simpleDateFormat.format(task.dueAt))
        }
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
                    period.set(getApplication<Application>().getString(R.string.task_period_custom_days))
                    periodCustomDays.set(task.period.toString())
                }
            }

        } else {
            repeatable.set(getApplication<Application>().getString(R.string.no))
        }

    }

}