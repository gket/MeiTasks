package com.gketdev.meitasks.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gketdev.meitasks.data.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}