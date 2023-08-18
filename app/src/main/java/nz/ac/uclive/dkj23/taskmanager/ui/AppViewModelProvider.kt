package nz.ac.uclive.dkj23.taskmanager.ui

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nz.ac.uclive.dkj23.taskmanager.TaskManagerApplication
import nz.ac.uclive.dkj23.taskmanager.ui.task.CreateTaskViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import nz.ac.uclive.dkj23.taskmanager.ui.calendar.CalendarViewModel
import nz.ac.uclive.dkj23.taskmanager.ui.home.HomeViewModel
import nz.ac.uclive.dkj23.taskmanager.ui.task.EditTaskViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        //Initializer for HomeViewModel
        initializer {
            HomeViewModel(taskManagerApplication().container.tasksRepository)
        }
        // Initializer for CreateTaskViewModel
        initializer {
            CreateTaskViewModel(taskManagerApplication().container.tasksRepository)
        }

        initializer {
            EditTaskViewModel(
                this.createSavedStateHandle(),
                taskManagerApplication().container.tasksRepository
            )
        }

        initializer {
            CalendarViewModel(
                taskManagerApplication().container.tasksRepository
            )
        }
    }
}

fun CreationExtras.taskManagerApplication(): TaskManagerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskManagerApplication)