package com.m.ammar.itaskmanager.data.dihilt

import android.app.Application
import android.content.Context
import com.m.ammar.itaskmanager.data.local.data_store.SettingsRepository
import com.m.ammar.itaskmanager.data.managers.DataManager
import com.m.ammar.itaskmanager.data.managers.DataManagerImpl
import com.m.ammar.itaskmanager.utility.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides app-level dependencies.
 */
@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    /**
     * Provides a singleton instance of [DataManager] using [DataManagerImpl].
     */
    @Singleton
    @Provides
    fun provideDataManager(dataManagerImpl: DataManagerImpl): DataManager {
        return dataManagerImpl
    }

    /**
     * Provides a [ResourceProvider] for accessing app resources.
     */
    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider {
        return ResourceProvider(context.applicationContext)
    }

    /**
     * Provides the application [Context] from the [Application] instance.
     */
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }


    /**
     * Provides a singleton instance of [SettingsRepository] for accessing app settings.
     */
    @Singleton
    @Provides
    fun provideSettingsRepo(context: Context): SettingsRepository {
        return SettingsRepository(context)
    }


}