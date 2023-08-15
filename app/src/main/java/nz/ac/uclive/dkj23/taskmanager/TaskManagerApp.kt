package nz.ac.uclive.dkj23.taskmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nz.ac.uclive.dkj23.taskmanager.ui.home.HomeDestination
import nz.ac.uclive.dkj23.taskmanager.ui.navigation.TaskManagerNavHost
import nz.ac.uclive.dkj23.taskmanager.ui.task.CreateTaskDestination

/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun TaskManagerApp(navController: NavHostController = rememberNavController()) {
    TaskManagerNavHost(navController = navController)
}

/**
 * App bar to display title and side bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagerTopBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { coroutineScope.launch{ drawerState.open() } }
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(id = R.string.menu_icon)
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    route: String,
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToCreateTask: () -> Unit,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet(modifier = Modifier) {
        DrawerHeader(modifier)
        Spacer(modifier = Modifier.padding(5.dp))
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.labelSmall
                )
            },
            selected = route == HomeDestination.route,
            onClick = {
                navigateToHome()
                closeDrawer()
            },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home icon") },
            shape = MaterialTheme.shapes.small
        )

        NavigationDrawerItem(
            label = { Text(text = "Calender", style = MaterialTheme.typography.labelSmall) },
            selected = route == "Calendar",
            onClick = {
                navigateToCalendar()
                closeDrawer()
            },
            icon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calender Icon") },
            shape = MaterialTheme.shapes.small
        )

        NavigationDrawerItem(
            label = { Text(text = "Create Task", style = MaterialTheme.typography.labelSmall) },
            selected = route == CreateTaskDestination.route,
            onClick = {
                navigateToCreateTask()
                closeDrawer()
            },
            icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Create Task Icon") },
            shape = MaterialTheme.shapes.small
        )
    }
}



@Composable
fun DrawerHeader(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(15.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}