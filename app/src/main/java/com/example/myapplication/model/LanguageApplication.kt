package com.example.myapplication.model

import com.example.myapplication.utils.enums.TypeLanguage

data class LanguageApplication(
    val id : Int,
    val flag: String,
    val typeLanguage : TypeLanguage,
    val language:String
)