package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderTrashBinding
import com.example.myapplication.entity.NoteEntity

class TrashAdapter(val data: MutableList<NoteEntity>,
    val onRemoveNote: (NoteEntity)-> Unit
) :
    RecyclerView.Adapter<TrashAdapter.ViewHolderTrash>() {
  inner  class ViewHolderTrash(val binding: ViewHolderTrashBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(noteEntity: NoteEntity){
                binding.tvTitle.text = noteEntity.title
                binding.tvDateStart.text = StringBuilder().apply {
                    append(noteEntity.dateStart + ", " + noteEntity.timeStart)
                }

                binding.btnTrashNote.setOnClickListener {
                    onRemoveNote(noteEntity)
                }


            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTrash {
        val view =
            ViewHolderTrashBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderTrash(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolderTrash, position: Int) {
        holder.bind(data[position])
    }
}