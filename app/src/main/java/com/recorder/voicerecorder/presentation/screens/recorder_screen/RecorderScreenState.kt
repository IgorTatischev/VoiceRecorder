package com.recorder.voicerecorder.presentation.screens.recorder_screen


data class RecorderScreenState(
    val record: RecorderState = RecorderState.Stopped,
    val hours: String = "00",
    val minutes: String = "00",
    val seconds: String = "00"
)

enum class RecorderState {
    Stopped,
    Active,
    Paused,
}

sealed class RecorderEvent {
    object StartRecording : RecorderEvent()
    object StopRecording : RecorderEvent()
    object ResumeRecording : RecorderEvent()
    object PauseRecording : RecorderEvent()
}

