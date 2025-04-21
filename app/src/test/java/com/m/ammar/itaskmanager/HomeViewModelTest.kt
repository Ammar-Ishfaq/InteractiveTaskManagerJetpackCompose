package com.m.ammar.itaskmanager

import com.m.ammar.itaskmanager.data.local.model.Priority
import com.m.ammar.itaskmanager.data.local.model.Task
import com.m.ammar.itaskmanager.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    // 1. Create test dispatcher and scope
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: HomeViewModel
    private lateinit var fakeDao: FakeTaskDao

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeTaskDao()
        viewModel = HomeViewModel(fakeDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addTask should add task to list`() = testScope.runTest {
        val task = createTestTask()

        viewModel.addTask(task)
        advanceUntilIdle()

        // Verify in both ViewModel flow and FakeDao
        val daoTasks = fakeDao.getAllTasksFlow().first()

        assertTrue("DAO should contain task", daoTasks.any { it.title == task.title })
    }

    @Test
    fun `deleteTask should remove task`() = testScope.runTest {
        val task = createTestTask()
        viewModel.addTask(task)
        advanceUntilIdle()

        viewModel.deleteTask(task)
        advanceUntilIdle()

        val tasks = fakeDao.getAllTasksFlow().first()

        assertFalse(tasks.contains(task))
    }


    @Test
    fun `deleteTaskWithUndo should keep task until snackbar dismissed`() =
        testScope.runTest {
            val task = createTestTask()
            viewModel.addTask(task)
            advanceUntilIdle()

            // Test undo scenario
            viewModel.deleteTask(task)
            advanceUntilIdle()

            // Verify task was removed
           var tasks = fakeDao.getAllTasksFlow().first()
            assertFalse(tasks.contains(task))

            // Simulate undo action
            viewModel.undoDelete()
            advanceUntilIdle()

            // Verify task was restored
            tasks = fakeDao.getTasksSortedAlphabetically()
            assertTrue(tasks.any { it.title == task.title })
        }

    private fun createTestTask(
        title: String = "Test",
        dueDate: Long = System.currentTimeMillis(),
        priority: Priority = Priority.MEDIUM,
        isCompleted: Boolean = false
    ): Task {
        return Task(
            title = title,
            dueDate = dueDate,
            priority = priority,
            id = 0,
            description = "task_desc",
            isCompleted = isCompleted
        )
    }

    // More test coming soon

}