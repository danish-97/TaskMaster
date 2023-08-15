package nz.ac.uclive.dkj23.taskmanager.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import nz.ac.uclive.dkj23.taskmanager.model.Task
import nz.ac.uclive.dkj23.taskmanager.model.TasksRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * ViewModel to validate and insert tasks in the Room database
 */
class CreateTaskViewModel(private val tasksRepository: TasksRepository): ViewModel() {

    /**
     * Holds current task ui state
     */
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState =
            TaskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    /**
     * Inserts a [Task] in the Room database
     */
    suspend fun saveTask() {
        if (validateInput()) {
            tasksRepository.addTask(taskUiState.taskDetails.toTask())
        }
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

}

/**
 * Represents Ui State for a Task
 */
data class TaskUiState(
    val taskDetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val taskId: Int = 0,
    val name: String = "",
    val description: String = "",
    val dueDate: String = convertLocalDateToString(LocalDate.now()),
    val priority: String = "",
    )

/**
 * Converting LocalDate to Date
 */
fun convertLocalDateToString(localDate: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return localDate.format(formatter)
}

/**
 * Extension function to convert [TaskUiState] to [Task].
 */
fun TaskDetails.toTask(): Task = Task(
    taskId = taskId,
    name = name,
    description = description,
    dueDate = dueDate,
    priority = priority
)

/**
 * Extension function to convert [Task] to [TaskUiState]
 */
fun Task.toTaskUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState(
    taskDetails = this.toTaskDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Task] to [TaskDetails]
 */
fun Task.toTaskDetails(): TaskDetails = TaskDetails(
    taskId = taskId,
    name = name,
    description = description,
    dueDate = dueDate,
    priority = priority
)