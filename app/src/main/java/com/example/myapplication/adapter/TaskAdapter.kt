package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderTaskBinding
import com.example.myapplication.listener.DelTask
import com.example.myapplication.model.Task

class TaskAdapter(private val tasks: MutableList<Task>,
   val delTask: DelTask
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

   inner class TaskViewHolder(val binding: ViewHolderTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task){
            binding.tvTask.text = task.nameTask

            binding.btnRemoveTask.setOnClickListener{
                delTask.delTask(task)
            }

            task.isChecked = binding.checkboxTask.isChecked

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val viewHolder = ViewHolderTaskBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return TaskViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(task = tasks[position])
    }
    fun updateAdapter(tasks: MutableList<Task>) {
        this.tasks.clear()
        this.tasks.addAll(tasks)
        notifyDataSetChanged()
    }



}