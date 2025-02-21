package com.example.myapplication.model

data class  AudioRecord(
    val id: Int = generateId() ,
    val fileName: String,
    val uri: String,
    var isPlaying : Boolean = false

){
    companion object{
        private var current: Int = 0
        private fun generateId() :Int {
            return ++current
        }
    }

}