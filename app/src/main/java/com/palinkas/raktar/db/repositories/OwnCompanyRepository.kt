package com.palinkas.raktar.db.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.palinkas.raktar.db.dao.OwnCompanyDao
import com.palinkas.raktar.db.dao.ProductDao
import com.palinkas.raktar.db.dao.StorageDao
import com.palinkas.raktar.db.entities.OwnCompany
import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.db.entities.Storage
import com.palinkas.raktar.utils.Constants
import javax.inject.Inject

class OwnCompanyRepository @Inject constructor(
    private val ownCompanyDao: OwnCompanyDao
) {

    fun getOne() = ownCompanyDao.getOne()

    fun getALl() = ownCompanyDao.getAll()

    fun update(ownCompany: OwnCompany) = ownCompanyDao.update(ownCompany)

    fun insertOrUpdateOwnCompany(ownCompany: OwnCompany) = ownCompanyDao.insertOrUpdate(ownCompany)
}