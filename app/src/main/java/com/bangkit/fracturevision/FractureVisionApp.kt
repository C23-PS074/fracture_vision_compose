package com.bangkit.fracturevision

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bangkit.fracturevision.navigation.Screen
import com.bangkit.fracturevision.screen.camerax.CameraXScreen
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
    val photoUriFromCameraX = remember {
        mutableStateOf<Uri?>(null)
    }
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
        composable(
            Screen.Scan.route,
        ) {
            ScanScreen(
                photoUri = photoUriFromCameraX.value,
                navigateToCameraX = {
                    navController.navigate(Screen.Camera.route)
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
    }
}