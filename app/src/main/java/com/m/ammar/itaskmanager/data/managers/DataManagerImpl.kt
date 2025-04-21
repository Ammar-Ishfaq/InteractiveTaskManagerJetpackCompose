package com.m.ammar.itaskmanager.data.managers

import com.m.ammar.itaskmanager.utility.ResourceProvider
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
) : DataManager {

    /**
     * Provides an instance of [ResourceProvider] for accessing resources.
     *
     * @return A [ResourceProvider] instance for managing application resources.
     */
    override fun getResourceManager(): ResourceProvider {
        return resourceProvider
    }
}
