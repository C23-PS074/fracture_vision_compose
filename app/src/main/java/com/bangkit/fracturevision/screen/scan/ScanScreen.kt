package com.bangkit.fracturevision.screen.scan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bangkit.fracturevision.AppViewModel
import com.bangkit.fracturevision.PredictViewModel
import com.bangkit.fracturevision.R
import com.bangkit.fracturevision.ScanApiStatus
import com.bangkit.fracturevision.component.LoadingCircular
import com.bangkit.fracturevision.utils.uriToFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    predictViewModel: PredictViewModel,
    photoUri: Uri?,
    navigateToCameraX: () -> Unit,
    navigateToResult: () -> Unit
) {
    val user by appViewModel.getUser().observeAsState()
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
    val isLoading by predictViewModel.status.observeAsState()
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
                OutlinedButton(
                    modifier = modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                    ,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                    onClick = {
                        checkRequestCameraPermission(
                            context,
                            permission,
                            cameraX,
                            navigateToCameraX
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "camera",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = modifier.padding(horizontal = 5.dp))
                    Text(text = "Take Photo")
                }
                OutlinedButton(
                    modifier = modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                    onClick = {
                        imagePicker.launch("image/*")
                    }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_image),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "gallery"
                    )
                    Spacer(modifier = modifier.padding(horizontal = 5.dp))
                    Text(text = "Gallery")
                }
            }
            if (isLoading == ScanApiStatus.LOADING) {
                LoadingCircular(
                    modifier = modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    if (user != null) {
                        if (imageUri != null) {
                            val imgFile = uriToFile(imageUri!!, context)
                            val compressedFile = reduceFileImage(imgFile)
                            predictViewModel.uploadImage(
                                id = user!!.id,
                                file = compressedFile,
                                context = context,
                                navigateToResult = navigateToResult
                            )
                        }
                    }
                }
            ) {
                Text(text = "Get Result")
            }
            Text(
                modifier = modifier.padding(vertical = 16.dp),
                text = "*Scanning can take 1 - 2 minutes or longer.",
                fontSize = 11.sp,
                color = Color(0xFFDE3969),
                fontWeight = FontWeight.Bold
            )

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

private fun reduceFileImage(file: File) : File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

