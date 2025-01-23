package com.example.myapplication.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.BottomSheetAttachmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseAttachmentBottomSheetFragment  : BottomSheetDialogFragment() {

    private val binding by lazy {
        BottomSheetAttachmentBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}