package com.example.myapplication.model

data class Task(
    val idTask: Int = generateId(),
    var nameTask :String,
    var isChecked: Boolean = false
) {
    companion object {
        private var currentId = 0

        private fun generateId(): Int {
            return ++currentId
        }
    }
}
