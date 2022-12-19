package com.palinkas.raktar.di

import android.app.Application
import androidx.room.Room
import com.palinkas.raktar.db.AppDatabase
import com.palinkas.raktar.db.dao.ProductDao
import com.palinkas.raktar.db.dao.StorageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [DaoModule::class])
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        const val DB_NAME_WITHOUT_EXTENSION = "app"
    }
}

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    @Singleton
    fun provide_StorageDao(db: AppDatabase): StorageDao {
        return db.storageDao()
    }

    @Provides
    @Singleton
    fun provide_ProductDao(db: AppDatabase): ProductDao {
        return db.productDao()
    }
}