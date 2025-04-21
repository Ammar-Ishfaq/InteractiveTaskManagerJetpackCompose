package com.m.ammar.itaskmanager.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.m.ammar.itaskmanager.data.local.dao.TaskDao
import com.m.ammar.itaskmanager.data.local.model.Task

/**
 * AppDatabase is the Room database class that provides access to the TaskDao
 * for performing database operations related to tasks.
 *
 * @Database annotation marks this class as a Room database, specifying the
 * entities and version.
 */
@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides the TaskDao for performing CRUD operations on tasks.
     *
     * @return TaskDao instance.
     */
    abstract fun taskDao(): TaskDao

    companion object {
        // Singleton instance of AppDatabase
        private var INSTANCE: AppDatabase? = null

        /**
         * Gets the singleton instance of AppDatabase.
         *
         * If an instance does not exist, it is created and returned.
         *
         * @param context The application context used to initialize the database.
         * @return The AppDatabase instance.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
