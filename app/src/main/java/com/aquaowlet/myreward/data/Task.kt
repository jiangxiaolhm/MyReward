/*
 * Created by Eric Hongming Lin on 20/05/18 3:05 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 2:40 AM
 */

package com.aquaowlet.myreward.data

import android.arch.persistence.room.*
import java.util.*

/**
 * Data model for a task.
 */
@Entity(tableName = "tasks")
@ForeignKey(
        entity = Task::class,
        parentColumns = arrayOf("id"),      // Parent entity
        childColumns = arrayOf("parent_id") // Current entity
)
data class Task(
        @ColumnInfo(name = "name") var name: String = "",
        @ColumnInfo(name = "description") var description: String = "",
        @ColumnInfo(name = "type") var type: Int = TYPE_TASK,
        @ColumnInfo(name = "archived") var archived: Boolean = false,
        @ColumnInfo(name = "status") var status: Int = STATUS_TODO,
        @ColumnInfo(name = "start_at") var startAt: Date? = null,
        @ColumnInfo(name = "due_at") var dueAt: Date? = null,
        @ColumnInfo(name = "repeatable") var repeatable: Boolean = false,
        @ColumnInfo(name = "period") var period: Long = 0,
        @ColumnInfo(name = "priority") var priority: Int = 0,
        @ColumnInfo(name = "parent_id") var parentId: String = "",
//        @Embedded var parent: Task? = null,
//        @ColumnInfo(name = "children") @Embedded var children: List<Task> = arrayListOf(),
//        @ColumnInfo(name = "parent") var prerequisiteId: String = "",
        @ColumnInfo(name = "reward") var reward: String = "",
        @ColumnInfo(name = "punishment") var punishment: String = ""
) {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = UUID.randomUUID().toString()

    @Ignore
    var isTreeNode: Boolean = false

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