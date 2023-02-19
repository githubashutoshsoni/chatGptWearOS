/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.gptassistant.presentation.activities

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text

import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.RemoteInputIntentHelper.Companion.putRemoteInputsExtra
import com.example.gptassistant.presentation.viewmodels.MainViewModel

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextInput()


        }
    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun TextInput(mainViewModel: MainViewModel = viewModel()) {
    val label = remember { mutableStateOf("Start") }


    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                val results: Bundle = RemoteInput.getResultsFromIntent(data)
                val ipAddress: CharSequence? = results.getCharSequence("ip_address")
                label.value = ipAddress.toString()
                Log.d("TextInputToken", "TextInput: ${ipAddress.toString()}")
                mainViewModel.saveToken(ipAddress.toString())
            }
        }
    Column() {
        Spacer(modifier = Modifier.height(20.dp))
        Chip(
            label = { Text(label.value) },
            onClick = {}
        )
        Chip(
            label = { Text("Search with specific IP") },
            onClick = {
                val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent();
                val remoteInputs: List<RemoteInput> = listOf(
                    RemoteInput.Builder("ip_address")
                        .setLabel("Manual IP Entry")
                        .build()
                )

                RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)

                launcher.launch(intent)
            }
        )
    }
}



