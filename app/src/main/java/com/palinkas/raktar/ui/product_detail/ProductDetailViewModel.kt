package com.palinkas.raktar.ui.product_detail

import androidx.lifecycle.LiveData
import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.db.repositories.ProductRepository
import com.palinkas.raktar.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel() {
    private val _product: MutableStateFlow<Product?> = MutableStateFlow(null)
    val product = _product.asStateFlow()

    fun setProduct(product: Product) {
        _product.value = product
    }

    fun queryProduct(id: Int): LiveData<Product> {
        return productRepository.get(id)
    }

    fun insertOrUpdateProduct() {
        product.value?.let {
            launchInIo({
                productRepository.insertOrUpdateProduct(it)
                navigateBack()
            }, true)
        }
    }
}