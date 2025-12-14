package com.kaushal.app.genai

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.mlkit.genai.common.DownloadCallback
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import com.google.mlkit.genai.imagedescription.ImageDescriber
import com.google.mlkit.genai.imagedescription.ImageDescriberOptions
import com.google.mlkit.genai.imagedescription.ImageDescription
import com.google.mlkit.genai.imagedescription.ImageDescriptionRequest
import kotlinx.coroutines.launch
import com.kaushal.app.genai.ui.theme.GenAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GenAITheme {
                ImageDescriptionScreen()
            }
        }
    }
}

@Composable
fun ImageDescriptionScreen() {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var imageDescriber by remember { mutableStateOf<ImageDescriber?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
        description = ""
        if (uri != null) {
            isLoading = true
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val options = ImageDescriberOptions.builder(context).build()
            val describer = ImageDescription.getClient(options)
            imageDescriber = describer // Set for cleanup
            coroutineScope.launch {
                val featureStatus = describer.checkFeatureStatus().get()
                if (featureStatus == FeatureStatus.DOWNLOADABLE) {
                    describer.downloadFeature(object : DownloadCallback {
                        override fun onDownloadStarted(bytesToDownload: Long) { }
                        override fun onDownloadFailed(e: GenAiException) {
                            description = "Download failed: ${e.message}"
                            isLoading = false
                        }
                        override fun onDownloadProgress(totalBytesDownloaded: Long) { }
                        override fun onDownloadCompleted() {
                            startImageDescriptionRequest(bitmap, describer, onResult = {
                                description = it
                                isLoading = false
                            })
                        }
                    })
                } else if (featureStatus == FeatureStatus.DOWNLOADING) {
                    startImageDescriptionRequest(bitmap, describer, onResult = {
                        description = it
                        isLoading = false
                    })
                } else if (featureStatus == FeatureStatus.AVAILABLE) {
                    startImageDescriptionRequest(bitmap, describer, onResult = {
                        description = it
                        isLoading = false
                    })
                } else {
                    description = "Feature unavailable."
                    isLoading = false
                }
            }
        }
    }
    DisposableEffect(imageDescriber) {
        onDispose {
            imageDescriber?.close()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Pick Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
        imageUri?.let { uri ->
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }
        }
        if (isLoading) {
            CircularProgressIndicator()
        } else if (description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(description)
        }
    }
}

private fun startImageDescriptionRequest(
    bitmap: android.graphics.Bitmap,
    imageDescriber: ImageDescriber,
    onResult: (String) -> Unit
) {
    val imageDescriptionRequest = ImageDescriptionRequest.builder(bitmap).build()
    val stringBuilder = StringBuilder()
    imageDescriber.runInference(imageDescriptionRequest) { outputText ->
        stringBuilder.append(outputText)
        onResult(stringBuilder.toString())
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GenAITheme {
        Greeting("Android")
    }
}