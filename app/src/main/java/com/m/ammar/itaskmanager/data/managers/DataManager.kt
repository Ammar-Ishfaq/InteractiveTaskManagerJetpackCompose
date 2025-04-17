package com.m.ammar.itaskmanager.data.managers

import com.m.ammar.itaskmanager.utility.ResourceProvider

interface DataManager {
    fun getResourceManager(): ResourceProvider
}