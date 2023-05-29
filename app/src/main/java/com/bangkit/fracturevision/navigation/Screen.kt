package com.bangkit.fracturevision.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Login: Screen("login")
    object Scan: Screen("scan")
}