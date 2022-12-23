package com.palinkas.raktar.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class PrefManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
){

    var updateDownloadedButNotInstalled: Boolean
        get() = sharedPreferences.getBoolean(key_update_downloaded_but_not_installed, false)
        set(value) = sharedPreferences.edit {
            putBoolean(
                key_update_downloaded_but_not_installed,
                value
            )
        }

    var latestUpdateDate: Long
        get() = sharedPreferences.getLong(key_latest_update_date, 0L)
        set(value) = sharedPreferences.edit().putLong(key_latest_update_date, value).apply()

    /**
     * Legutóbb alk. verziószám, frissítésnél kell
     */
    var versionNumber: Int
        get() {
            return sharedPreferences.getInt(KEY_VERSION_NUMBER, -1)
        }
        set(value) {
            sharedPreferences.edit().putInt(KEY_VERSION_NUMBER, value).apply()
        }

    companion object{
        const val key_update_downloaded_but_not_installed = "key_update_downloaded_but_not_installed"
        const val key_latest_update_date = "key_latest_update_date"

        /**
         * Indításkor mentett verziószám
         */
        const val KEY_VERSION_NUMBER = "key_version_number"
    }
}