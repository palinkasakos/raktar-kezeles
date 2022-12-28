package com.palinkas.raktar.db.repositories

import com.palinkas.raktar.db.dao.ProductDao
import com.palinkas.raktar.db.entities.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
    fun get(id: Int) = productDao.get(id)

    fun getALl() = productDao.getAll()

    fun updateProduct(product: Product) = productDao.update(product)

    fun insertOrUpdateProduct(product: Product) = productDao.insertOrUpdate(product)

    fun insertProducts(products: List<Product>) = productDao.insertAll(products)

    fun insertProduct(product: Product) = productDao.insertOrReplace(product)
}