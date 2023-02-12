package com.example.hivmanager.data.model.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("receiver","onReceive entered")


        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        if(context!=null) {
            NotificationHelper(context).createNotification(title = "HIVmanager", message = message)
        }
        val pm = context!!.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isScreenOn =
            if (Build.VERSION.SDK_INT >= 20) pm.isInteractive else pm.isScreenOn // check if screen is on

        if (!isScreenOn) {
            val wl = pm.newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "myApp:notificationLock"
            )
            wl.acquire(3000) //set your time in milliseconds
        }
        Log.d("receiver","notification sent")
    }
}