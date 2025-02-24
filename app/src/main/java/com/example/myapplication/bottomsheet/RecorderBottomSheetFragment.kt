package com.example.myapplication.bottomsheet

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.databinding.BottomSheetRecoderBinding
import com.example.myapplication.model.AudioRecord
import com.example.myapplication.viewmodel.RecorderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.IOException

class RecorderBottomSheetFragment(private val viewModel: RecorderViewModel) : BottomSheetDialogFragment() {
    private var isStatRecorder = false
    private var mediaRecorder: MediaRecorder? = null
    private var audioUri: Uri? = null
    private var fileName: String = ""
    private var outputDir: File? = null


    private var statRecord: Long = 0L
     private var handler = Handler(Looper.getMainLooper())


    private val binding by lazy {
        BottomSheetRecoderBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.btnStartOrPauseRecorder.setOnClickListener {
           if(!isStatRecorder){
               startRecorder()
               isStatRecorder = true
           }else{
               stopRecording()
               isStatRecorder = false
           }



        }
        binding.btnClose.setOnClickListener {
            deleteFile()
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            audioUri?.let {
                val nameUri = it.toString()
                val recorder = AudioRecord(fileName = fileName , uri = nameUri)
                viewModel.insertRecorder(recorder)
                Toast.makeText(requireContext(), recorder.uri, Toast.LENGTH_SHORT).show()
            }

            dismiss()

        }


    }


    private val updateTimeRunnable = object : Runnable {
        @SuppressLint("DefaultLocale")
        override fun run() {
            val elapsedTime = System.currentTimeMillis() - statRecord
            val seconds = (elapsedTime / 1000) % 60
            val minutes = (elapsedTime / 1000) / 60
            binding.tvDuration.text = String.format("%02d:%02d", minutes, seconds)
            handler.postDelayed(this, 1000) // Cập nhật mỗi giây
        }
    }


    private fun deleteFile() {
        fileName.let {
            val file = File(it)
            if (file.exists()) {
                file.delete()
            }
        }



    }

    private fun playRecorder() {

        if (audioUri != null) {
            try {

                val mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA).build()
                    )
                    setDataSource(requireActivity(), audioUri!!)
                    prepare()
                    start()
                }

                Toast.makeText(requireContext(), "$audioUri", Toast.LENGTH_SHORT).show()
                Log.d("URI", "$audioUri")
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(), "Error playing audio: ${e.message}", Toast.LENGTH_SHORT
                ).show()
                Log.e("MediaPlayer", "Error playing audio", e)
            }
        } else {
            Toast.makeText(requireContext(), "uri null", Toast.LENGTH_SHORT).show()
        }

    }

    private fun startRecorder() {
        outputDir = requireActivity().getExternalFilesDir(null)

        outputDir?.let {
            val timeSpan = System.currentTimeMillis()
            fileName = "${it.absolutePath}/audio_record_$timeSpan.3gp"

            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(fileName)
            }

            try {
                mediaRecorder?.prepare()
                mediaRecorder?.start()

                statRecord = System.currentTimeMillis()
                handler.post(updateTimeRunnable)
            } catch (_: IOException) {
            }


        }

    }

    private fun stopRecording() {
        mediaRecorder?.let {
            it.stop()
            it.release()
            mediaRecorder = null
            handler.removeCallbacks(updateTimeRunnable)

            val file = File(fileName)
            audioUri = Uri.fromFile(file)
        }
    }


}
