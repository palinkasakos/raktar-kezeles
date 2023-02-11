package com.palinkas.raktar.db.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.palinkas.raktar.db.dao.ProductDao
import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.utils.Constants
import com.palinkas.raktar.utils.toSqlLikeQuery
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
    fun get(oid: String) = productDao.get(oid)

    fun getALl() = productDao.getAll()

    fun getAllPaged(filter: String): LiveData<PagingData<Product>> {
        val sb = StringBuilder()
        val params = arrayListOf<Any>()

        sb.append("SELECT * FROM ${Product.TABLE_NAME} ")

        if (filter.isNotEmpty()){
            sb.append("WHERE name like ?")
            params.add(filter.toSqlLikeQuery())
        }

        val simpleSQLiteQuery = SimpleSQLiteQuery(sb.toString(), params.toTypedArray())

        val pagingSourceFactory = { productDao.getAllPaged(simpleSQLiteQuery) }

        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE), null, pagingSourceFactory
        ).liveData
    }

    fun updateProduct(product: Product) = productDao.update(product)

    fun insertOrUpdateProduct(product: Product) = productDao.insertOrUpdate(product)

    fun insertProducts(products: List<Product>) = productDao.insertAll(products)

    fun insertProduct(product: Product) = productDao.insertOrReplace(product)
}