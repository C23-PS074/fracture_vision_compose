package com.bangkit.fracturevision.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Register: Screen("register")
    object Login: Screen("login")
    object Scan: Screen("scan")
    object Camera: Screen("camera")
    object Profile: Screen("profile")
    object Result: Screen("result")
}