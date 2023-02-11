package com.palinkas.raktar.ui.company

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.palinkas.raktar.db.entities.Storage
import com.palinkas.raktar.db.repositories.ProductRepository
import com.palinkas.raktar.db.repositories.StorageRepository
import com.palinkas.raktar.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : BaseViewModel() {
    private val _storage: MutableStateFlow<Storage?> = MutableStateFlow(null)
    val storage = _storage.asStateFlow()

    fun setStorage(storage: Storage)
    {
        _storage.value = storage
    }

    fun insertOrUpdateStorage() {
        storage.value?.let {
            launchInIo({
                storageRepository.insertOrUpdateStorage(it)
                navigateBack()
            }, true)
        }
    }
}