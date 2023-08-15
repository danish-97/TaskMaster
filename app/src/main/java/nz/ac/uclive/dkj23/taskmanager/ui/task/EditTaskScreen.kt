package nz.ac.uclive.dkj23.taskmanager.ui.task

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nz.ac.uclive.dkj23.taskmanager.R
import nz.ac.uclive.dkj23.taskmanager.TaskManagerTopBar
import nz.ac.uclive.dkj23.taskmanager.ui.AppViewModelProvider
import nz.ac.uclive.dkj23.taskmanager.ui.navigation.NavigationDestination

object EditTaskDestination: NavigationDestination {
    override val route = "task_details"
    override val titleRes = R.string.task_details_title
    const val taskIdArgument = "taskId"
    val routeWithArgs = "$route/{$taskIdArgument}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: EditTaskViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            TaskManagerTopBar(
                title = stringResource(EditTaskDestination.titleRes),
                canNavigateBack = true,
                coroutineScope = coroutineScope,
                drawerState = drawerState,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) {contentPadding ->
        CreateTaskBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            onCreateClick = {
                coroutineScope.launch {
                    viewModel.updateItem()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(contentPadding)
            )

    }
}