package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalTime

class DateTimeViewModel: ViewModel() {

    private val _dateNow = MutableLiveData<String>()
    private val _timeNow = MutableLiveData<String>()
    val dateNow: LiveData<String> = _dateNow
    val timeNow: LiveData<String> = _timeNow

    init {
        setDateNow()
        setTimeNow()
    }

    private fun setDateNow() {
        val currentDate = LocalDate.now()
        val day = currentDate.dayOfMonth
        val month = currentDate.monthValue
        val year = currentDate.year

        val dateNow = StringBuilder().apply {
            append("${year}/")
            append("${month}/")
            append("$day ")
        }

        _dateNow.value = dateNow.toString()
    }

    private fun setTimeNow() {
        val currentTime = LocalTime.now()
        val hour = currentTime.hour
        val minute = currentTime.minute

        val timeNow = StringBuilder().apply {
            append("${hour}:")
            append("$minute  ")
            if(hour > 12){
                append("Pm")
            }else{
                append("Am")
            }
        }
        _timeNow.value = timeNow.toString()
    }

}