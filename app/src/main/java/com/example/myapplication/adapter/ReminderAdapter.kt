package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderReminderBinding
import com.example.myapplication.entity.NoteWithDetails

class ReminderAdapter(val data: MutableList<NoteWithDetails>) :
    RecyclerView.Adapter<ReminderAdapter.ViewHolderReminder>() {
    class ViewHolderReminder(val binding: ViewHolderReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind (noteWithDetails: NoteWithDetails){
                binding.tvTitle.text = noteWithDetails.note.title
                binding.tvDateStart.text = StringBuilder().apply {
                    append(noteWithDetails.note.dateStart + ", " + noteWithDetails.note.timeStart)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderReminder {
        val view =
            ViewHolderReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderReminder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolderReminder, position: Int) {
        holder.bind(data[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChanged(newData: MutableList<NoteWithDetails>){
        this.data.clear()
        this.data.addAll(newData)
        notifyDataSetChanged()
    }
}