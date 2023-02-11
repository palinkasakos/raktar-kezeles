package com.palinkas.raktar.ui.company

import com.palinkas.raktar.db.dao.OwnCompanyDao
import com.palinkas.raktar.db.entities.OwnCompany
import com.palinkas.raktar.db.repositories.OwnCompanyRepository
import com.palinkas.raktar.db.repositories.StorageRepository
import com.palinkas.raktar.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val ownCompanyRepository: OwnCompanyRepository
) : BaseViewModel() {

    val storageList by lazy { storageRepository.getALl() }

    private val _ownCompany = MutableStateFlow<OwnCompany?>(null)
    val ownCompany = _ownCompany.asStateFlow()

    fun queryOwnCompany(){
        _ownCompany.value = ownCompanyRepository.getOne()
        if (_ownCompany.value == null)
            _ownCompany.value = OwnCompany()
    }
}