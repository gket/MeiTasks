package com.gketdev.meitasks.repository

import com.gketdev.meitasks.data.DataResultState
import com.gketdev.meitasks.data.TaskDetail
import com.gketdev.meitasks.data.TaskEntity
import com.gketdev.meitasks.source.LocalDataSource
import com.gketdev.meitasks.source.RemoteDataSource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    fun searchQuery(query: String, status: Int?) = flow {
        remoteDataSource.searchQuery(query, status).collect {
            when (it) {
                is DataResultState.Success -> {
                    val mappedTasks = mapToTaskItem(it.data)
                    localDataSource.saveTasks(mappedTasks)
                    emit(DataResultState.Success(mappedTasks))
                }
                is DataResultState.Error -> {
                    emit(
                        DataResultState.Error<List<TaskEntity>>(
                            it.error,
                            it.code,
                            it.message
                        )
                    )
                }
                is DataResultState.Offline -> {
                    val data = localDataSource.searchTask(query, status)
                    emit(DataResultState.Success(data))
                }
            }
        }
    }

    private fun mapToTaskItem(data: TaskDetail): List<TaskEntity> {
        val taskItems = data.tasks.map { task ->
            val sectionId = task.sectionId
            val sectionInfo = data.sections.find { sectionId == it.sectionId }
            val projectInfo = data.projects.find { sectionInfo?.projectId == it.projectId }
            TaskEntity(
                taskId = task.taskId,
                taskName = task.taskName,
                status = task.status,
                projectName = projectInfo?.projectName ?: ""
            )
        }
        return taskItems
    }

}