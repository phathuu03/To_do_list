package com.example.myapplication.reciver

import android.Manifest
import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gun0912.tedpermission.provider.TedPermissionProvider


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {



        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(TedPermissionProvider.context, "androidknowledge")
                .setSmallIcon(R.drawable.sym_def_app_icon)
                .setContentTitle("Reminder")
                .setContentText("week up  tao.")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notificationManagerCompat =
            NotificationManagerCompat.from(TedPermissionProvider.context)
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManagerCompat.notify(123, builder.build())    }
}