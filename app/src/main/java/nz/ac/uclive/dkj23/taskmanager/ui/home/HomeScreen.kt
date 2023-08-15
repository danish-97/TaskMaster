package nz.ac.uclive.dkj23.taskmanager.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nz.ac.uclive.dkj23.taskmanager.R
import nz.ac.uclive.dkj23.taskmanager.TaskManagerTopBar
import nz.ac.uclive.dkj23.taskmanager.model.Task
import nz.ac.uclive.dkj23.taskmanager.ui.AppViewModelProvider
import nz.ac.uclive.dkj23.taskmanager.ui.navigation.NavigationDestination
import nz.ac.uclive.dkj23.taskmanager.ui.theme.TaskManagerTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToCreateTask: () -> Unit,
    navigateToEditTask: (Int) -> Unit,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TaskManagerTopBar(
                title = stringResource(id = HomeDestination.titleRes),
                scrollBehavior = scrollBehavior,
                coroutineScope = coroutineScope,
                drawerState = drawerState,
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCreateTask,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.create_task_title)
                )
            }
        }
    ){contentPadding ->
        HomeBody(
            taskList = homeUiState.taskList,
            onEditTask = navigateToEditTask,
            onDelete = { task -> coroutineScope.launch { viewModel.deleteTask(task) } },
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeBody(
    taskList: List<Task>,
    onEditTask: (Int) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (taskList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_task_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            TaskList(
                taskList = taskList,
                onEditTask = onEditTask,
                onDelete = onDelete,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small)))
        }
    }
}

@Composable
private fun TaskList(
    taskList: List<Task>,
    onEditTask: (Int) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = taskList, key = { it.taskId }) {item ->
            TaskCard(
                task = item,
                onEditTask = onEditTask,
                onDelete = onDelete
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    task: Task,
    onEditTask: (Int) -> Unit,
    onDelete: (Task) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var isLongPressed by remember { mutableStateOf(false) }
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    Card (
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .combinedClickable(
                onClick = { expanded = !expanded },
                onLongClick = { isLongPressed = !isLongPressed },
            )
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = spring()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isLongPressed) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = stringResource(id = R.string.check_symbol),
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.checkmark_size)),
                        )
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clip(MaterialTheme.shapes.medium)
                            .weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                } else {
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clip(MaterialTheme.shapes.medium)
                            .weight(1f),
                    )
                }

                Spacer(Modifier.weight(1f))
                if (isLongPressed) {
                    OutlinedButton(
                        onClick = { onEditTask(task.taskId) },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(id = R.string.edit_button))
                    }
                    OutlinedButton(
                        onClick = { deleteConfirmationRequired = true },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete_button))
                    }
                    if (deleteConfirmationRequired) {
                        DeleteConfirmationDialog(
                            onDeleteConfirm = {
                                deleteConfirmationRequired = false
                                onDelete(task)
                            },
                            onDeleteCancel = { deleteConfirmationRequired = false },
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                        )
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = task.priority,
                            style = MaterialTheme.typography.labelLarge
                        )
                        DueByDays(days = calculateDaysUntilDue(dueDate = task.dueDate))
                    }
                }
            }
            if (expanded) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun DueByDays(days: Long) {
    val color = when {
        days <= 0 -> MaterialTheme.colorScheme.error
        days <= 3 -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = if (days > 0) "Due in $days days" else "Overdue by ${-days} days",
        color = color,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun calculateDaysUntilDue(dueDate: String): Long {
    val formattedDueDate = LocalDate.parse(dueDate, DateTimeFormatter.ISO_DATE)
    val currentDate = LocalDate.now()
    return ChronoUnit.DAYS.between(currentDate, formattedDueDate)
}

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TaskManagerTheme {
        HomeScreen(
            navigateToCreateTask = {},
            navigateToEditTask = {},
            rememberCoroutineScope(),
            rememberDrawerState(initialValue = DrawerValue.Closed))
    }
}