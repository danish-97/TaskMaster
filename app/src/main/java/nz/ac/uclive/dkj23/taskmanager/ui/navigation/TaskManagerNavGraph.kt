package nz.ac.uclive.dkj23.taskmanager.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import nz.ac.uclive.dkj23.taskmanager.AppDrawer
import nz.ac.uclive.dkj23.taskmanager.ui.home.HomeDestination
import nz.ac.uclive.dkj23.taskmanager.ui.home.HomeScreen
import nz.ac.uclive.dkj23.taskmanager.ui.calendar.CalendarDestination
import nz.ac.uclive.dkj23.taskmanager.ui.calendar.CalendarScreen
import nz.ac.uclive.dkj23.taskmanager.ui.task.CreateTask
import nz.ac.uclive.dkj23.taskmanager.ui.task.CreateTaskDestination
import nz.ac.uclive.dkj23.taskmanager.ui.task.TaskDetailDestination
import nz.ac.uclive.dkj23.taskmanager.ui.task.TaskDetailScreen

/**
 * Provides the Navigation graph for the application
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskManagerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: HomeDestination.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                route = currentRoute,
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateToCalendar = { navController.navigate(CalendarDestination.route) },
                navigateToCreateTask = { navController.navigate(CreateTaskDestination.route) }
            ) { coroutineScope.launch { drawerState.close() } }
        }
    ) {
        Scaffold {contentPadding ->
            Box(modifier = Modifier.padding(contentPadding))
            {
                NavHost(
                    navController = navController,
                    startDestination = HomeDestination.route,
                    modifier = modifier
                ) {
                    composable(route = HomeDestination.route) {
                        HomeScreen(
                            navigateToCreateTask = { navController.navigate(CreateTaskDestination.route) },
                            coroutineScope = coroutineScope,
                            drawerState = drawerState
                        )
                    }
                    composable(route = CreateTaskDestination.route) {
                        CreateTask(
                            navigateBack = { navController.navigate(HomeDestination.route) },
                            coroutineScope = coroutineScope,
                            drawerState = drawerState
                        )
                    }
                    composable(route = CalendarDestination.route) {
                        CalendarScreen(
                            coroutineScope = coroutineScope,
                            drawerState = drawerState
                        )
                    }
                    composable(route = TaskDetailDestination.route) {
                        TaskDetailScreen()
                    }
                }
            }
        }
    }
}