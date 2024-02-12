package com.recorder.voicerecorder.presentation.screens.player_screen

import java.io.File

data class PlayerScreenState(
    val audios: List<File> = emptyList(),
    val isPlaying: Boolean = false,
)