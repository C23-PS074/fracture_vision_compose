package com.bangkit.fracturevision.screen.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangkit.fracturevision.AppViewModel
import com.bangkit.fracturevision.component.LoadingCircular
import com.bangkit.fracturevision.component.ProfileItemInfo
import com.bangkit.fracturevision.model.User

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
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ){
                Text(
                    modifier = modifier.padding(bottom = 20.dp),
                    text = "User Profile",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                )
                Card (
                    modifier = modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    ProfileItemInfo(
                        itemName = "Fullname",
                        value = user?.fullname.toString(),
                        isHaveDivider = true
                    )
                    ProfileItemInfo(
                        itemName = "Username",
                        value = user?.username.toString(),
                        isHaveDivider = true
                    )
                    ProfileItemInfo(
                        itemName = "Phone",
                        value = user?.phone.toString(),
                        isHaveDivider = true
                    )
                    ProfileItemInfo(
                        itemName = "Address",
                        value = user?.address.toString(),
                        isHaveDivider = false
                    )
                }
                Button(
                    modifier = modifier
                        .padding(vertical = 40.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFDE3969)),
                    onClick = {
                        isLoading = true
                        appViewModel.logout()
                    }
                ) {
                    Text(text = "Logout")
                }
                if (isLoading) {
                    LoadingCircular()
                }
            }
        }
    }
}