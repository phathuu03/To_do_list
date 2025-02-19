package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderCheckTaskBinding
import com.example.myapplication.entity.TaskEntity

class TaskAdapter(val data: List<TaskEntity>) : RecyclerView.Adapter<TaskAdapter.ViewHolderTask>() {
    class ViewHolderTask(val binding: ViewHolderCheckTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TaskEntity) {
            binding.tvId.text = data.nameTask
            binding.checkboxTask.isChecked = data.isChecked
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTask {
        val view =
            ViewHolderCheckTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderTask(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolderTask, position: Int) {
        holder.bind(data[position])
    }
}