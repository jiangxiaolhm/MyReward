/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 27/05/18 9:46 PM
 */

package com.aquaowlet.myreward.data

import android.arch.persistence.room.*
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Data model for a task.
 */
@Entity(tableName = "tasks")
@ForeignKey(
        entity = Task::class,
        parentColumns = ["id"],      // Parent entity
        childColumns = ["parent_id"] // Current entity
)
data class Task(
        @ColumnInfo(name = "name") var name: String = "",
        @ColumnInfo(name = "reward") var reward: String = "",
        @ColumnInfo(name = "punishment") var punishment: String = "",
        @ColumnInfo(name = "start_at") var startAt: Date? = null,
        @ColumnInfo(name = "due_at") var dueAt: Date? = null,
        @ColumnInfo(name = "repeatable") var repeatable: Boolean = false,
        @ColumnInfo(name = "period") var period: Int = 0,
        @ColumnInfo(name = "description") var description: String = "",
        @ColumnInfo(name = "type") var type: Int = TYPE_TASK,
        @ColumnInfo(name = "status") var status: Int = STATUS_TODO,
        @ColumnInfo(name = "expanded") var expanded: Boolean = false,
        @ColumnInfo(name = "archived") var archived: Boolean = false
) : Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = UUID.randomUUID().toString()
    @Ignore
    val children: ArrayList<Task> = arrayListOf()
    @Ignore
    var parent: Task? = null
    @ColumnInfo(name = "parent_id")
    var parentId: String = ""
    @ColumnInfo(name = "index_in_parent")
    var indexInParent: Int = Int.MAX_VALUE

    @Ignore
    constructor(name: String, reward: String, punishment: String, startAt: Date?, dueAt: Date?, repeatable: Boolean, period: Int, description: String) : this() {
        this.name = name
        this.reward = reward
        this.punishment = punishment
        this.startAt = startAt
        this.dueAt = dueAt
        this.repeatable = repeatable
        this.period = period
        this.description = description
    }

    /**
     * Add a child task to current task.
     */
    fun addChild(child: Task) {
        this.children.add(child)
        child.parent = this
        child.parentId = this.id
        child.indexInParent = this.children.size - 1
    }

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

        /**
         * Repeatable task periods.
         */
        const val NOT_REPEATABLE = 0
        const val EVERY_DAY = -1
        const val EVERY_WEEK = -2
        const val EVERY_MONTH = -3
        const val EVERY_YEAR = -4
    }
}