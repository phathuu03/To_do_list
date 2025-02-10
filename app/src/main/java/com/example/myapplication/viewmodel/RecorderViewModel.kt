package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.AudioRecord

class RecorderViewModel : ViewModel() {
    private val _recorders = MutableLiveData<List<AudioRecord>>()

    val recorders: LiveData<List<AudioRecord>> = _recorders

    fun insertRecorder (record: AudioRecord){
        val currencyRecord = _recorders.value ?: emptyList()

            _recorders.value = currencyRecord + record

    }

}