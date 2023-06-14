package com.bangkit.fracturevision.screen.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.fracturevision.component.LoadingCircular
import com.bangkit.fracturevision.component.TextInput

@Composable
fun RegisterScreen(
    modifier : Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel(),
    navigateToLogin: () -> Unit
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var fullname by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val isLoading by registerViewModel.status.observeAsState()
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Sign Up",
                fontSize = 24.sp,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                ),
            )
            TextInput(
                modifier = modifier.clip(
                    RoundedCornerShape(8.dp)
                ),
                value = username,
                onValueChange = {
                    username = it
                },
                icon = Icons.Default.Person,
                placeholder = "Username"
            )
            TextInput(
                modifier = modifier.clip(
                    RoundedCornerShape(8.dp)
                ),
                value = password,
                icon = Icons.Default.Lock,
                onValueChange = {
                    password = it
                },
                keyboardType = KeyboardType.Password,
                placeholder = "Password"
            )
            TextInput(
                modifier = modifier.clip(
                    RoundedCornerShape(8.dp)
                ),
                value = fullname,
                onValueChange = {
                    fullname = it
                },
                icon = Icons.Default.Info,
                placeholder = "Fullname"
            )
            TextInput(
                modifier = modifier.clip(
                    RoundedCornerShape(8.dp)
                ),
                value = phone,
                icon = Icons.Default.Phone,
                onValueChange = {
                    phone = it
                },
                keyboardType = KeyboardType.Number,
                placeholder = "Phone Number"
            )
            TextInput(
                modifier = modifier.clip(
                    RoundedCornerShape(8.dp)
                ),
                value = address,
                onValueChange = {
                    address = it
                },
                icon = Icons.Default.LocationOn,
                placeholder = "Address"
            )
            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    registerViewModel.registerApi(
                        username,
                        password,
                        fullname,
                        phone,
                        address,
                        context,
                        navigateToLogin
                    )
                },
                enabled = isLoading != RegisterApiStatus.LOADING
            ) {
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                )
            }
            ClickableText(
                text = AnnotatedString("Already have account? Login")
            ) {
                navigateToLogin()
            }
            if (isLoading == RegisterApiStatus.LOADING) {
                LoadingCircular()
            }
        }
    }
}