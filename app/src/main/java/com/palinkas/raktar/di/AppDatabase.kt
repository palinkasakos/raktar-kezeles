package com.palinkas.raktar.di

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.palinkas.raktar.db.DbTypeConverters
import com.palinkas.raktar.db.dao.*
import com.palinkas.raktar.db.entities.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Database(
    entities = [Product::class, Storage::class, OwnCompany::class],
    version = 1
)
@TypeConverters(DbTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun storageDao(): StorageDao

    abstract fun productDao(): ProductDao

    abstract fun ownCompanyDao(): OwnCompanyDao
}