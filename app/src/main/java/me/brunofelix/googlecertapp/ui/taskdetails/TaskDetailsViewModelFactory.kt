package me.brunofelix.googlecertapp.ui.taskdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import me.brunofelix.googlecertapp.data.TaskRepository
import me.brunofelix.googlecertapp.utils.AppProvider
import java.lang.IllegalArgumentException

class TaskDetailsViewModelFactory constructor(
    private val repository: TaskRepository,
    private val dispatcher: CoroutineDispatcher,
    private val provider: AppProvider
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TaskDetailsViewModel::class.java)) {
            TaskDetailsViewModel(repository, dispatcher, provider) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}