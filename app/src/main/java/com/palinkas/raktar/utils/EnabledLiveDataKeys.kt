package com.palinkas.raktar.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

class EnabledLiveDataByKeys(private val dependecyCount: Int, private val name: String = "") {
    private val _liveData = MutableLiveData<Boolean?>()
    val value: LiveData<Boolean?> = _liveData

    private var innerValue: Boolean? = null

    init {
        _liveData.value = null
    }

    private val map = HashMap<String, Boolean>()

    fun putValue(key: String, value: Boolean) {
        var log = "setting key: $key value: $value"
        if (!name.isBlank())
            log += "; on $name"

        Timber.d(log)
        map[key] = value
        invalidate()
    }

    private fun invalidate() {
        val valid = map.filterValues { !it }.isEmpty()

        val currentValue = value.value
        if (currentValue == null || currentValue != valid) {

            innerValue = valid
            if (map.count() == dependecyCount) {
                if (!name.isBlank()) Timber.d("$name dispatching new value: $valid")
                _liveData.value = innerValue
            }
        }
    }

    fun reset() {
        map.clear()
        _liveData.value = null
    }
}