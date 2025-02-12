package com.example.myapplication.model

data class CustomCanvas(
    var id: Int = generateId(),

    val fileName: String,

    val uri: String
){
    companion object{
        private var currentId = 0

        private fun generateId() : Int {
            return ++currentId
        }
    }
}