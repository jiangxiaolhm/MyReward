/*
 * Created by Eric Hongming Lin on 20/05/18 10:37 PM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 10:37 PM
 */

package com.aquaowlet.myreward.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class TaskParentChildren(
        @Embedded
        var parent: Task = Task(),
        @Relation(parentColumn = "id", entityColumn = "parent_id", entity = Task::class)
        var children: List<Task> = listOf()
)