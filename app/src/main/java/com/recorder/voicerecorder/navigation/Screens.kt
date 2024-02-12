package com.recorder.voicerecorder.navigation


sealed class Screens(val route: String) {
    object Recorder : Screens(
        route = "recorder"
    )

    object Player : Screens(
        route = "player"
    )
}
