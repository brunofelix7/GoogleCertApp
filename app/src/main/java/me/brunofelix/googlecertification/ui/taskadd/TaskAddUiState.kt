package me.brunofelix.googlecertification.ui.taskadd

sealed class TaskAddUiState {
    object Loading: TaskAddUiState()
    class Success(val message: String): TaskAddUiState()
    class Error(val message: String): TaskAddUiState()
}
