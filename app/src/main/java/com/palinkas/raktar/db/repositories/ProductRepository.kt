package com.palinkas.raktar.db.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.palinkas.raktar.db.dao.ProductDao
import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.utils.Constants
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
    fun get(id: Int) = productDao.get(id)

    fun getALl() = productDao.getAll()

    fun getAllPaged(): LiveData<PagingData<Product>> {
        val pagingSourceFactory = { productDao.getAllPaged() }

        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE), null, pagingSourceFactory
        ).liveData
    }

    fun updateProduct(product: Product) = productDao.update(product)

    fun insertOrUpdateProduct(product: Product) = productDao.insertOrUpdate(product)

    fun insertProducts(products: List<Product>) = productDao.insertAll(products)

    fun insertProduct(product: Product) = productDao.insertOrReplace(product)
}