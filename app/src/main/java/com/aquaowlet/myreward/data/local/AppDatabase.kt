/*
 * Created by Eric Hongming Lin on 20/05/18 4:58 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 20/05/18 4:13 AM
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


@Database(entities = arrayOf(Task::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
    abstract fun taskCurrentAndChildren(): TaskCurrentAndChildrenDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val roomDatabaseCallback = object : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                PopulateDbAsync(INSTANCE!!).execute()
            }
        }

        private class PopulateDbAsync internal constructor(db: AppDatabase) : AsyncTask<Void, Void, Void>() {

            private val dao: TasksDao = db.tasksDao()
            /**
             * Mock data
             */
            override fun doInBackground(vararg params: Void): Void? {
                dao.deleteTasks()

                val parent1 = Task("parent 1")
                val parent2 = Task("parent 2")
                val child1 = Task("child 1")
                val child2 = Task("child 2")
                val child3 = Task("child 3")
                val child4 = Task("child 4")

                parent1.indexInParent = 1
                parent2.indexInParent = 0
                parent1.addChild(child1)
                parent1.addChild(child2)
                child2.addChild(child3)
                child2.addChild(child4)
                child3.indexInParent = 1
                child4.indexInParent = 0

                dao.insertTask(parent1)
                dao.insertTask(parent2)
                dao.insertTask(child1)
                dao.insertTask(child2)
                dao.insertTask(child3)
                dao.insertTask(child4)

                return null
            }
        }

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room
                                .inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java)
                                //.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_db")
                                .allowMainThreadQueries()
                                .addCallback(roomDatabaseCallback)
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}