package com.m.ammar.itaskmanager.data.remote

import com.m.ammar.itaskmanager.BaseRepository
import com.m.ammar.itaskmanager.data.managers.DataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepo @Inject constructor(dataManager: DataManager) : BaseRepository(dataManager) {
    private val dispatcher = Dispatchers.IO
    suspend fun getList() = withContext(dispatcher) {
        safeApiCall { Result.success(dataManager.getApiProvider().getRandomQuote()) }
    }
}