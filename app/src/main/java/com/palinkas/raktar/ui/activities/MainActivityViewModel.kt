package com.palinkas.raktar.ui.activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
) : ViewModel() {
    val showConfirmationDialogBeforeBackNavigation = MutableLiveData<Boolean>()
}