package nz.ac.uclive.dkj23.taskmanager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import nz.ac.uclive.dkj23.taskmanager.model.Task
import nz.ac.uclive.dkj23.taskmanager.model.TasksRepository

/**
 * ViewModel to retrieve all the tasks in the Room database
 */
class HomeViewModel(private val tasksRepository: TasksRepository): ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [TasksRepository] and mapped
     * to [HomeUiState]
     */
    var homeUiState: StateFlow<HomeUiState> =
        tasksRepository.getAllTasksStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    suspend fun deleteTask(task: Task) {
        tasksRepository.deleteTask(task)
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val taskList:List<Task> = listOf(),
)