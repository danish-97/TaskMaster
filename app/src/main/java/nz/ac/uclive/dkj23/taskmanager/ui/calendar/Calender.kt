package nz.ac.uclive.dkj23.taskmanager.ui.calendar

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import nz.ac.uclive.dkj23.taskmanager.R
import nz.ac.uclive.dkj23.taskmanager.TaskManagerTopBar
import nz.ac.uclive.dkj23.taskmanager.ui.home.HomeDestination
import nz.ac.uclive.dkj23.taskmanager.ui.navigation.NavigationDestination

object CalendarDestination: NavigationDestination {
    override val route = "calendar_screen"
    override val titleRes = R.string.calendar_title
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState
) {
    Scaffold(
        topBar = {
            TaskManagerTopBar(
                title = stringResource(id = CalendarDestination.titleRes),
                coroutineScope = coroutineScope,
                drawerState = drawerState,
                canNavigateBack = false
            )
        },
    ) {

    }
}