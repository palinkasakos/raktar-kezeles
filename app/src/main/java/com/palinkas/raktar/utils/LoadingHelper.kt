package com.palinkas.raktar.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadingHelper @Inject constructor() {
    private val _showLoading = MutableSharedFlow <Boolean>()
    val loadingFlow = _showLoading.asSharedFlow()

    suspend fun showLoading() {
        _showLoading.emit(true)
    }

    suspend fun hideLoading() {
        _showLoading.emit(false)
    }
}