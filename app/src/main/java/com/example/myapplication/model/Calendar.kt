package com.example.myapplication.model

data class Calendar(
    val id: Int = generateId(),
    val timestampL: Long,
    val hour :Long ,
    val minute : Long
) {
    companion object {
        private var currentId = 0

        private fun generateId(): Int {
            return ++currentId
        }
    }
}