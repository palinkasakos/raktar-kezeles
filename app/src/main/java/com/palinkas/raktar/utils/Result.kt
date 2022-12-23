package com.palinkas.raktar.utils

import com.palinkas.raktar.BuildConfig
import timber.log.Timber

sealed class Result<out T : Any?> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class ApiErrorMsg(val msg: String?) : Result<Nothing>()

    fun errorString(
        unknownErrorString: String? = null,
        showDetails: Boolean = BuildConfig.DEBUG
    ): String {

        return when (this) {
            is Error -> {
                Timber.e(this.exception)

                if (!showDetails)
                    unknownErrorString ?: "Sikertelen kérés"
                else
                    this.exception.message.toString()
            }
            is ApiErrorMsg -> {
                this.msg ?: unknownErrorString ?: "Sikertelen kérés"
            }
            else ->
                unknownErrorString ?: ""

        }
    }
}

sealed class NullableResult<out T : Any?> {
    data class Success<out T : Any?>(val data: T) : NullableResult<T>()
    data class Error(val exception: Exception) : NullableResult<Nothing>()
    data class ApiErrorMsg(val msg: String?) : NullableResult<Nothing>()

    fun errorString(
        unknownErrorString: String? = null,
        showDetails: Boolean = BuildConfig.DEBUG
    ): String {

        return when (this) {
            is Error -> {
                Timber.e(this.exception)

                if (!showDetails)
                    unknownErrorString ?: "Sikertelen kérés"
                else
                    this.exception.message.toString()
            }
            is ApiErrorMsg -> {
                this.msg ?: unknownErrorString ?: "Sikertelen kérés"
            }
            else ->
                unknownErrorString ?: ""

        }
    }
}