package me.brunofelix.googlecertification.ui.taskadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.brunofelix.googlecertification.data.Task
import me.brunofelix.googlecertification.data.TaskRepository
import me.brunofelix.googlecertification.extension.scheduleWorker
import me.brunofelix.googlecertification.util.AppConstants
import me.brunofelix.googlecertification.util.AppProvider

class TaskAddViewModel constructor(
    private val repository: TaskRepository,
    private val dispatcher: CoroutineDispatcher,
    private val provider: AppProvider
) : ViewModel() {

    private val _liveData = MutableLiveData<TaskAddUiState>()
    val liveData: LiveData<TaskAddUiState> get() = _liveData

    fun addTask(task: Task) {
        _liveData.value = TaskAddUiState.Loading

        viewModelScope.launch(dispatcher) {
            val result = repository.insert(task)

            if (result > 0) {
                provider.context().scheduleWorker(task = task, workerTag = result)

                withContext(Dispatchers.Main) {
                    _liveData.value = TaskAddUiState.Success(AppConstants.SUCCESS_ADD)
                }
            } else {
                withContext(Dispatchers.Main) {
                    _liveData.value = TaskAddUiState.Error(AppConstants.GENERIC_ERROR)
                }
            }
        }
    }
}
