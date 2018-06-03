/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 2/06/18 5:57 AM
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