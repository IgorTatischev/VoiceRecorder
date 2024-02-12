package com.recorder.voicerecorder.di

import com.recorder.voicerecorder.presentation.screens.player_screen.PlayerViewModel
import com.recorder.voicerecorder.presentation.screens.recorder_screen.RecorderViewModel
import com.recorder.voicerecorder.services.Recorder
import com.recorder.voicerecorder.services.RecordingPlayer
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object MainModule {
    operator fun invoke() = module {
        singleOf(::Recorder)
        singleOf(::RecordingPlayer)
        viewModelOf(::RecorderViewModel)
        viewModelOf(::PlayerViewModel)
    }
}