package com.palinkas.raktar.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import com.palinkas.raktar.R

class NotificationHelper(context: Context) : ContextWrapper(context) {
    private var manager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                PRIMARY_CHANNEL,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel1.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            getManager()?.createNotificationChannel(channel1)
        }
    }

    fun notify(id: Int, notification: NotificationCompat.Builder?) {
        if (notification == null) return
        getManager()?.notify(id, notification.build())
    }

    private fun getManager(): NotificationManager? {
        if (manager == null) {
            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        }
        return manager
    }

    companion object {
        const val PRIMARY_CHANNEL = "default"
    }
}