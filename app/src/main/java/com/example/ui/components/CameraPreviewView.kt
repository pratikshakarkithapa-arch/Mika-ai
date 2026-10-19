package com.example.ui.components

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ui.theme.MikaSurfaceElevated
import com.example.ui.theme.MikaTextSecondary

@Composable
fun CameraPreviewView(
    modifier: Modifier = Modifier,
    isFrontCamera: Boolean = true,
    hasCameraPermission: Boolean = true
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isCameraBound by remember { mutableStateOf(false) }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    LaunchedEffect(isFrontCamera, hasCameraPermission) {
        if (!hasCameraPermission) {
            isCameraBound = false
            return@LaunchedEffect
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val cameraSelector = if (isFrontCamera) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
                isCameraBound = true
            } catch (e: Exception) {
                Log.e("CameraPreviewView", "Failed to bind camera lifecycle", e)
                isCameraBound = false
            }
        }, ContextCompat.getMainExecutor(context))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MikaSurfaceElevated),
        contentAlignment = Alignment.Center
    ) {
        if (hasCameraPermission && isCameraBound) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Elegant user placeholder avatar when camera is loading or permission pending
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Camera Preview",
                tint = MikaTextSecondary,
                modifier = Modifier.fillMaxSize(0.5f)
            )
        }
    }
}
