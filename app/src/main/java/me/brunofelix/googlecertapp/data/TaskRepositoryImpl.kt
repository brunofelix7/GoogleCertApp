package me.brunofelix.googlecertapp.data

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

class TaskRepositoryImpl constructor(
    private val dao: TaskDao
) : TaskRepository {

    override suspend fun insert(task: Task): Long {
        return dao.insert(task)
    }

    override suspend fun delete(task: Task) {
        dao.delete(task)
    }

    override suspend fun findById(id: Long): Task? {
        return dao.findById(id)
    }

    override fun findAll(query: SupportSQLiteQuery) = dao.findAll(query)

    override fun createQuery(orderBy: TaskOrderByEnum?): SupportSQLiteQuery {
        val defaultQuery = StringBuilder("SELECT * FROM task")

        when (orderBy) {
            TaskOrderByEnum.NAME_ASC -> defaultQuery.append(" ORDER BY name ASC")
            TaskOrderByEnum.NAME_DESC -> defaultQuery.append(" ORDER BY name DESC")
            TaskOrderByEnum.DATE_ASC -> defaultQuery.append(" ORDER BY date ASC")
            TaskOrderByEnum.DATE_DESC -> defaultQuery.append(" ORDER BY date DESC")
            else -> Unit
        }
        return SimpleSQLiteQuery(defaultQuery.toString())
    }
}
