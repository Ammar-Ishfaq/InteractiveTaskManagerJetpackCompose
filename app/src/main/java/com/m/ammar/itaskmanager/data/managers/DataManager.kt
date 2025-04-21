package com.m.ammar.itaskmanager.data.managers

import com.m.ammar.itaskmanager.utility.ResourceProvider

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
}
