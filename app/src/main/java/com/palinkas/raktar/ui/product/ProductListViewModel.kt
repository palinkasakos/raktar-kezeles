package com.palinkas.raktar.ui.product

import androidx.lifecycle.ViewModel
import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.db.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    fun insertProduct() {
        productRepository.insertProduct(
            Product(
                UUID.randomUUID().toString(),
                "CK001",
                "teszt termék",
                "kg",
                "",
                "1".toBigDecimal(),
                "teszt kód"
            )
        )
    }

    var list = productRepository.getALl()

}