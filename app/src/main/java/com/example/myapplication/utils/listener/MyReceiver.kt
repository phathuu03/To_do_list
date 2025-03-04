package com.example.myapplication.utils.listener

import android.content.ClipData
import android.view.View
import androidx.core.view.ContentInfoCompat
import androidx.core.view.OnReceiveContentListener

class MyReceiver : OnReceiveContentListener {
    companion object{
        val MIME_TYPES = arrayOf("image/*", "video/*")

    }
    override fun onReceiveContent(view: View, contentInfo: ContentInfoCompat): ContentInfoCompat {
        val split = contentInfo.partition { item: ClipData.Item -> item.uri != null }
        val uriContent = split.first
        val remaining = split.second
        if (uriContent != null) {
            // App-specific logic to handle the URI(s) in uriContent.

        }
        return remaining
    }}