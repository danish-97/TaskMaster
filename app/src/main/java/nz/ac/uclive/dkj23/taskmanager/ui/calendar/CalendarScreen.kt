package nz.ac.uclive.dkj23.taskmanager.ui.calendar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvent
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import nz.ac.uclive.dkj23.taskmanager.R
import nz.ac.uclive.dkj23.taskmanager.TaskManagerTopBar
import nz.ac.uclive.dkj23.taskmanager.model.Task
import nz.ac.uclive.dkj23.taskmanager.ui.AppViewModelProvider
import nz.ac.uclive.dkj23.taskmanager.ui.home.HomeViewModel
import nz.ac.uclive.dkj23.taskmanager.ui.navigation.NavigationDestination
import java.text.SimpleDateFormat
import java.util.Date


object CalendarDestination: NavigationDestination {
    override val route = "calendar_screen"
    override val titleRes = R.string.calendar_title
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "FlowOperatorInvokedInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    calendarViewModel: CalendarViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // To get current day
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val currentDay = currentDateTime.date

    val homeUiState by viewModel.homeUiState.collectAsState()
    val kalendarEvents = homeUiState.taskList.map { task ->
        KalendarEvent(
            date = task.dueDate.toLocalDate(),
            eventName = task.name,
            eventDescription = task.description
        )
    }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TaskManagerTopBar(
                title = stringResource(id = CalendarDestination.titleRes),
                coroutineScope = coroutineScope,
                drawerState = drawerState,
                canNavigateBack = false
            )
        },
    ) { contentPadding ->
        Kalendar(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            currentDay = currentDay,
            kalendarType = KalendarType.Firey,
            showLabel = true,
            events = KalendarEvents(kalendarEvents),
            onDayClick = { date, _ ->
                selectedDate = date
                calendarViewModel.updateTasksByDate(selectedDate.toString())
                showDialog = true
            }
        )
        if (showDialog) {
            selectedDate?.let {
                // Access the tasksByDate state flow
                val tasksByDate = calendarViewModel.tasksByDate.collectAsState().value
                val tasksForSelectedDate = tasksByDate[selectedDate.toString()] ?: emptyList()
                TaskListDialogue(
                    it,
                    tasksForSelectedDate,
                    onClose = { showDialog = false },
                )
            }
        }
    }
}

@Composable
fun TaskListDialogue(
    selectedDate: LocalDate,
    tasksForSelectedDate: List<Task>,
    onClose: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = { /*Do Nothing*/ },
        title = { Text(text = stringResource(id = R.string.calendar_popup, formatDate(selectedDate.toString()))) },
        confirmButton = {
            TextButton(onClick = onClose,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text(text = stringResource(R.string.close))
            }
        },
        modifier = Modifier.fillMaxWidth(),
        text = {
            if (tasksForSelectedDate.isNotEmpty()) {
                LazyColumn {
                    items(tasksForSelectedDate) {task ->
                        Text(text = task.name, modifier = Modifier.fillMaxWidth())
                    }
                }
            } else {
                Text(text = stringResource(id = R.string.no_calendar_tasks))
            }
        }
    )
}

@SuppressLint("SimpleDateFormat")
fun formatDate(inputDate: String): String {
    val dateFormat: Date = SimpleDateFormat("yyyy-MM-dd").parse(inputDate)!!
    return SimpleDateFormat("dd MMMM").format(dateFormat)
}