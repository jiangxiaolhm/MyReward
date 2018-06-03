/*
 * Created by Eric Hongming Lin on 4/06/18 2:51 AM
 * Copyright (c) 4/06/18 2:51 AM. All right reserved
 *
 * Last modified 4/06/18 2:28 AM
 */

package com.aquaowlet.myreward.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.aquaowlet.myreward.data.Task
import android.arch.persistence.db.SupportSQLiteDatabase
import android.os.AsyncTask
import java.util.*

/**
 * Store the app data in the room database.
 */
@Database(entities = [(Task::class)], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
    abstract fun taskCurrentAndChildren(): TaskCurrentAndChildrenDao

    companion object {

        private var IS_DEBUGGING = true

        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val roomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                PopulateDbAsync(INSTANCE!!).execute()
            }
        }

        /**
         * Mock data for presentation.
         */
        private class PopulateDbAsync internal constructor(db: AppDatabase) : AsyncTask<Void, Void, Void>() {

            private val dao: TasksDao = db.tasksDao()

            override fun doInBackground(vararg params: Void): Void? {
                dao.deleteTasks()

                val parent1 = Task("parent 1", "Nintendo Switch", "", Date(), Date(), true, 20, "It is a mock task.")
                val parent2 = Task("parent 2")
                val child1 = Task("child 1")
                val child2 = Task("child 2")
                val child3 = Task("child 3")
                val child4 = Task("child 4")

                child1.status = Task.STATUS_TODO
                child2.status = Task.STATUS_COMPLETE
                child3.status = Task.STATUS_OVERDUE

                parent1.expanded = true

                parent1.indexInParent = 1
                parent2.indexInParent = 0
                parent1.addChild(child1)
                parent1.addChild(child2)
                child2.addChild(child3)
                child2.addChild(child4)
                child3.indexInParent = 1
                child4.indexInParent = 0

                dao.insert(parent1)
                dao.insert(parent2)
                dao.insert(child1)
                dao.insert(child2)
                dao.insert(child3)
                dao.insert(child4)

                return null
            }
        }

        /**
         * Get database instance with singleton pattern.
         */
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    if (INSTANCE == null) {

                        INSTANCE = if (IS_DEBUGGING) {
                            Room
                                    .inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java)
                                    .allowMainThreadQueries()
                                    .addCallback(roomDatabaseCallback)
                                    .build()
                        } else {
                            Room
                                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_db")
                                    .allowMainThreadQueries()
                                    .addCallback(roomDatabaseCallback)
                                    .build()
                        }
                    }
                }
            }
            return INSTANCE!!
        }
    }

}