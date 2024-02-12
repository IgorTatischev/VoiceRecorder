package com.recorder.voicerecorder.presentation.screens.player_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recorder.voicerecorder.services.RecordingPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.min

class PlayerViewModel(private val player: RecordingPlayer) : ViewModel() {

    private val _state = MutableStateFlow(PlayerScreenState())
    val state = _state.asStateFlow()

    init {
        getAudioList()
    }

    private fun getAudioList() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.emit(_state.value.copy(audios = player.readFiles()))
        }
    }

    fun deleteAudio(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            player.deleteFile(path)
            val list = _state.value.audios.toMutableList().apply {
                remove(File(path))
            }
            _state.emit(_state.value.copy(audios = list))
        }
    }

    fun initMediaPlayer(path: String) {
        viewModelScope.launch {
            player.prepareMediaPlayer(path)
            player.mediaPlayer.start()
            _state.emit(_state.value.copy(isPlaying = true))
        }

        player.mediaPlayer.setOnCompletionListener {
            viewModelScope.launch {
                _state.emit(_state.value.copy(isPlaying = false))
            }
        }
    }

    fun rewindTenSeconds() {
        player.mediaPlayer.seekTo(player.mediaPlayer.currentPosition - 10000)
    }

    fun forwardTenSeconds() {
        player.mediaPlayer.seekTo(
            player.mediaPlayer.currentPosition + (min(
                10000,
                player.mediaPlayer.duration - player.mediaPlayer.currentPosition
            ))
        )
    }

    fun playMedia() {
        viewModelScope.launch {
            player.mediaPlayer.start()
            _state.emit(_state.value.copy(isPlaying = true))
        }
    }

    fun pauseMedia() {
        viewModelScope.launch {
            player.mediaPlayer.pause()
            _state.emit(_state.value.copy(isPlaying = false))
        }
    }
}

