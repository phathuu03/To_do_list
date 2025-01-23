package com.example.myapplication.model

import android.net.Uri
import com.example.myapplication.utils.enums.MediaType

data class AttachmentNote(
    val idAttachment: Int,

    val uri : Uri,

    val type: MediaType,

    val pathDocument : Uri


)