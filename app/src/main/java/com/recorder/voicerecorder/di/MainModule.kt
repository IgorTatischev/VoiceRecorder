package com.recorder.voicerecorder.di

import com.recorder.voicerecorder.presentation.screens.player_screen.PlayerViewModel
import com.recorder.voicerecorder.presentation.screens.recorder_screen.RecorderViewModel
import com.recorder.voicerecorder.services.player.MediaPlayerProvider
import com.recorder.voicerecorder.services.player.RecordingPlayer
import com.recorder.voicerecorder.services.recorder.ServiceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object MainModule {
    val module = module {

        single { MediaPlayerProvider(context = androidContext()) }
        single { RecordingPlayer(context = androidContext()) }
        single { ServiceHelper(context = androidContext()) }

        viewModelOf(::RecorderViewModel)
        viewModelOf(::PlayerViewModel)
    }
}