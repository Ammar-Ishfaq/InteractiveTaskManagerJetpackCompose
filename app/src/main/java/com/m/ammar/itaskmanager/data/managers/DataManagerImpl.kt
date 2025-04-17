package com.m.ammar.itaskmanager.data.managers

import com.m.ammar.itaskmanager.utility.ResourceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManagerImpl @Inject constructor(
    private val resourceProvider: ResourceProvider,
) : DataManager {

    override fun getResourceManager(): ResourceProvider {
        return resourceProvider
    }

}