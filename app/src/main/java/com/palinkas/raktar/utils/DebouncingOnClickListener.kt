package com.palinkas.raktar.utils

import android.os.Handler
import android.os.Looper
import android.view.View

/**
 * Véletlen dupla kattintás megelőzésére
 */
class DebouncingOnClickListener(private val event: (view: View?) -> Unit) : View.OnClickListener {

    private var enabled = true

    override fun onClick(v: View?) {
        if (enabled) {
            enabled = false
            Handler(Looper.getMainLooper()).postDelayed({ enabled = true }, 200)
            event(v)
        }
    }
}
