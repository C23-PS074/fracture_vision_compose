package com.bangkit.fracturevision.screen.scan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bangkit.fracturevision.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    modifier: Modifier = Modifier,
    photoUri: Uri?,
    navigateToCameraX: () -> Unit
) {
    var imageUri by rememberSaveable {
        mutableStateOf(photoUri)
    }
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
    }
    val cameraX = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {
        isGranted ->
            if (isGranted) {
                navigateToCameraX()
            } else {
                Toast.makeText(context, "Need Permission", Toast.LENGTH_SHORT).show()
            }
    }
    val permission = Manifest.permission.CAMERA
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
    ){
        innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            AsyncImage(
                modifier = modifier
                    .fillMaxSize()
                    .height(320.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
                    .build(),
                contentDescription = "Image",
                placeholder = painterResource(id = R.drawable.ic_image),
                error = painterResource(id = R.drawable.ic_image),
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    modifier = modifier.weight(1f),
                    onClick = {
                        checkRequestCameraPermission(
                            context,
                            permission,
                            cameraX,
                            navigateToCameraX
                        )
                    }
                ) {
                    Text(text = "Take Photo")
                }
                Button(
                    modifier = modifier.weight(1f),
                    onClick = {
                        imagePicker.launch("image/*")
                    }
                ) {
                    Text(text = "Gallery")
                }
            }

            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    Toast.makeText(context, "$imageUri", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(text = "Get Result")
            }
        }
    }
}

fun checkRequestCameraPermission(
    context: Context,
    permission: String,
    cameraX: ManagedActivityResultLauncher<String, Boolean>,
    navigateToCameraX: () -> Unit
) {
    val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        navigateToCameraX()
    } else {
        cameraX.launch(permission)
    }
}

