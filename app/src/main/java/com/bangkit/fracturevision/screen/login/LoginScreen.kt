package com.bangkit.fracturevision.screen.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.bangkit.fracturevision.AppViewModel
import com.bangkit.fracturevision.component.TextInput

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    loginViewModel: LoginViewModel,
    navigateToHome : () -> Unit
) {
    val user by appViewModel.getUser().observeAsState()
    if(user?.isLogin == true) {
        LaunchedEffect(user) {
            navigateToHome()
        }
    } else {
        LoginContent(
            loginViewModel = loginViewModel,
            navigateToHome = navigateToHome
        )
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    navigateToHome : () -> Unit
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = "Login",
                fontSize = 24.sp,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                ),
            )
            TextInput(
                value = username,
                onValueChange = {
                    username = it
                },
                icon = Icons.Default.Person,
                placeholder = "Username"
            )
            TextInput(
                value = password,
                icon = Icons.Default.Lock,
                onValueChange = {
                    password = it
                },
                keyboardType = KeyboardType.Password,
                placeholder = "Password"
            )
            Button(onClick = {
                loginViewModel.loginApi(username, password, context, navigateToHome)
            }) {
                Text(text = "Login")
            }
        }
    }
}