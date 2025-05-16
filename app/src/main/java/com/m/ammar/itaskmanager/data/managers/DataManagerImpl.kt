package com.m.ammar.itaskmanager.data.managers

import com.m.ammar.itaskmanager.data.local.dao.TaskDao
import com.m.ammar.itaskmanager.data.providers.SettingsProvider
import com.m.ammar.itaskmanager.data.providers.ApiProvider
import com.m.ammar.itaskmanager.data.providers.ResourceProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the [DataManager] interface responsible for managing
 * and providing access to application resources.
 *
 * This class is annotated with [Singleton] to ensure a single instance is used
 * throughout the application.
 *
 * @param resourceProvider An instance of [ResourceProvider] used for accessing resources.
 */
@Singleton
class DataManagerImpl @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val appService: ApiProvider,
    private val settingsProvider: SettingsProvider,
    private val taskDao: TaskDao,
) : DataManager {

    /**
     * Provides an instance of [ResourceProvider] for accessing resources.
     *
     * @return A [ResourceProvider] instance for managing application resources.
     */
    override fun getResourceManager(): ResourceProvider {
        return resourceProvider
    }

    /**
     * Provides an instance of [ApiProvider] for accessing APIs .
     *
     * @return A [ApiProvider] instance for API Request.
     */
    override fun getApiProvider(): ApiProvider {
        return appService
    }

    /**
     * Provides an instance of [SettingsProvider] for accessing settings .
     *
     * @return A [SettingsProvider] instance for settings.
     */
    override fun getSettingsProvider(): SettingsProvider {
        return settingsProvider
    }

    override fun getTaskDaoProvider(): TaskDao {
        return taskDao
    }
}
