package com.bangkit.fracturevision.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bangkit.fracturevision.AppViewModel
import com.bangkit.fracturevision.model.User

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    navigateToLogin : () -> Unit,
    navigateToScan : () -> Unit
) {
    val user by appViewModel.getUser().observeAsState()
    if(user?.isLogin == false){
        LaunchedEffect(user) {
            navigateToLogin()
        }
    } else {
        HomeContent(
            user = user,
            appViewModel = appViewModel,
            navigateToScan = navigateToScan
        )
    }

}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    user: User?,
    appViewModel: AppViewModel,
    navigateToScan : () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = "Home Screen",
                fontSize = 24.sp,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                ),
            )
            Button(onClick = {
                navigateToScan()
            }) {
                Text(text = "Scan")
            }
            Button(onClick = {
                appViewModel.logout()
            }) {
                Text(text = "Logout")
            }
            Text(
                text = user?.isLogin.toString()
            )
        }
    }
}