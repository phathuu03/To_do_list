package com.example.myapplication.bottomsheet

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.BottomSheetChooseFontBinding
import com.example.myapplication.listener.PasserFontNote
import com.example.myapplication.model.Font
import com.example.myapplication.model.FontNote
import com.example.myapplication.viewmodel.NoteFontViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseFontBottomSheet(val passerFontNote: PasserFontNote) : BottomSheetDialogFragment(), ChooseFontBottomFragment.OnClickFont {

    private var fontSelected: Font? = null
    private lateinit var tvNameFont: TextView
    private lateinit var viewModel: NoteFontViewModel
    private lateinit var spinner: Spinner
    private lateinit var fontSizes: List<Int>
    private lateinit var btnSetBold: ImageButton
    private lateinit var btnSetItalic: ImageButton
    private lateinit var btnSetUnderscore: ImageButton
    private lateinit var btnSetFontSample: ImageButton
    private var colorToolSelected: Int = 0
    private var colorToolNonSelected: Int = 0
    private lateinit var btnSetFont: ImageButton

    private var fontNote: FontNote? = null


    private val binding by lazy {
        BottomSheetChooseFontBinding.inflate(layoutInflater)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        initViewDefault()
        initViewSpinner()
        clickItemToolSetStyle()


    }

    override fun onStart() {
        super.onStart()
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

        binding.btnSelectFont.setOnClickListener {
            val bottomSheetChooseFont = ChooseFontBottomFragment(this)
            bottomSheetChooseFont.show(
                requireActivity().supportFragmentManager,
                bottomSheetChooseFont.tag
            )
        }

        binding.btnCloseBottomSheet.setOnClickListener {
            passerFontNote.passerFontNote(null)
            dismiss()
        }

        btnSetFont.setOnClickListener {

            viewModel.font.observe(requireActivity()){
                passerFontNote.passerFontNote(it)
            }

            dismiss()
        }


        val bottomSheet = view.parent as View
        val behavior = BottomSheetBehavior.from(bottomSheet)

        isCancelable = false
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                TODO("Not yet implemented")
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                TODO("Not yet implemented")
            }

        })

    }




    private fun clickItemToolSetStyle() {
        viewModel.font.observe(this) { font ->
            btnSetBold.setOnClickListener {
                viewModel.setBold(!font.isBold)
            }
            btnSetItalic.setOnClickListener {
                viewModel.setItalic(!font.isItalic)
            }
            btnSetUnderscore.setOnClickListener {
                viewModel.setUnderscoreL(!font.isUnderscoreL)

            }

            btnSetFontSample.setOnClickListener {
                viewModel.setFontSample(!font.isFontSample)
            }

        }


    }


    private fun initViewSpinner() {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fontSizes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val itemSelected = fontSizes[position]
                viewModel.setFontSize(itemSelected)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }


    @SuppressLint("SetTextI18n")
    private fun initViewDefault() {


        viewModel.font.observe(this) { fontNote ->

            tvNameFont.text = fontNote.pathFont.nameFont

            val defaultPosition = fontSizes.indexOf(fontNote.fontSize)
            spinner.setSelection(defaultPosition)

            if (fontNote.isBold) {
                btnSetBold.backgroundTintList = ColorStateList.valueOf(colorToolSelected)
            } else {
                btnSetBold.backgroundTintList = ColorStateList.valueOf(colorToolNonSelected)

            }


            if (fontNote.isUnderscoreL) {
                btnSetUnderscore.backgroundTintList = ColorStateList.valueOf(colorToolSelected)
            } else {
                btnSetUnderscore.backgroundTintList = ColorStateList.valueOf(colorToolNonSelected)

            }

            if (fontNote.isItalic) {
                btnSetItalic.backgroundTintList = ColorStateList.valueOf(colorToolSelected)
            } else {
                btnSetItalic.backgroundTintList = ColorStateList.valueOf(colorToolNonSelected)

            }


            if (fontNote.isFontSample) {
                btnSetFontSample.backgroundTintList = ColorStateList.valueOf(colorToolSelected)
            } else {
                btnSetFontSample.backgroundTintList = ColorStateList.valueOf(colorToolNonSelected)

            }


        }
    }

    private fun initView() {
        colorToolSelected = ContextCompat.getColor(
            requireContext(),
            me.relex.circleindicator.R.color.design_default_color_primary
        )
        colorToolNonSelected = ContextCompat.getColor(requireContext(), R.color.black)
        viewModel = ViewModelProvider(this)[NoteFontViewModel::class.java]
        tvNameFont = binding.tvNameFont
        spinner = binding.spinner
        fontSizes = mutableListOf<Int>().apply {
            for (size in 4..30) {
                add(size)
            }
        }
        btnSetBold = binding.btnSetFontBold
        btnSetUnderscore = binding.btnSetFontUnderscore
        btnSetItalic = binding.btnSetFontItalic
        btnSetFontSample = binding.btnSetFontSample
        btnSetFont = binding.btnSetFont


    }


    override fun sendFont(font: Font) {

        viewModel.setPathFont(font)

    }

}