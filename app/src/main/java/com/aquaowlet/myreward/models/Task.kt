package com.aquaowlet.myreward.models

import java.util.*

/**
 * Data model for a task.
 */
data class Task(
        var name: String,
        var description: String,
        var type: Int,
        var archived: Boolean,
        var status: Int,
        var startAt: Date?,
        var dueAt: Date?,
        var repeatable: Boolean,
        var period: Long,
        var priority: Int,
        var parent: Task?,
        var children: List<Task>,
        var prerequisite: Task?,
        var reward: String,
        var punishment: String

) {

    /**
     * Empty default constructor.
     */
    constructor() : this(
            "",
            "",
            TYPE_TASK,
            false,
            STATUS_TODO,
            null,
            null,
            false,
            0,
            0,
            null,
            emptyList<Task>(),
            null,
            "",
            "")

    /**
     * Constructor with task name and due date only.
     */
    constructor(name: String, dueAt: Date) :this(
            name,
            "",
            TYPE_TASK,
            false,
            STATUS_TODO,
            null,
            dueAt,
            false,
            0,
            0,
            null,
            emptyList<Task>(),
            null,
            "",
            ""
    )

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