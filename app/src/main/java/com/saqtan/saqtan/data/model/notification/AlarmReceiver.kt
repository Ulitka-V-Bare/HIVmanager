package com.saqtan.saqtan.data.model.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

/**
 * класс, который работает в фоновом режиме, он обрабатывает входящие intent
 * и создает уведомления*/
@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("receiver","onReceive entered")
        try {
            val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
            if(context!=null) {
                NotificationHelper(context).createNotification(title = "Saqtan", message = message)
            }
            val pm = context!!.getSystemService(Context.POWER_SERVICE) as PowerManager
            val isScreenOn =
                if (Build.VERSION.SDK_INT >= 20) pm.isInteractive else pm.isScreenOn // проверить, включен ли экран

            if (!isScreenOn) {
                val wl = pm.newWakeLock(
                    PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "myApp:notificationLock"
                )
                wl.acquire(3000) //включение экрана, если он выключен
                //через 3 секунды, чтобы успеть обработать и выставить уведомление
            }
            Log.d("receiver","notification sent")
        }catch (e:Exception){
            Log.d("receiver","${e.message}")
        }

    }
}