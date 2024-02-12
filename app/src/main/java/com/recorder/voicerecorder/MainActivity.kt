package com.recorder.voicerecorder

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.recorder.voicerecorder.navigation.Navigation
import com.recorder.voicerecorder.services.recorder.RecorderService
import com.recorder.voicerecorder.ui.theme.VoiceRecorderTheme

class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var recorderService: RecorderService
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as RecorderService.RecorderServiceBinder
            recorderService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, RecorderService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            VoiceRecorderTheme {
                if (isBound) {

                    val state = recorderService.recorderState.value
                    val hours by recorderService.hours
                    val minutes by recorderService.minutes
                    val seconds by recorderService.seconds

                    Navigation(
                        navController = navController,
                        state = state,
                        hours = hours,
                        minutes = minutes,
                        seconds = seconds
                    )
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}

