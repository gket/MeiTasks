package com.gketdev.meitasks.di

import android.content.Context
import androidx.room.Room
import com.gketdev.meitasks.R
import com.gketdev.meitasks.database.TaskDao
import com.gketdev.meitasks.database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        return Room.databaseBuilder(
            appContext,
            TaskDatabase::class.java,
            appContext.getString(R.string.app_name)
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(database: TaskDatabase): TaskDao {
        return database.getTaskDao()
    }
}