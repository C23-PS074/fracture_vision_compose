package com.bangkit.fracturevision.screen.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bangkit.fracturevision.PredictViewModel
import com.bangkit.fracturevision.R
import com.bangkit.fracturevision.ui.theme.FractureVisionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    predictViewModel: PredictViewModel
) {
    val data by predictViewModel.data.observeAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Fracture Detection",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                        ),
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ){ innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(24.dp),
        ) {
            AsyncImage(
                modifier = modifier
                    .fillMaxSize()
                    .height(320.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data?.image_path)
                    .build(),
                contentDescription = "Image",
                placeholder = painterResource(id = R.drawable.ic_image),
                error = painterResource(id = R.drawable.ic_image),
            )
            Text(
                modifier = modifier.fillMaxWidth(),
                text = "Result",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            if(data != null) {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    text = data!!.prediction,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    text = "Failed to get result.",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
@Preview
fun ResultScreenPreview() {
    FractureVisionTheme {
        ResultScreen(
            predictViewModel = viewModel()
        )
    }
}