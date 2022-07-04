package me.brunofelix.googlecertapp.ui.taskdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.brunofelix.googlecertapp.data.Task
import me.brunofelix.googlecertapp.data.TaskRepository
import me.brunofelix.googlecertapp.utils.AppConstants
import me.brunofelix.googlecertapp.utils.AppProvider

class TaskDetailsViewModel constructor(
    private val repository: TaskRepository,
    private val dispatcher: CoroutineDispatcher,
    private val provider: AppProvider
): ViewModel() {

    private val _liveData = MutableLiveData<TaskDetailsUiState>()
    val liveData: LiveData<TaskDetailsUiState> get() = _liveData

    fun findTaskById(id: Long) {
        viewModelScope.launch(dispatcher) {
            val task = repository.findById(id)

            if (task != null) {
                withContext(Dispatchers.Main) {
                    _liveData.value = TaskDetailsUiState.OnFound(task)
                }
            } else {
                withContext(Dispatchers.Main) {
                    _liveData.value = TaskDetailsUiState.Error(AppConstants.NOT_FOUND_ERROR)
                }
            }
        }
    }

    fun updateTask(task: Task) {
        _liveData.value = TaskDetailsUiState.Loading

        viewModelScope.launch(dispatcher) {
            if (repository.insert(task) > 0) {
                withContext(Dispatchers.Main) {
                    _liveData.value = TaskDetailsUiState.OnUpdated
                }
            } else {
                withContext(Dispatchers.Main) {
                    _liveData.value = TaskDetailsUiState.Error(AppConstants.GENERIC_ERROR)
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        _liveData.value = TaskDetailsUiState.Loading

        viewModelScope.launch(dispatcher) {
            repository.delete(task)

            withContext(Dispatchers.Main) {
                _liveData.value = TaskDetailsUiState.OnDeleted
            }
        }
    }
}
