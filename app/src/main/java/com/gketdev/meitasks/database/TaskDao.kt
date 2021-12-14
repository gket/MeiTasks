package com.gketdev.meitasks.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gketdev.meitasks.data.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(mappedTasks: List<TaskEntity>)

    @Query("SELECT * FROM tasks where taskName like :searchQuery OR projectName like :searchQuery")
    suspend fun getSearchResults(searchQuery: String): List<TaskEntity>

    @Query("SELECT * FROM tasks where (taskName like :searchQuery OR projectName like :searchQuery) AND status = :status")
    suspend fun getSearchResults(searchQuery: String, status: Int): List<TaskEntity>
}
