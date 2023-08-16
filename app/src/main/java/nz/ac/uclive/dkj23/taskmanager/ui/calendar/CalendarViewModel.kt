package nz.ac.uclive.dkj23.taskmanager.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nz.ac.uclive.dkj23.taskmanager.model.Task
import nz.ac.uclive.dkj23.taskmanager.model.TasksRepository

/**
 * ViewModel to get the list of tasks by date
 */
class CalendarViewModel(private val tasksRepository: TasksRepository): ViewModel() {

    private val _tasksByDate: MutableStateFlow<Map<String, MutableList<Task>>> = MutableStateFlow(emptyMap())
    val tasksByDate: StateFlow<Map<String, MutableList<Task>>> = _tasksByDate

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    // State for tasks grouped by date
    fun updateTasksByDate(selectedDate: String) {
        viewModelScope.launch {
            _loadingState.value = true
            val tasks = tasksRepository.getTasksByDate(selectedDate)
            tasks.collect{taskList ->
                val tasksByDate = taskList.groupByTo(mutableMapOf()) { it.dueDate }
                _tasksByDate.value = tasksByDate
                _loadingState.value = false
            }
        }
    }
}