package com.example.myapplication.model

import android.net.Uri

data class CustomCanvas(
    var id: Int = generateId(),

    val fileName: String,

    val uri: Uri
){
    companion object{
        private var currentId = 0

        private fun generateId() : Int {
            return ++currentId
        }
    }
}