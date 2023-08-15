package nz.ac.uclive.dkj23.taskmanager.model

import kotlinx.coroutines.flow.Flow


/**
 * Repository that provides add, update, delete and retrieve of [Task] from a given data source.
 */
interface TasksRepository {
    /**
     * Retrieve all the tasks from the given data source
     */
    fun getAllTasksStream(): Flow<List<Task>>

    /**
     * Retrieve a task from the given data source that matches with the [taskId]
     */
    fun getTaskStream(taskId: Int): Flow<Task>

    /**
     * Add task in the data source
     */
    suspend fun addTask(task: Task)

    /**
     * Delete task from the data source
     */
    suspend fun deleteTask(task: Task)

    /**
     * Update task in the data source
     */
    suspend fun updateTask(task: Task)
}