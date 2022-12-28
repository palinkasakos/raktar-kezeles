package com.palinkas.raktar.ui.product

import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.db.repositories.ProductRepository
import com.palinkas.raktar.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel() {
    fun newProduct() {

    }

    var list = productRepository.getALl()

}