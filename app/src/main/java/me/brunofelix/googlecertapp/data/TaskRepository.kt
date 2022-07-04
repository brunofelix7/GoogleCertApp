package me.brunofelix.googlecertapp.data

import androidx.paging.PagingSource
import androidx.sqlite.db.SupportSQLiteQuery

interface TaskRepository {

    suspend fun insert(task: Task): Long

    suspend fun delete(task: Task)

    suspend fun findById(id: Long): Task?

    fun findAll(query: SupportSQLiteQuery): PagingSource<Int, Task>

    fun createQuery(orderBy: TaskOrderByEnum?): SupportSQLiteQuery
}
