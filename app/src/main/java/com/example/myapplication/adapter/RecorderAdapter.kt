package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderAudioBinding
import com.example.myapplication.model.AudioRecord

class RecorderAdapter(
    val data: MutableList<AudioRecord>,
    val onClickItem: (AudioRecord) -> Unit,
    val onDeleteRecorder: (AudioRecord) -> Unit
) : RecyclerView.Adapter<RecorderAdapter.ViewHolderRecorder>() {

    // Lưu vị trí của item hiện tại đang được phát (play)
    var positionCurrent = -1  // Để lưu giữ vị trí của item đang phát hoặc tạm dừng

    inner class ViewHolderRecorder(val binding: ViewHolderAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(audioRecord: AudioRecord, position: Int) {

            if (positionCurrent == position) {
                binding.btnPauseOrPlayRecorder.setBackgroundResource(com.google.android.exoplayer2.R.drawable.exo_ic_pause_circle_filled)
            } else {
                binding.btnPauseOrPlayRecorder.setBackgroundResource(com.google.android.exoplayer2.R.drawable.exo_ic_play_circle_filled)
            }

            binding.btnTrashRecorder.setOnClickListener {
                onDeleteRecorder(audioRecord)
            }

            binding.btnPauseOrPlayRecorder.setOnClickListener {

                if (positionCurrent != position) {
                    positionCurrent = position
                    audioRecord.isPlaying = false
                } else {
                    positionCurrent = -1
                    audioRecord.isPlaying = true
                }

                onClickItem(audioRecord)
                notifyDataSetChanged()
            }
        }
    }

    // Tạo ViewHolder cho item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRecorder {
        val view =
            ViewHolderAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderRecorder(view)
    }

    // Trả về số lượng item trong danh sách
    override fun getItemCount(): Int = data.size

    // Gọi bind để gán dữ liệu cho từng item
    override fun onBindViewHolder(holder: ViewHolderRecorder, position: Int) {
        holder.bind(data[position], position)
    }
}
