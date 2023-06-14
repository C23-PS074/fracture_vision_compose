package com.bangkit.fracturevision.screen.camerax

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.bangkit.fracturevision.R
import com.bangkit.fracturevision.utils.createFile
import java.lang.Exception
import java.util.concurrent.Executor

@Composable
fun CameraXScreen(
    modifier: Modifier = Modifier,
    onImageCaptured : (Uri) -> Unit,
    navigateToScan : () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val executor = ContextCompat.getMainExecutor(context)
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().build()
    }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AndroidView(factory = {
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Failed to find Camera!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, executor)
            previewView
        },
            modifier = modifier.fillMaxSize(),
        )
        IconButton(
            onClick = {
                takePhoto(
                    context = context,
                    imageCapture = imageCapture,
                    executor = executor,
                    onImageCaptured = onImageCaptured,
                    navigateToScan = navigateToScan
                )
            },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera_48),
                    contentDescription = "Take Picture",
                    tint = Color.White
                )
            }
        )
    }
}

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    navigateToScan: () -> Unit
) {
    val photoFile = createFile(context.applicationContext as Application)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    imageCapture.takePicture(outputOptions, executor, object: ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Log.d("URI", exception.toString())
            Toast.makeText(
                context,
                "Failed to take a picture!",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            onImageCaptured(savedUri)
            navigateToScan()
        }
    })
}

