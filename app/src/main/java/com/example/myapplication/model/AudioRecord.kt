package com.example.myapplication.model

import android.net.Uri

data class  AudioRecord(
    val id: Int = generateId() ,
    val fileName: String,
    val uri: Uri,

){
    companion object{
        private var current: Int = 0
        private fun generateId() :Int {
            return ++current
        }
    }

}