package com.palinkas.raktar.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EncryptedKeyValueStore @Inject constructor(@ApplicationContext context: Context) {
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        sharedPrefName,
        mainKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getValue(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun setValue(key: String, value: String) {
        sharedPreferences.edit().apply {
            this.putString(key, value)
            apply()
        }
    }

    companion object {
        const val sharedPrefName = "PrivateSharedPref"

        const val KEY_USER_NAME = "user"
        const val KEY_PASSWORD = "password"
        const val KEY_TOKEN = "token"
    }
}