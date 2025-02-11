package com.example.myapplication.listener

import com.example.myapplication.model.Task

interface DelTask {
    fun delTask (task : Task) : Unit
}