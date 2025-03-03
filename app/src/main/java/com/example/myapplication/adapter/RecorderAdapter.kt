package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderAudioBinding
import com.example.myapplication.entity.AudioRecordEntity
import com.example.myapplication.model.AudioRecord

class RecorderAdapter(
    val data: MutableList<Any>,
    val onClickItem: (Any) -> Unit,
    val onDeleteRecorder: (Any) -> Unit
) : RecyclerView.Adapter<RecorderAdapter.ViewHolderRecorder>() {

    // Lưu vị trí của item hiện tại đang được phát (play)
    var positionCurrent = -1  // Để lưu giữ vị trí của item đang phát hoặc tạm dừng

    inner class ViewHolderRecorder(val binding: ViewHolderAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Any, position: Int) {

            if (positionCurrent == position) {
                binding.btnPauseOrPlayRecorder.setBackgroundResource(com.google.android.exoplayer2.R.drawable.exo_ic_pause_circle_filled)
            } else {
                binding.btnPauseOrPlayRecorder.setBackgroundResource(com.google.android.exoplayer2.R.drawable.exo_ic_play_circle_filled)
            }

            binding.btnTrashRecorder.setOnClickListener {
                onDeleteRecorder(item)
            }

            binding.btnPauseOrPlayRecorder.setOnClickListener {
                if (item is AudioRecord) {
                    if (positionCurrent != position) {
                        positionCurrent = position
                        item.isPlaying = false
                    } else {
                        positionCurrent = -1
                        item.isPlaying = true
                    }
                    onClickItem(item)
                } else if (item is AudioRecordEntity) {
                    if (positionCurrent != position) {
                        positionCurrent = position
                        item.isPlaying = false
                    } else {
                        positionCurrent = -1
                        item.isPlaying = true
                    }
                    onClickItem(item)
                }



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

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataChanged(newData : MutableList<Any>){
        this.data.clear()
        this.data.addAll(newData)
        notifyDataSetChanged()
    }
}
