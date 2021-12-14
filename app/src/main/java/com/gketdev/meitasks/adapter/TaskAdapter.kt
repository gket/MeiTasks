package com.gketdev.meitasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gketdev.meitasks.data.TaskEntity
import com.gketdev.meitasks.data.TaskItem
import com.gketdev.meitasks.databinding.ItemTaskBinding
import javax.inject.Inject
import kotlin.properties.Delegates

class TaskAdapter @Inject constructor() :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var onTaskClicked: ((TaskEntity) -> Unit) = {}

    var tasks: List<TaskEntity> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemBinding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskByPositioned = tasks[position]
        holder.bind(taskByPositioned)
    }

    override fun getItemCount() = tasks.size


    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskEntity) {
            binding.textViewTaskDetail.text = item.taskName
            binding.textViewProjectName.text = item.projectName
            binding.root.setOnClickListener {
                onTaskClicked.invoke(item)
            }
        }
    }


}