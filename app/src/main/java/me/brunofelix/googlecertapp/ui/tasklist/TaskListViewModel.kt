package me.brunofelix.googlecertapp.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.brunofelix.googlecertapp.R
import me.brunofelix.googlecertapp.data.Task
import me.brunofelix.googlecertapp.data.TaskOrderByEnum
import me.brunofelix.googlecertapp.data.TaskRepository
import me.brunofelix.googlecertapp.utils.AppProvider
import me.brunofelix.googlecertapp.utils.JsonReaderUtil

class TaskListViewModel constructor(
    private val repository: TaskRepository,
    private val dispatcher: CoroutineDispatcher,
    private val provider: AppProvider
) : ViewModel() {

    fun createInitialTasks() {
        viewModelScope.launch(dispatcher) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(provider.context())

            if (prefs.getBoolean(provider.res().getString(R.string.pref_is_db_empty), true)) {
                prefs.edit().apply {
                    putBoolean(provider.res().getString(R.string.pref_is_db_empty), false)
                    apply()
                }
                for (task in JsonReaderUtil.getDataFromJson(provider.context())) {
                    repository.insert(task)
                }
            }
        }
    }

    fun findAll(sortBy: TaskOrderByEnum?): Flow<PagingData<Task>> {
        return Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
            val query = repository.createQuery(sortBy)
            repository.findAll(query)
        }.flow
    }
}