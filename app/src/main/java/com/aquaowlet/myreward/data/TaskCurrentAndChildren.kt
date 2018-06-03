/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 21/05/18 4:29 PM
 */

package com.aquaowlet.myreward.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

/**
 * A task could have 0 to many child tasks.
 */
class TaskCurrentAndChildren(
        @Embedded
        var current: Task = Task(),
        @Relation(parentColumn = "id", entityColumn = "parent_id", entity = Task::class)
        var children: List<Task> = listOf()
)