package com.bangkit.fracturevision

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bangkit.fracturevision.navigation.Screen
import com.bangkit.fracturevision.screen.home.HomeScreen
import com.bangkit.fracturevision.screen.login.LoginScreen
import com.bangkit.fracturevision.screen.login.ViewModelFactory
import com.bangkit.fracturevision.screen.scan.ScanScreen

@Composable
fun FractureVisionApp(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                appViewModel = appViewModel,
                navigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                navigateToScan = {
                    navController.navigate(Screen.Scan.route)
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                appViewModel = appViewModel,
                loginViewModel= viewModel(factory = ViewModelFactory(appViewModel)),
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Scan.route) {
            ScanScreen()
        }
    }
}