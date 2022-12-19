package com.palinkas.raktar.db.repositories

import com.palinkas.raktar.db.dao.ProductDao
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
}