package com.recorder.voicerecorder.util

object Constants {
    const val ACTION_START_RECORDING = "ACTION_START_RECORDING"
    const val ACTION_STOP_RECORDING = "ACTION_STOP_RECORDING"
    const val ACTION_RESUME_RECORDING = "ACTION_RESUME_RECORDING"
    const val ACTION_PAUSE_RECORDING = "ACTION_PAUSE_RECORDING"

    const val RECORDER_STATE = "RECORDER_STATE"

    const val NOTIFICATION_CHANNEL_ID = "RECORDER_NOTIFICATION_ID"
    const val NOTIFICATION_CHANNEL_NAME = "RECORDER_NOTIFICATION"
    const val NOTIFICATION_ID = 10

    const val STOP_REQUEST_CODE = 101
    const val PAUSE_REQUEST_CODE = 102
    const val RESUME_REQUEST_CODE = 103
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}
