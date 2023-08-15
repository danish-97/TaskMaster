package nz.ac.uclive.dkj23.taskmanager.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Databases access object to access the Task Database
 */

@Dao
interface TaskDao {

    @Query("SELECT * from task ORDER BY name ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * from task WHERE taskId = :taskId")
    fun getTask(taskId: Int): Flow<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}