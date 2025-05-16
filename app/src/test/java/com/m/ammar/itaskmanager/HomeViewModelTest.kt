package com.m.ammar.itaskmanager

import com.m.ammar.itaskmanager.data.local.dao.TaskDao
import com.m.ammar.itaskmanager.data.providers.SettingsProvider
import com.m.ammar.itaskmanager.data.local.model.Priority
import com.m.ammar.itaskmanager.data.local.model.Task
import com.m.ammar.itaskmanager.data.managers.DataManagerImpl
import com.m.ammar.itaskmanager.data.providers.ApiProvider
import com.m.ammar.itaskmanager.presentation.home.HomeViewModel
import com.m.ammar.itaskmanager.data.providers.ResourceProvider
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
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var fakeDao: TaskDao
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var appService: ApiProvider
    private lateinit var settingsProvider: SettingsProvider

    private lateinit var repo: DataManagerImpl
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Create mocks or fakes for dependencies
        fakeDao = FakeTaskDao()
        resourceProvider = mock()
        appService = mock()
        settingsProvider = mock()

        repo = DataManagerImpl(
            resourceProvider = resourceProvider,
            appService = appService,
            settingsProvider = settingsProvider,
            taskDao = fakeDao
        )

        viewModel = HomeViewModel(repo)
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