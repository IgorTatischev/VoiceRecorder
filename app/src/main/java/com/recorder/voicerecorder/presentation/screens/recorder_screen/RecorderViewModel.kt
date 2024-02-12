package com.recorder.voicerecorder.presentation.screens.recorder_screen

import androidx.lifecycle.ViewModel
import com.recorder.voicerecorder.services.recorder.ServiceHelper


class RecorderViewModel(private val recorder: ServiceHelper) : ViewModel() {

    fun serviceEvent(event: String) {
        recorder.actionService(event)
    }
}




