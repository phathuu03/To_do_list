package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderTaskInAddNoteBinding
import com.example.myapplication.model.Task

class TaskInAddNoteAdapter(
    private var data: List<Task>,
    val onDelNote: (Task) -> Unit,
    val onChangeText: (String, Task) -> Unit

) : RecyclerView.Adapter<TaskInAddNoteAdapter.ViewHolderTaskInAddNote>() {

    inner class ViewHolderTaskInAddNote(val binding: ViewHolderTaskInAddNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(task: Task) {
            binding.btnDelNote.setOnClickListener {
                onDelNote(task)
            }
            binding.editNameTask.addTextChangedListener{s ->
                onChangeText(s.toString() , task)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TaskInAddNoteAdapter.ViewHolderTaskInAddNote {
        return ViewHolderTaskInAddNote(
            ViewHolderTaskInAddNoteBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: TaskInAddNoteAdapter.ViewHolderTaskInAddNote, position: Int
    ) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataChanged(newData: MutableList<Task>) {
        this.data = newData.toList()
        notifyDataSetChanged()
    }
}