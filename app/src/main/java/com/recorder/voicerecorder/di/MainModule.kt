package com.recorder.voicerecorder.di

import com.recorder.voicerecorder.presentation.screens.player_screen.PlayerViewModel
import com.recorder.voicerecorder.presentation.screens.recorder_screen.RecorderViewModel
import com.recorder.voicerecorder.services.player.MediaPlayerProvider
import com.recorder.voicerecorder.services.player.RecordingPlayer
import com.recorder.voicerecorder.services.recorder.ServiceHelper
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object MainModule {
    operator fun invoke() = module {
        singleOf(::MediaPlayerProvider)
        singleOf(::RecordingPlayer)
        singleOf(::ServiceHelper)
        viewModelOf(::RecorderViewModel)
        viewModelOf(::PlayerViewModel)
    }
}