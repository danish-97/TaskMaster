package nz.ac.uclive.dkj23.taskmanager.model

import android.content.Context

/**
 * App container for dependency injection
 */
interface AppContainer {
    val tasksRepository: TasksRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineTasksRepository]
 */
class AppDataContainer(private val context: Context): AppContainer {
    /**
     * Implementation for [TasksRepository]
     */
    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(TaskManagerDatabase.getDatabase(context).taskDao())
    }
}
