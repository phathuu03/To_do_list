package com.example.myapplication.model

data class Calendar(
    val id: Int = generateId(),
    val timestampL: Long,
    var hour: Int,
    var minute: Int
) {
    companion object {
        private var currentId = 0

        private fun generateId(): Int {
            return ++currentId
        }
    }
}