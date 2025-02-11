package com.example.myapplication.model

data class CategoryNote(
    val idCategory: Int = generate(),

    val nameCategory: String

) {
    companion object {
        private var currentId = 0
        private fun generate(): Int {
            return currentId++
        }
    }

}