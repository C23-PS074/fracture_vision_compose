package com.bangkit.fracturevision.screen.profile

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bangkit.fracturevision.AppViewModel
import com.bangkit.fracturevision.component.LoadingCircular
import com.bangkit.fracturevision.screen.home.HomeContent

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    navigateToLogin : () -> Unit,
) {
    val user by appViewModel.getUser().observeAsState()
    var isLoading by rememberSaveable { mutableStateOf(false) }

    if(user?.isLogin == false){
        LaunchedEffect(user) {
            navigateToLogin()
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = "Profile Screen",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                )
                Text(text = user?.username.toString())
                Button(onClick = {
                    isLoading = true
                    appViewModel.logout()
                }) {
                    Text(text = "Logout")
                }
                if (isLoading) {
                    LoadingCircular()
                }
            }
        }
    }
}