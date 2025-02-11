package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderAttachmentBinding
import com.example.myapplication.databinding.ViewHolderCanvasBinding
import com.example.myapplication.model.NoteItem

class NoteAdapter(private val noteItems : MutableList<NoteItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
        companion object{
            private const val TYPE_ATTACHMENT = 0
            private const val TYPE_CANVAS = 1

        }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<NoteItem>) {
        noteItems.clear()
        noteItems.addAll(newData)
        notifyDataSetChanged() // Cập nhật RecyclerView
    }
    fun removeItem(position: Int) {
        if (position >= 0 && position < noteItems.size) {
            noteItems.removeAt(position)
            notifyItemRemoved(position)
        }



    }
    override fun getItemViewType(position: Int): Int {
        return when(noteItems[position]){
            is NoteItem.AttachmentItem -> TYPE_ATTACHMENT
            is NoteItem.CanvasItem -> TYPE_CANVAS

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



           else -> throw IllegalArgumentException("Invalid View Type")

        }
    }

    override fun getItemCount(): Int = noteItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when(holder){
           is AttachmentViewHolder -> {
               val item = noteItems[position] as NoteItem.AttachmentItem
//               holder.imageView.setImageURI(Uri.parse(item.attachment.uri.toString()))
           }
           is CanvasViewHolder -> {
               val item = noteItems[position] as NoteItem.CanvasItem
               holder.imageView.setImageURI(Uri.parse(item.canvas.uri.toString()))

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





}