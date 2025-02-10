package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderAttachmentBinding
import com.example.myapplication.databinding.ViewHolderAudioBinding
import com.example.myapplication.databinding.ViewHolderCanvasBinding
import com.example.myapplication.databinding.ViewHolderTaskBinding
import com.example.myapplication.model.NoteItem

class NoteAdapter(private val noteItems : MutableList<NoteItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
        companion object{
            private const val TYPE_ATTACHMENT = 0
            private const val TYPE_CANVAS = 1
            private const val TYPE_AUDIO = 2
            private const val TYPE_TASK = 3
        }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<NoteItem>) {
        noteItems.clear()
        noteItems.addAll(newData)
        notifyDataSetChanged() // Cập nhật RecyclerView
    }
    override fun getItemViewType(position: Int): Int {
        return when(noteItems[position]){
            is NoteItem.AttachmentItem -> TYPE_ATTACHMENT
            is NoteItem.CanvasItem -> TYPE_CANVAS
            is NoteItem.AudioItem -> TYPE_AUDIO
            is NoteItem.TaskItem -> TYPE_TASK
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType){
            TYPE_ATTACHMENT ->{
                val binding = ViewHolderAttachmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AttachmentViewHolder(binding)
            }
            TYPE_CANVAS ->{
                val binding = ViewHolderCanvasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CanvasViewHolder(binding)
            }

            TYPE_AUDIO->{
                val binding = ViewHolderAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AudioViewHolder(binding)
            }
            TYPE_TASK->{
                val binding = ViewHolderTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TaskViewHolder(binding)
            }

           else -> throw IllegalArgumentException("Invalid View Type")

        }
    }

    override fun getItemCount(): Int = noteItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when(holder){
           is AttachmentViewHolder -> {
               val item = noteItems[position] as NoteItem.AttachmentItem
               holder.imageView.setImageURI(Uri.parse(item.attachment.uri.toString()))
           }
           is CanvasViewHolder -> {
               val item = noteItems[position] as NoteItem.CanvasItem
               holder.imageView.setImageURI(Uri.parse(item.canvas.uri.toString()))
           }
           is AudioViewHolder -> {
               val item = noteItems[position] as NoteItem.AudioItem
               holder.playButton.setOnClickListener {
                   val mediaPlayer = MediaPlayer.create(holder.itemView.context, Uri.parse(item.audio.uri.toString()))
                   mediaPlayer.start()
               }
           }
           is TaskViewHolder -> {
               val item = noteItems[position] as NoteItem.TaskItem
               holder.checkBox.text = item.task.nameTask
               holder.checkBox.isChecked = item.task.isChecked
           }
       }
    }

    class AttachmentViewHolder(binding: ViewHolderAttachmentBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.imageView
    }

    // ViewHolder cho Canvas
    class CanvasViewHolder(binding: ViewHolderCanvasBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.canvasImageView
    }

    // ViewHolder cho Audio
    class AudioViewHolder(binding: ViewHolderAudioBinding) : RecyclerView.ViewHolder(binding.root) {
        val playButton = binding.playButton
    }

    // ViewHolder cho Task
    class TaskViewHolder(binding: ViewHolderTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        val checkBox = binding.taskCheckBox
    }

}