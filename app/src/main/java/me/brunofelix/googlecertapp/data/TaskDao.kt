package me.brunofelix.googlecertapp.data

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import org.jetbrains.annotations.NotNull

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun findById(id: Long): Task?

    @RawQuery(observedEntities = [Task::class])
    fun findAll(@NotNull query: SupportSQLiteQuery): PagingSource<Int, Task>
}
