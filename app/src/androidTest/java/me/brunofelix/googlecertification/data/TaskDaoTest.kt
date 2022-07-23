package me.brunofelix.googlecertification.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var database: AppDatabase
    private lateinit var dao: TaskDao

    @Test
    fun insertTest() = runTest {
        // Given
        val task = Task(name = "Todo test", date = System.currentTimeMillis())

        // When
        dao.insert(task)
        val taskInserted = dao.findById(1)

        // Then
        assertThat(taskInserted?.id).isEqualTo(1)
    }

    @Test
    fun deleteTest() = runTest {
        // Given
        val task = Task(name = "Todo test", date = System.currentTimeMillis())

        // When
        dao.insert(task)
        val taskInserted = dao.findById(1)

        dao.delete(taskInserted!!)
        val taskDeleted = dao.findById(1)

        // Then
        assertThat(taskDeleted).isNull()
    }

    @Test
    fun findByIdTest() = runTest {
        // Given
        val task = Task(name = "Todo test", date = System.currentTimeMillis())

        // When
        dao.insert(task)
        val taskInserted = dao.findById(1)

        // Then
        assertThat(taskInserted).isNotNull()
    }

    /** SETUP */
    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = database.taskDao()
    }

    /** SETUP */
    @After
    fun tearDown() {
        database.close()
    }
}