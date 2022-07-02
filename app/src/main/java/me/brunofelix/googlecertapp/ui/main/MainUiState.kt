package me.brunofelix.googlecertapp.ui.main

sealed class MainUiState {
    object Loading: MainUiState()
    class Success(val message: String): MainUiState()
    class Error(val message: String): MainUiState()
}
