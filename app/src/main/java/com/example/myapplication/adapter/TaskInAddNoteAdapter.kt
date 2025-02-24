package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderTaskInAddNoteBinding
import com.example.myapplication.model.Task

class TaskInAddNoteAdapter(
    private var data: MutableList<Task>,
    val onDelNote: (Task) -> Unit,
    val onChangeText: (String, Task) -> Unit,
    val onCheckChange: (Boolean, Task) -> Unit
) : RecyclerView.Adapter<TaskInAddNoteAdapter.ViewHolderTaskInAddNote>() {

    inner class ViewHolderTaskInAddNote(val binding: ViewHolderTaskInAddNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.btnDelNote.setOnClickListener { onDelNote(task) }


            binding.editNameTask.setText(task.nameTask)
            if (task.isChecked){
                binding.checkboxTask.isChecked = true
            }
            binding.editNameTask.addTextChangedListener { s ->
                if (s.toString() != task.nameTask) {
                    onChangeText(s.toString(), task)
                }
            }
            binding.checkboxTask.setOnCheckedChangeListener { _, isChecked ->
                onCheckChange(isChecked, task)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTaskInAddNote {
        return ViewHolderTaskInAddNote(
            ViewHolderTaskInAddNoteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderTaskInAddNote, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    // Dùng DiffUtil để cập nhật danh sách tối ưu hơn
    fun updateDataChanged(newData: List<Task>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = data.size
            override fun getNewListSize() = newData.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].idTask == newData[newItemPosition].idTask
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newData[newItemPosition]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        data.clear()
        data.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
}
