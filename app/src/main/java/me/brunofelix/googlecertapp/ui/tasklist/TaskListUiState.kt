package me.brunofelix.googlecertapp.ui.tasklist

sealed class TaskListUiState {
    object Loading: TaskListUiState()
    class Success(val message: String): TaskListUiState()
    class Error(val message: String): TaskListUiState()
}
