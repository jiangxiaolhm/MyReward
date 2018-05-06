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
        var startAt: Date?,
        var dueAt: Date?,
        var priority: Int,
        var parent: Task?,
        var children: List<Task>,
        var prerequisite: Task?,
        var reward: String,
        var punishment: String,
        var archived: Boolean
) {

    /**
     * Empty constructor
     */
    constructor() : this(
            "",
            "",
            TYPE_TASK,
            STATUS_TODO,
            null,
            null,
            0,
            null,
            emptyList<Task>(),
            null,
            "",
            "",
            false)

    companion object {
        /**
         * Task types.
         */
        const val TYPE_TASK = 0
        const val TYPE_REWARD = 1
        const val TYPE_PUNISHMENT = 2

        /**
         * Task statuses.
         */
        const val STATUS_TODO = 0
        const val STATUS_COMPLETE = 1
        const val STATUS_OVERDUE = 2
    }
}