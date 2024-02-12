package com.recorder.voicerecorder.presentation.screens.recorder_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recorder.voicerecorder.services.Recorder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RecorderViewModel(private val recorder: Recorder) : ViewModel() {

    private lateinit var timer: Timer
    private var duration: Duration = Duration.ZERO

    private val _state = MutableStateFlow(RecorderScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event: RecorderEvent) {
        when (event) {
            RecorderEvent.StartRecording -> startRecorder()
            RecorderEvent.StopRecording -> stopRecorder()
            RecorderEvent.PauseRecording -> pauseRecorder()
            RecorderEvent.ResumeRecording -> resumeRecorder()
        }
    }

    private fun startRecorder() {
        viewModelScope.launch {
            _state.emit(_state.value.copy(record = RecorderState.Active))
            recorder.prepareRecorder()
            recorder.mediaRecorder?.start()
            startTime()
        }

    }

    private fun resumeRecorder() {
        viewModelScope.launch {
            _state.emit(_state.value.copy(record = RecorderState.Active))
            recorder.mediaRecorder?.resume()
            startTime()
        }
    }

    private fun pauseRecorder() {
        viewModelScope.launch {
            recorder.mediaRecorder?.pause()
            _state.emit(_state.value.copy(record = RecorderState.Paused))
            timer.cancel()
        }
    }

    private fun stopRecorder() {
        viewModelScope.launch {
            _state.emit(_state.value.copy(record = RecorderState.Stopped))
            duration = Duration.ZERO
            updateTimeUnits()
            recorder.mediaRecorder?.apply {
                stop()
                release()
            }
            recorder.mediaRecorder = null
        }
    }


    private fun startTime() {
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
        }
    }

    private fun updateTimeUnits() = with(_state.value) {
        duration.toComponents { hours, minutes, seconds, _ ->
            _state.value = copy(
                hours = hours.toInt().pad(),
                minutes = minutes.pad(),
                seconds = seconds.pad()
            )
        }
    }
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}