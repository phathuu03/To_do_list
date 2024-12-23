package com.example.myapplication.model

import com.example.myapplication.enums.TypeLanguage

data class Language(
    val id : Int,
    val flag: String,
    val typeLanguage : TypeLanguage,
    val language:String
)