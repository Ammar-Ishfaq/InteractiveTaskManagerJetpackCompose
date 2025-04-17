package com.m.ammar.itaskmanager.data.dihilt

import android.app.Application
import android.content.Context
import com.m.ammar.itaskmanager.data.managers.DataManager
import com.m.ammar.itaskmanager.data.managers.DataManagerImpl
import com.m.ammar.itaskmanager.utility.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDataManager(dataManagerImpl: DataManagerImpl): DataManager {
        return dataManagerImpl
    }

    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider {
        return ResourceProvider(context.applicationContext)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }



}