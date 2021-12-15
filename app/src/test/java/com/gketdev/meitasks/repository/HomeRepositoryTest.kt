package com.gketdev.meitasks.repository

import app.cash.turbine.test
import com.gketdev.meitasks.data.DataResultState
import com.gketdev.meitasks.data.Task
import com.gketdev.meitasks.data.TaskDetail
import com.gketdev.meitasks.data.TaskEntity
import com.gketdev.meitasks.source.LocalDataSource
import com.gketdev.meitasks.source.RemoteDataSource
import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import kotlin.time.ExperimentalTime

@ExperimentalTime
class HomeRepositoryTest {

    lateinit var SUT: HomeRepository

    private val item = TaskEntity(1, "KC-240", 1, "Project X")
    private val itemTask = Task(2, "KC-240", 1, 1)

    private val emptyTaskDetail = TaskDetail(emptyList(), emptyList(), emptyList())

    lateinit var remoteDataSource: RemoteDataSource

    lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        remoteDataSource = mockk(relaxed = true)
        localDataSource = mockk(relaxed = true)
        SUT = HomeRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun `when response null should return error state`() = runBlocking {
        every {
            remoteDataSource.searchQuery(anyString(), anyInt())
        } returns flow {
            emit(DataResultState.Error<TaskDetail>())
        }
        val tasks = SUT.searchQuery(
            anyString(),
            anyInt()
        )

        tasks.test {
            val awaitItem = awaitItem()
            assert(awaitItem is DataResultState.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when state offline should call local search`() = runBlocking {
        every {
            remoteDataSource.searchQuery(anyString(), anyInt())
        } returns flow {
            emit(DataResultState.Offline<TaskDetail>())
        }

        val tasks = SUT.searchQuery(
            anyString(),
            anyInt()
        )

        tasks.test {
            val awaitedItem = awaitItem()
            assert(awaitedItem is DataResultState.Success)
            coVerify { localDataSource.searchTask(anyString(), anyInt()) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when list empty should return success state with empty list `() = runBlocking {
        every {
            remoteDataSource.searchQuery(anyString(), anyInt())
        } returns flow {
            emit(DataResultState.Success(emptyTaskDetail))
        }

        val tasks = SUT.searchQuery(
            anyString(),
            anyInt()
        )

        tasks.test {
            val awaitedItem = awaitItem()
            assert((awaitedItem as DataResultState.Success).data.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when tasks filled should return success state with filled list `() = runBlocking {
        every {
            remoteDataSource.searchQuery(anyString(), anyInt())
        } returns flow {
            emit(
                DataResultState.Success(
                    TaskDetail(
                        listOf(),
                        listOf(itemTask, itemTask.copy(taskId = 2)),
                        listOf()
                    )
                )
            )
        }


        val tasks = SUT.searchQuery(
            anyString(),
            anyInt()
        )

        tasks.test {
            val awaitItem = awaitItem()
            assert((awaitItem as DataResultState.Success).data.isNotEmpty())
            assert(awaitItem.data.size == 2)
            coVerify {
                localDataSource.saveTasks(awaitItem.data)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }


}