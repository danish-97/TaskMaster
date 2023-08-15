package nz.ac.uclive.dkj23.taskmanager.ui

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nz.ac.uclive.dkj23.taskmanager.TaskManagerApplication
import nz.ac.uclive.dkj23.taskmanager.ui.task.CreateTaskViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import nz.ac.uclive.dkj23.taskmanager.ui.home.HomeViewModel

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
    }
}

fun CreationExtras.taskManagerApplication(): TaskManagerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskManagerApplication)