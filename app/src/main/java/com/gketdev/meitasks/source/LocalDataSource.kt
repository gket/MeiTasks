package com.gketdev.meitasks.source

import com.gketdev.meitasks.data.TaskEntity
import com.gketdev.meitasks.database.TaskDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: TaskDao) {

    suspend fun searchTask(query: String, status: Int?): List<TaskEntity> {
        return if (status != null) {
            dao.getSearchResults("%$query%", status)
        } else {
            dao.getSearchResults("%$query%")
        }
    }

    fun saveTasks(mappedTasks: List<TaskEntity>) {
        dao.insertTasks(mappedTasks)
    }

}