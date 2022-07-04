package me.brunofelix.googlecertapp.ui.taskdetails

import me.brunofelix.googlecertapp.data.Task

sealed class TaskDetailsUiState {
    object Loading: TaskDetailsUiState()
    class OnFound(val task: Task): TaskDetailsUiState()
    class Error(val message: String): TaskDetailsUiState()
    object OnUpdated: TaskDetailsUiState()
    object OnDeleted: TaskDetailsUiState()
}