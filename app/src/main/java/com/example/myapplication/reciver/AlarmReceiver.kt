package com.example.myapplication.reciver

import android.Manifest
import android.R
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.AddNoteActivity
import com.gun0912.tedpermission.provider.TedPermissionProvider


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title_note")
        val content = intent?.getStringExtra("content_note")
        val idNote = intent?.getLongExtra("id_note", -1)
        val intent = Intent(context , AddNoteActivity::class.java)
        intent.putExtra("id_note", idNote)
        val pendingIntent = PendingIntent.getActivity(context , 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT)


        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(TedPermissionProvider.context, "androidknowledge")
                .setSmallIcon(R.drawable.sym_def_app_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        val notificationManagerCompat =
            NotificationManagerCompat.from(TedPermissionProvider.context)
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        notificationManagerCompat.notify(123, builder.build())
    }
}