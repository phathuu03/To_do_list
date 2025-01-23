package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Font
import com.example.myapplication.model.FontNote
import com.example.myapplication.utils.Utils.defaultFont

class NoteFontViewModel : ViewModel() {

    private var fontNote: FontNote = defaultFont
    private var _font = MutableLiveData<FontNote>()

    val font: LiveData<FontNote> = _font




     fun setFontDefault() {
         fontNote = defaultFont.copy()
         _font.value =  fontNote

    }

    fun setPathFont(pathFont: Font){
        fontNote.pathFont = pathFont
        _font.value  = fontNote
    }

    fun setFontSize(size: Int){
        fontNote.fontSize = size
        _font.value  = fontNote
    }

    fun setBold(){
        fontNote.isBold = !fontNote.isBold
        _font.value  = fontNote
    }
    fun setItalic(){
        fontNote.isItalic = !fontNote.isItalic
        _font.value  = fontNote
    }
    fun setUnderscoreL(){
        fontNote.isUnderscoreL = !fontNote.isUnderscoreL
        _font.value  = fontNote
    }
    fun setFontSample(){
        fontNote.isFontSample = !fontNote.isFontSample
        _font.value  = fontNote
    }



}
