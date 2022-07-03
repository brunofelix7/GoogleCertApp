package me.brunofelix.googlecertapp.ui.tasklist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.brunofelix.googlecertapp.data.Task
import me.brunofelix.googlecertapp.databinding.ItemTaskBinding

class TaskListAdapter : PagingDataAdapter<Task, TaskListAdapter.TaskListViewHolder>(DIFF_CALLBACK) {

    var listener: TaskListClickListener? = null
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val root = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskListViewHolder(root, listener, context)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
        }
    }

    /**
     * My ViewHolder
     */
    inner class TaskListViewHolder constructor(
        private val binding: ItemTaskBinding,
        private val listener: TaskListClickListener?,
        private val context: Context
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvName.text = task.name
            binding.tvState.text = task.state
            binding.tvDate.text = "2022-07-03"
            binding.layoutRoot.setOnClickListener {
                listener?.onTaskClick(task)
            }
        }
    }
}