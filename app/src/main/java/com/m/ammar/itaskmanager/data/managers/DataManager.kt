package com.m.ammar.itaskmanager.data.managers

import com.m.ammar.itaskmanager.data.local.dao.TaskDao
import com.m.ammar.itaskmanager.data.providers.SettingsProvider
import com.m.ammar.itaskmanager.data.providers.ApiProvider
import com.m.ammar.itaskmanager.data.providers.ResourceProvider

/**
 * Interface responsible for managing and providing access to application resources.
 */
interface DataManager {

    /**
     * Provides an instance of [ResourceProvider] for accessing resources.
     *
     * @return A [ResourceProvider] instance for managing application resources.
     */
    fun getResourceManager(): ResourceProvider

    /**
     * Provides an instance of [ApiProvider] for accessing api.
     *
     * @return A [ApiProvider] instance for api.
     */
    fun getApiProvider(): ApiProvider

    /**
     * Provides an instance of [SettingsProvider] for accessing settings.
     *
     * @return A [SettingsProvider] instance for managing application settings.
     */
    fun getSettingsProvider(): SettingsProvider

    /**
     * Provides an instance of [TaskDao] for accessing task.
     *
     * @return A [TaskDao] instance for managing application task.
     */
    fun getTaskDaoProvider(): TaskDao

}
