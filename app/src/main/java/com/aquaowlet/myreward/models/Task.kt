package com.aquaowlet.myreward.models

import java.util.*

/**
 * Data model for a task.
 */
data class Task(
        var name: String,
        var description: String,
        var type: Int,
        var status: Int,
        var startAt: Date,
        var dueAt: Date,
        var priority: Int,
        var parent: Task,
        var children: ArrayList<Task>,
        var prerequisite: Task,
        var reward: String,
        var punishment: String,
        var archived: Boolean
) {
    companion object {
        val TYPE_TASK = 0
        val TYPE_REWARD = 1
        val TYPE_PUNISHMENT = 2
        val STATUS_TODO = 0
        val STATUS_COMPLETE = 1
        val STATUS_OVERDUE = 2
    }
}