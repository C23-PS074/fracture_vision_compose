package com.bangkit.fracturevision

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bangkit.fracturevision.navigation.NavigationItem
import com.bangkit.fracturevision.navigation.Screen
import com.bangkit.fracturevision.screen.camerax.CameraXScreen
import com.bangkit.fracturevision.screen.home.HomeScreen
import com.bangkit.fracturevision.screen.login.LoginScreen
import com.bangkit.fracturevision.screen.login.LoginViewModelFactory
import com.bangkit.fracturevision.screen.profile.ProfileScreen
import com.bangkit.fracturevision.screen.result.ResultScreen
import com.bangkit.fracturevision.screen.scan.ScanScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FractureVisionApp(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    predictViewModel: PredictViewModel,
    navController: NavHostController = rememberNavController()
) {
    val photoUriFromCameraX = remember {
        mutableStateOf<Uri?>(null)
    }

    var showBottomNavBar by rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = modifier,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val list = listOf(
                Screen.Home.route, Screen.Profile.route
            )
            if (list.contains(currentRoute)) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    appViewModel = appViewModel,
                    navigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    appViewModel = appViewModel,
                    loginViewModel= viewModel(factory = LoginViewModelFactory(appViewModel)),
                    navigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                Screen.Scan.route,
            ) {
                ScanScreen(
                    appViewModel = appViewModel,
                    predictViewModel = predictViewModel,
                    photoUri = photoUriFromCameraX.value,
                    navigateToCameraX = {
                        navController.navigate(Screen.Camera.route)
                    },
                    navigateToResult = {
                        navController.navigate(Screen.Result.route) {
                            popUpTo(Screen.Scan.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Camera.route) {
                CameraXScreen(
                    navigateToScan = {
                        navController.navigate(Screen.Scan.route) {
                            popUpTo(Screen.Scan.route) { inclusive = true }
                        }
                    },
                    onImageCaptured = { uri -> photoUriFromCameraX.value = uri }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    appViewModel = appViewModel,
                    navigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(
                Screen.Result.route
            ) {
                ResultScreen(
                    predictViewModel = predictViewModel
                )
            }
        }
    }

}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavigationBar(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = "Home",
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = "Predict",
                icon = Icons.Default.AddCircle,
                screen = Screen.Scan
            ),
            NavigationItem(
                title = "Profile",
                icon = Icons.Default.Person,
                screen = Screen.Profile
            )
        )
        navigationItems.forEachIndexed { _, item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}