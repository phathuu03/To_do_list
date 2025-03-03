package com.example.myapplication.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.myapplication.R
import com.example.myapplication.entity.NoteWithDetails
import com.example.myapplication.model.Font
import com.example.myapplication.model.FontNote
import com.example.myapplication.model.LanguageApplication
import com.example.myapplication.reciver.AlarmReceiver
import com.example.myapplication.utils.enums.TypeLanguage
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

object Utils {
     var pathFont = Font(nameFont = "Medium", resId = R.font.roboto_regular)
    private val currentTime = LocalTime.now()
    private val currentDate = LocalDate.now()
    val defaultFont: FontNote = FontNote()


    val fonts = mutableListOf<Font>() .apply {
        add(Font(nameFont = "Bold" , resId = R.font.roboto_bold))
        add(Font(nameFont = "Medium" , resId = R.font.roboto_medium))
        add(Font(nameFont = "Regular" , resId = R.font.roboto_regular))
    }

     val languages = listOf(
        LanguageApplication(1, "", TypeLanguage.EN,"English"),
        LanguageApplication(2, "", TypeLanguage.VN,"Việt nam"),
        LanguageApplication(3, "", TypeLanguage.KO,"Korea")
    )

    val emojiList = listOf(
        (0x1F600..0x1F64F),  // Mặt cười
        (0x1F400..0x1F4FF),  // Động vật & thiên nhiên
        (0x1F300..0x1F5FF),  // Biểu tượng & đồ vật
        (0x1F1E6..0x1F1FF)   // Cờ quốc gia
    ).flatten().map { Character.toChars(it).concatToString() }


    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(context : Context, hour: Int, minute: Int, requestCode : Int, noteWithDetail : NoteWithDetails){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context  , AlarmReceiver::class.java)
        intent.putExtra("id_note", noteWithDetail.note.idNote)
        intent.putExtra("title_note", noteWithDetail.note.title)
        intent.putExtra("content_note", noteWithDetail.note.content)
        val pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java) // Phải là cùng Intent đã đặt trước đó
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode, // requestCode phải giống khi đặt báo thức
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent) // Hủy báo thức
        pendingIntent.cancel()

    }







}