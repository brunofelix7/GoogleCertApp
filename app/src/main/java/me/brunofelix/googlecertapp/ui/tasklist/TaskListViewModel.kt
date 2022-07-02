package me.brunofelix.googlecertapp.ui.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.brunofelix.googlecertapp.data.Task
import me.brunofelix.googlecertapp.data.TaskRepository
import me.brunofelix.googlecertapp.utils.AppConstants
import me.brunofelix.googlecertapp.utils.AppProvider

class TaskListViewModel constructor(
    private val repository: TaskRepository,
    private val dispatcher: CoroutineDispatcher,
    private val provider: AppProvider
) : ViewModel() {

    private val _liveData = MutableLiveData<TaskListUiState>()
    val liveData: LiveData<TaskListUiState> get() = _liveData

    fun addTask(task: Task) {
        _liveData.value = TaskListUiState.Loading

        viewModelScope.launch(dispatcher) {
            if (repository.insert(task) > 0) {
                withContext(Dispatchers.Main) {
                    _liveData.value = TaskListUiState.Success(AppConstants.SUCCESS_ADD)
                }
            } else {
                withContext(Dispatchers.Main) {
                    _liveData.value = TaskListUiState.Error(AppConstants.GENERIC_ERROR)
                }
            }
        }
    }

    fun findAll(sortBy: String?): Flow<PagingData<Task>> {
        return Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
            val query = repository.createQuery(sortBy)
            repository.findAll(query)
        }.flow
    }
}