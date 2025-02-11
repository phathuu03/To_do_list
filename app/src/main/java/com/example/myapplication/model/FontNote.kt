package com.example.myapplication.model

import com.example.myapplication.utils.Utils

data class FontNote(

    val idFont: Int = generateId(),

    var pathFont: Font = Utils.pathFont,

    var fontSize: Int = 14,

    var isBold: Boolean = false,

    var isItalic: Boolean= false,

    var isUnderscoreL: Boolean= false,

    var isFontSample: Boolean= false


){
    companion object{
        private var current = 0

        private fun generateId() : Int{
            return ++current
        }
    }
}
