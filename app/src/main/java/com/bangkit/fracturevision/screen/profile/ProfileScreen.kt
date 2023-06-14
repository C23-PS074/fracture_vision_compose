package com.bangkit.fracturevision.screen.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.fracturevision.AppViewModel
import com.bangkit.fracturevision.component.LoadingCircular
import com.bangkit.fracturevision.model.User
import com.bangkit.fracturevision.screen.home.HomeContent
import com.bangkit.fracturevision.ui.theme.FractureVisionTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    navigateToLogin : () -> Unit,
) {
    val user by appViewModel.getUser().observeAsState()

    if(user?.isLogin == false){
        LaunchedEffect(user) {
            navigateToLogin()
        }
    } else {
        ProfileContent(
            appViewModel = appViewModel,
            user = user
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    user: User?,
) {
    var isLoading by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                        ),
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) {
        innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                Text(text = user?.fullname.toString())
                Text(text = user?.phone.toString())
                Text(text = user?.address.toString())
                Text(text = user?.id.toString())

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