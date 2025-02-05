package com.example.myapplication.utils

import com.example.myapplication.R
import com.example.myapplication.model.Font
import com.example.myapplication.model.FontNote
import com.example.myapplication.model.LanguageApplication
import com.example.myapplication.utils.enums.TypeLanguage
import java.time.LocalDate
import java.time.LocalTime

object Utils {
     var pathFont = Font(nameFont = "Medium", resId = R.font.roboto_regular)
    private val currentTime = LocalTime.now()
    private val currentDate = LocalDate.now()
    val defaultFont: FontNote = FontNote(pathFont = pathFont , fontSize = 14)

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





}