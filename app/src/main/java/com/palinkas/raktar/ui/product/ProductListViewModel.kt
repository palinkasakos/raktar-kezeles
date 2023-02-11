package com.palinkas.raktar.ui.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.db.repositories.ProductRepository
import com.palinkas.raktar.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.switchMap
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel() {
    private val _filter = MutableLiveData("")

    val list by lazy { _filter.switchMap { productRepository.getAllPaged(it).cachedIn(viewModelScope)}}

    fun setFilter(text: String){
        _filter.value = text
    }
}