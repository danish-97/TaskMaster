package nz.ac.uclive.dkj23.taskmanager.ui.task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nz.ac.uclive.dkj23.taskmanager.R
import nz.ac.uclive.dkj23.taskmanager.TaskManagerTopBar
import nz.ac.uclive.dkj23.taskmanager.ui.AppViewModelProvider
import nz.ac.uclive.dkj23.taskmanager.ui.navigation.NavigationDestination
import nz.ac.uclive.dkj23.taskmanager.ui.theme.TaskManagerTheme
import java.text.SimpleDateFormat
import java.util.Calendar

object CreateTaskDestination : NavigationDestination {
    override val route = "create_task"
    override val titleRes = R.string.create_task_title
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTask(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: CreateTaskViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            TaskManagerTopBar(
                title = stringResource(id = CreateTaskDestination.titleRes),
                coroutineScope = coroutineScope,
                drawerState = drawerState,
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
    ) { innerPadding ->
        CreateTaskBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            onCreateClick = {
                coroutineScope.launch {
                    viewModel.saveTask()
                    navigateBack()
                }
            },
            canShare = false,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@SuppressLint("SimpleDateFormat")
private fun getPickedDateAsString(year: Int, month: Int, day: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(calendar.time)
}

@Composable
fun CreateTaskBody(
    taskUiState: TaskUiState,
    onTaskValueChange: (TaskDetails) -> Unit,
    onCreateClick: () -> Unit,
    canShare: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val shareTaskString = stringResource(id = R.string.share_task)

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        TaskInputForm(
            taskDetails = taskUiState.taskDetails,
            onValueChange = onTaskValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            if (canShare) {
                Button(
                    onClick = {
                        shareTask(
                            task = taskUiState.taskDetails.toTask(),
                            context = context,
                            shareTaskString = shareTaskString
                        )
                    },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = stringResource(id = R.string.share_task),
                        modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small))
                    )
                    Text(text = stringResource(id = R.string.share_task))
                }
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
            }
            Button(
                onClick = onCreateClick,
                enabled = taskUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (canShare) Icons.Filled.Edit else Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.share_task),
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small))
                )
                Text(text = if (canShare) stringResource(id = R.string.update_task_action)
                else stringResource(id = R.string.create_task_action))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInputForm(
    taskDetails: TaskDetails,
    modifier: Modifier,
    onValueChange: (TaskDetails) -> Unit = {},
    enabled: Boolean = true
) {
    // Variables required for the dropdown menu
    var expanded by remember { mutableStateOf(false) }
    val priorityList = listOf(
        "-",
        stringResource(id = R.string.high),
        stringResource(id = R.string.medium),
        stringResource(id = R.string.low)
    )
    var selectedItem by remember { mutableStateOf(priorityList[0]) }

    // Variables required for the date-picker popup
    var selectedDate by remember { mutableStateOf("") }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    // Fetching the current day, month and year
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val day = calendar[Calendar.DAY_OF_MONTH]

    // Something to trigger the onValueChange event when choosing a date using date-picker
    val dateSetListener = DatePickerDialog.OnDateSetListener{ _, newYear, newMonth, newDay ->
        selectedDate = getPickedDateAsString(newYear, newMonth, newDay)
        onValueChange(taskDetails.copy(dueDate = getPickedDateAsString(newYear, newMonth, newDay)))
    }

    val datePicker = DatePickerDialog(
        context, dateSetListener, year, month, day
    )
    datePicker.datePicker.minDate = calendar.timeInMillis

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = taskDetails.name,
            onValueChange = { onValueChange(taskDetails.copy(name = it)) },
            label = { Text(stringResource(id = R.string.task_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = taskDetails.description,
            onValueChange = { onValueChange(taskDetails.copy(description = it)) },
            label = { Text(stringResource(id = R.string.task_description)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = taskDetails.dueDate,
            onValueChange = { },
            label = { Text(stringResource(id = R.string.task_due_date_req)) },
            enabled = enabled,
            readOnly = true,
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = stringResource(id = R.string.date_icon),
                    modifier = Modifier.clickable { datePicker.show() },
                )
            }
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = { },
                label = { Text(stringResource(id = R.string.task_priority)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                }
            )
            // Creating the dropdown list
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize()
            ) {
                priorityList.forEach{ priorityLabel ->
                    DropdownMenuItem(
                        text = { Text(text = priorityLabel) },
                        onClick = {
                            selectedItem = priorityLabel
                            expanded = false
                            onValueChange(taskDetails.copy(priority = priorityLabel))
                        })
                }
            }
        }
        if (enabled) {
            Text(
                text = stringResource(id = R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview (showBackground = true)
@Composable
fun CreateTaskPreview() {
    TaskManagerTheme {
        CreateTask(
            navigateBack = {},
            onNavigateUp = {},
            rememberCoroutineScope(),
            rememberDrawerState(initialValue = DrawerValue.Closed)
        )
    }
}