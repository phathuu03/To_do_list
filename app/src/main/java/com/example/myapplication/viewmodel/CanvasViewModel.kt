package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.CustomCanvas

class CanvasViewModel : ViewModel() {
    private val _canvas = MutableLiveData<List<CustomCanvas>>()
    val canvas: LiveData<List<CustomCanvas>> = _canvas

    fun insertCanvas(canvas: CustomCanvas){
        val currentList = _canvas.value ?: emptyList()
        _canvas.value = currentList + canvas
    }

    fun removeCanvas(canvas: CustomCanvas) {
        val currentList = _canvas.value ?: emptyList()
        _canvas.value = currentList.filter { it != canvas }
    }
}