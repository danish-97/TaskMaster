package nz.ac.uclive.dkj23.taskmanager.ui.task

import androidx.compose.runtime.Composable
import nz.ac.uclive.dkj23.taskmanager.R
import nz.ac.uclive.dkj23.taskmanager.ui.navigation.NavigationDestination

object TaskDetailDestination: NavigationDestination {
    override val route = "task_details"
    override val titleRes = R.string.task_details_title
    const val taskIdArgument = "taskId"
    val routeWithArgs = "$route/{$taskIdArgument}"
}

@Composable
fun TaskDetailScreen() {

}