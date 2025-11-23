package com.unicamp.dslearn.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unicamp.dslearn.core.navigation.DSLearnNavHost
import com.unicamp.dslearn.core.navigation.Destination
import com.unicamp.dslearn.core.ui.theme.DSLearnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val startDestination = Destination.HOME
            var selectedDestination by rememberSaveable { mutableStateOf(startDestination) }

            val currentDestinationRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            val showBottomBar = Destination.hasRoute(currentDestinationRoute)

            DSLearnTheme {
                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = showBottomBar,
                            enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                            exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
                        ) {
                            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                                Destination.entries.forEach { destination ->
                                    NavigationBarItem(
                                        selected = selectedDestination == destination,
                                        onClick = {
                                            selectedDestination = destination
                                            navController.navigate(route = destination.route)
                                        },
                                        icon = {
                                            Icon(
                                                destination.icon,
                                                contentDescription = destination.contentDescription
                                            )
                                        },
                                        label = { Text(destination.label) }
                                    )
                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    DSLearnNavHost(
                        navController,
                        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                    )
                }
            }
        }
    }
}
