package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.FontNote
import com.example.myapplication.utils.Utils.defaultFont

class NoteFontViewModel : ViewModel() {

    private var fontNote: FontNote = defaultFont.copy()
    private val _font = MutableLiveData<FontNote>()

    val font: LiveData<FontNote> = _font

    fun setFontDefault(){
        fontNote = defaultFont.copy()
        _font.value = fontNote
    }

    fun setFontCustom(fontNote: FontNote){
        _font.value = fontNote
    }




    fun setNameFont(name: String) {
        fontNote.nameFont = name
        _font.value = fontNote

    }

    fun setResIdFont(resId: Int) {
        fontNote.resId = resId
        _font.value = fontNote

    }

    fun setFontSize(size: Int) {
//        _font.value = fontNote.copy(fontSize = size)
        fontNote.fontSize = size
        _font.value = fontNote
    }

    fun setBold(bold: Boolean) {
//        _font.value = fontNote.copy(isBold= bold)
        fontNote.isBold = bold
        _font.value = fontNote
    }


    fun setItalic(italic: Boolean) {
//        _font.value = fontNote.copy(isItalic = italic)
        fontNote.isItalic = italic
        _font.value = fontNote
    }


    fun setUnderscoreL(isUnder: Boolean) {
//        _font.value = fontNote.copy(isUnderscoreL = isUnder)
        fontNote.isUnderscoreL = isUnder
        _font.value = fontNote
    }


    fun setFontSample(fontSample: Boolean) {
//        _font.value = fontNote.copy(isFontSample = fontSample)
        fontNote.isFontSample = fontSample
        _font.value = fontNote
    }



}
