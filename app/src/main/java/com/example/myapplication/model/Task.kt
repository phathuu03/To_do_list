package com.example.myapplication.model

data class Task(
    private val idTask: Int = generateId(),
    val nameTask :String ,
    val isChecked: Boolean = false
) {
    companion object {
        private var currentId = 0

        private fun generateId(): Int {
            return ++currentId
        }
    }
}
