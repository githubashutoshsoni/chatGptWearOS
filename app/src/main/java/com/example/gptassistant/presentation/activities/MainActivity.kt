/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.gptassistant.presentation.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.gptassistant.R
import com.example.gptassistant.presentation.theme.GPTAssistantTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp("Android")
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    GPTAssistantTheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */

        var permissionGranted by remember {
            mutableStateOf(false)
        }

        if (!permissionGranted) PermissionScreen { permission ->
            permissionGranted = permission

        }
        else {
            ShowMicrophoneScreen()
        }

        /*Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                verticalArrangement = Arrangement.Center
            ) {
                Greeting(greetingName = greetingName)
            }*/
    }
}

@Composable
fun ShowMicrophoneScreen() {
    val context = LocalContext.current

    var inputText by remember {
        mutableStateOf("")
    }

    var startListeningState by remember {
        mutableStateOf(false)
    }

    val shape = RectangleShape
    Column(
        modifier = Modifier
            .fillMaxSize()

            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Icon(painter = painterResource(id = R.drawable.ic_baseline_settings_24),
            contentDescription = "For settings",
            modifier = Modifier.clickable {
                val intent = Intent(context, SettingsActivity::class.java)
                context.startActivity(intent)
            })

        if (!startListeningState) Text(text = "Click on the microphone icon to start your query...")

        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_mic_24),
            contentDescription = "mic icon",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(if (startListeningState) 48.dp else 24.dp)
                .clickable {
                    startListeningState = true
                    startSpeechToText(
                        context,
                        { inputTextFromSpeech -> inputText = inputTextFromSpeech }) {

                        Log.d("ShowMicrophoneScreen", "text to speech service stopped ")
                        startListeningState = false

                    }
                    Log.d("ShowMicrophoneScreen", "clickable ")

                },
            tint = if (startListeningState) Color.Red else Color.White

        )

        if (!startListeningState) Text(text = inputText)

        if (!inputText.isEmpty()) Button(onClick = {


        }) {
            if (!startListeningState) Text(text = "Proceed", Modifier.clickable {
                val intent = Intent(context, GPTActivity::class.java)
                context.startActivity(intent)
            })
        }
    }
}


fun startSpeechToText(ctx: Context, inputText: (String) -> Unit, finished: () -> Unit) {


    if (SpeechRecognizer.isRecognitionAvailable(ctx)) {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(ctx)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
        )

        // Optionally I have added my mother language
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el_GR")
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi")


        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle?) {
                Log.d("ShowMicrophoneScreen", "text to onReadyForSpeech ")
            }

            override fun onBeginningOfSpeech() {
                Log.d("ShowMicrophoneScreen", "text to onBeginningOfSpeech ")

            }

            override fun onRmsChanged(v: Float) {
                Log.d("ShowMicrophoneScreen", "text to onRmsChanged ")

            }

            override fun onBufferReceived(bytes: ByteArray?) {

                Log.d("ShowMicrophoneScreen", "text onBufferReceived ")
            }

            override fun onEndOfSpeech() {
                finished()
                // changing the color of your mic icon to
                // gray to indicate it is not listening or do something you want
            }

            override fun onError(i: Int) {
                Log.e("ShowMicrophoneScreen", "onError $i ")

            }

            override fun onResults(bundle: Bundle) {
                val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result != null) {
                    // attaching the output
                    // to our viewmodel
                    inputText.invoke(result[0])
                    Log.d("SpeechToText", "onResults:${result[0]} ")
//                    vm.textFromSpeech = result[0]
                }
            }

            override fun onPartialResults(bundle: Bundle) {
                Log.d("ShowMicrophoneScreen", "text onPartialResults ")
            }

            override fun onEvent(i: Int, bundle: Bundle?) {
                Log.d("ShowMicrophoneScreen", "onEvent $i ")
            }

        })
        speechRecognizer.startListening(speechRecognizerIntent)
    } else {
        finished()
        // SOME SORT OF ERROR
    }
}


@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}


@Composable
fun PermissionScreen(isPermissionGranter: (Boolean) -> Unit) {


    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something

            Log.d("ExampleScreen", "PERMISSION GRANTED")
            isPermissionGranter.invoke(true)

        } else {
            isPermissionGranter.invoke(false)
            // Permission Denied: Do something
            Log.d("ExampleScreen", "PERMISSION DENIED")
        }
    }


    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isPermissionGranter.invoke(true)
            // Some works that require permission

        }
    })

    Button(onClick = {
        // Check permission
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) -> {
                isPermissionGranter.invoke(true)
                // Some works that require permission
                Log.d("ExampleScreen", "permission granted...")
            }
            else -> {
                // Asking for permission
                launcher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }) {


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            modifier = Modifier
                .border(1.dp, Color.Green, RectangleShape)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(20.dp)
        ) {
            Text("Need microphone permission to proceed.", textAlign = TextAlign.Center)

        }


    }
}