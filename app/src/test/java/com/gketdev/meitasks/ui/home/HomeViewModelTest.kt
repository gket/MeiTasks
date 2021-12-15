package com.gketdev.meitasks.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gketdev.meitasks.data.DataResultState
import com.gketdev.meitasks.data.TaskEntity
import com.gketdev.meitasks.repository.HomeRepository
import com.gketdev.meitasks.utils.MainCoroutineRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainRule = MainCoroutineRule()

    lateinit var SUT: HomeViewModel

    lateinit var repository: HomeRepository

    private val item = TaskEntity(1, "KC-240", 1, "Project X")

    private val itemList = listOf(item, item.copy(taskId = 2, taskName = "KC-241"))

    @Before
    fun setUp() {
        repository = mockk<HomeRepository>(relaxed = true)
        SUT = HomeViewModel(repository)
    }


    @Test
    fun `when query get error homeviewstate should be error`() = runBlocking {

        every { repository.searchQuery(anyString(), anyInt()) } returns flow {
            emit(DataResultState.Error<List<TaskEntity>>(null, null, "Hello"))
        }

        val observer = mockk<Observer<HomeViewState>> { every { onChanged(any()) } answers {} }

        SUT.viewState.observeForever(observer)

        SUT.searchQuery(anyString(), anyInt())

        verifySequence {
            observer.onChanged(HomeViewState.InitialLoading)
            observer.onChanged(HomeViewState.RequestLoading)
            observer.onChanged(HomeViewState.Error("Hello"))
        }
    }

    @Test
    fun `when query get success homeviewstate should be tasks`() = runBlocking {

        every { repository.searchQuery(anyString(), anyInt()) } returns flow {
            emit(
                DataResultState.Success<List<TaskEntity>>(
                    itemList
                )
            )
        }

        val observer = mockk<Observer<HomeViewState>> { every { onChanged(any()) } answers {} }

        SUT.viewState.observeForever(observer)

        SUT.searchQuery(anyString(), anyInt())

        verifySequence {
            observer.onChanged(HomeViewState.InitialLoading)
            observer.onChanged(HomeViewState.RequestLoading)
            observer.onChanged(HomeViewState.Tasks(itemList))
        }
    }

}