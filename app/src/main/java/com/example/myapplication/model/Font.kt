package com.example.myapplication.model

data class Font(
    val idFont: Int  = generateId(),

    val nameFont : String,

    val resId: Int?,

    var isSelected : Boolean = false




){
    companion object{
        private var currentId = 0

        private fun generateId() : Int {
            return ++currentId
        }
    }
}