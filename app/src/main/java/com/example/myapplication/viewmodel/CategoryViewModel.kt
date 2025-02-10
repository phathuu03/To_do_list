package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.CategoryNote

class CategoryViewModel : ViewModel() {

    private val _listCategory   =  MutableLiveData<List<CategoryNote>>()
    val listCategory: LiveData<List<CategoryNote>> = _listCategory



    fun insertDefault(categoryNoteList: List<CategoryNote>){
        _listCategory.value  = categoryNoteList
    }

    fun addCategory(categoryNote: CategoryNote) {
        val currentList = _listCategory.value ?: emptyList()
        _listCategory.value = currentList + categoryNote
    }
}