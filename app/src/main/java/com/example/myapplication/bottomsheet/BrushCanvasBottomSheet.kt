package com.example.myapplication.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.canvas.GraphicView
import com.example.myapplication.databinding.FragmentCanvasBinding
import com.example.myapplication.viewmodel.CanvasViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BrushCanvasBottomSheet(private val viewModel: CanvasViewModel) : BottomSheetDialogFragment() {
    private lateinit var drawingView: GraphicView
    private val binding by lazy {
        FragmentCanvasBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawingView = binding.canvas
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClear.setOnClickListener {
            drawingView.clearCanvas()
        }
        binding.btnCloseBottomSheet.setOnClickListener {
            dismiss()
        }
        binding.saveBitmap.setOnClickListener {
            dismiss()
        }

        binding.btnSetPathColorBlack.setOnClickListener {
            drawingView.setColorPaint(ContextCompat.getColor(requireContext(), R.color.black))
        }
        binding.btnSetPathColorRed.setOnClickListener {
            val color = ContextCompat.getColor(requireContext(), R.color.red)
            drawingView.setColorPaint(color)
        }
        binding.btnSetPathColorGray.setOnClickListener {
            drawingView.setColorPaint(ContextCompat.getColor(requireContext(), R.color.gray))
        }
        binding.btnSetPathColorBlue.setOnClickListener {
            drawingView.setColorPaint(ContextCompat.getColor(requireContext(), R.color.blue))
        }

        binding.btnSetPathColorVariant.setOnClickListener {
            drawingView.setColorPaint(ContextCompat.getColor(requireContext(), R.color.variant))
        }

        binding.saveBitmap.setOnClickListener {
            saveBitMap()
        }

    }

    private fun saveBitMap() {
        drawingView.saveDrawing(requireContext())
        val canvas = drawingView.getCanvas()

        canvas?.let {
            Log.d("uri canvas", it.uri.toString())
            viewModel.insertCanvas(it)
        }
        dismiss()

    }
}