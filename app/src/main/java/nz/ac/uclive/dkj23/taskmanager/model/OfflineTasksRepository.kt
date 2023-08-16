package nz.ac.uclive.dkj23.taskmanager.model

import kotlinx.coroutines.flow.Flow

class OfflineTasksRepository(private val taskDao: TaskDao): TasksRepository {

    override fun getAllTasksStream(): Flow<List<Task>> = taskDao.getAllTasks()

    override fun getTaskStream(taskId: Int): Flow<Task> = taskDao.getTask(taskId)

    override fun getTasksByDate(dueDate: String): Flow<List<Task>> = taskDao.getTaskByDate(dueDate)

    override suspend fun addTask(task: Task) = taskDao.insert(task)

    override suspend fun deleteTask(task: Task) = taskDao.delete(task)

    override suspend fun updateTask(task: Task) = taskDao.update(task)

}