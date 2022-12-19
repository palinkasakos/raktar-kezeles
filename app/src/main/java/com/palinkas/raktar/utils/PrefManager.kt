package com.palinkas.raktar.utils

import android.app.Application
import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Beállítások és paraméterek
 */
class PrefManager @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val appContext: Application
) {
}
