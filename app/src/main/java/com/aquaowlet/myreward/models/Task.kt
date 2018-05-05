package com.aquaowlet.myreward.models

import java.util.*

/**
 * Data model for a task.
 */
data class Task(
        private var name: String,
        private var description: String,
        private var type: Int,
        private var status: Int,
        private var startAt: Date,
        private var dueAt: Date,
        private var priority: Int,
        private var parent: Task,
        private var children: ArrayList<Task>,
        private var prerequisite: Task,
        private var reward: String,
        private var punishment: String,
        private var archived: Boolean
) {
    companion object {
        const val TYPE_TASK = 0
        const val TYPE_REWARD = 1
        const val TYPE_PUNISHMENT = 2
        const val STATUS_TODO = 0
        const val STATUS_COMPLETE = 1
        const val STATUS_OVERDUE = 2
    }
}