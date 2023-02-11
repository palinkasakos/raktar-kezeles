package com.palinkas.raktar.db.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.palinkas.raktar.db.dao.ProductDao
import com.palinkas.raktar.db.dao.StorageDao
import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.db.entities.Storage
import com.palinkas.raktar.utils.Constants
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storageDao: StorageDao
) {

    fun getALl() = storageDao.getAll()

    fun update(storage: Storage) = storageDao.update(storage)

    fun insertOrUpdateStorage(storage: Storage) = storageDao.insertOrUpdate(storage)

    fun insertStorages(storages: List<Storage>) = storageDao.insertAll(storages)
}