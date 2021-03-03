package com.madokasoftwares.mvvmkotlin.ui.tasks

import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.madokasoftwares.mvvmkotlin.data.Task
import com.madokasoftwares.mvvmkotlin.databinding.ItemTaskBinding

//ListAdapter is a type of recycleviewer
class TasksAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Task, TasksAdapter.TasksViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TasksViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }

                checkBoxCompleted.setOnClickListener{
                    val positon = adapterPosition
                    if(positon != RecyclerView.NO_POSITION){
                        val task=getItem(positon)
                        listener.onCheckBoxClick(task,checkBoxCompleted.isChecked)

                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                checkBoxCompleted.isChecked =
                    task.completed //checkbox checked if the task is completed
                textViewName.text = task.name
                textViewName.paint.isStrikeThruText =
                    task.completed //strike true when task.is completed
                labelPriority.isVisible = task.important//visile when task is important

            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onCheckBoxClick(
            task: Task,
            isChecked: Boolean
        ) //we want to handle when our checkbox are checked
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem


    }



}





