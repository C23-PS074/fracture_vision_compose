package com.bangkit.fracturevision.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.fracturevision.AppViewModel
import com.bangkit.fracturevision.component.LoadingCircular
import com.bangkit.fracturevision.component.RecordItemCard
import com.bangkit.fracturevision.model.RecordResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    homeViewModel: HomeViewModel = viewModel(),
    navigateToLogin : () -> Unit,
) {
    val user by appViewModel.getUser().observeAsState()
    val datarecord by homeViewModel.datarecord.observeAsState()
    val isLoading by homeViewModel.status.observeAsState()
    if(user?.isLogin == false){
        LaunchedEffect(user) {
            navigateToLogin()
        }
    } else {
        LaunchedEffect(Unit){
            homeViewModel.getRecord(user?.id)
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Home",
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
            if (datarecord != null) {
                datarecord?.let {
                    HomeContent(
                        modifier = modifier,
                        record = it,
                        padding = innerPadding
                    )
                }
            } else {
                if(isLoading == HomeApiStatus.LOADING) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingCircular()
                    }
                } else if (isLoading == HomeApiStatus.ERROR) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Failed To Load Data.")
                    }
                }

            }

        }

    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    record: RecordResponse,
    padding: PaddingValues
) {
    if (record.datarecord.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Empty Record.")
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(record.datarecord, key = { it.id }) { data ->
                    RecordItemCard(
                        data = data
                    )
                }
            }
        }
    }
}