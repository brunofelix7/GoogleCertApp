package me.brunofelix.googlecertapp.ui.tasklist

import me.brunofelix.googlecertapp.data.Task

interface TaskListClickListener {
    fun onTaskClick(task: Task)
}