package com.palinkas.raktar.ui.common

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palinkas.raktar.utils.BusinessException
import com.palinkas.raktar.utils.LoadingHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var loadingHelper: LoadingHelper

    private val _snackBarSharedFlow = MutableSharedFlow<Any>()
    val snackBarSharedFlow: SharedFlow<Any> = _snackBarSharedFlow.asSharedFlow()

    private val _showLoading = MutableSharedFlow<Boolean>()
    val showLoading: SharedFlow<Boolean> = _showLoading.asSharedFlow()

    private val _navigateBack = MutableSharedFlow<Boolean>()
    val navigateBack: SharedFlow<Boolean> = _navigateBack

    fun showSnackbarString(value: String) {
        viewModelScope.launch {
            _snackBarSharedFlow.emit(value)
        }
    }

    fun showSnackbarResource(@StringRes value: Int) {
        viewModelScope.launch {
            _snackBarSharedFlow.emit(value)
        }
    }

    fun showLoading() {
        viewModelScope.launch {
            //loadingHelper.showLoading()
        }
    }

    fun hideLoading() {
        viewModelScope.launch {
            //loadingHelper.hideLoading()
        }
    }

    fun navigateBack() {
        hideLoading()

        viewModelScope.launch {
            _navigateBack.emit(true)
        }
    }

    fun launchInIo(block: suspend () -> Unit, showLoading: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            if (showLoading)
                showLoading()

            block()

            if (showLoading)
                hideLoading()
        }
    }
}