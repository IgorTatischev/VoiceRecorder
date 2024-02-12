package com.recorder.voicerecorder.services.recorder

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.recorder.voicerecorder.util.Constants.PAUSE_REQUEST_CODE
import com.recorder.voicerecorder.util.Constants.RECORDER_STATE
import com.recorder.voicerecorder.util.Constants.RESUME_REQUEST_CODE
import com.recorder.voicerecorder.util.Constants.STOP_REQUEST_CODE

class ServiceHelper(private val context: Context) {

    fun actionService(action: String) {
        Intent(context, RecorderService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }

    private val flag = PendingIntent.FLAG_IMMUTABLE

    fun stopPendingIntent(): PendingIntent {
        val stopIntent = Intent(context, RecorderService::class.java).apply {
            putExtra(RECORDER_STATE, RecorderState.Stopped.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    fun resumePendingIntent(): PendingIntent {
        val resumeIntent = Intent(context, RecorderService::class.java).apply {
            putExtra(RECORDER_STATE, RecorderState.Active.name)
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun pausePendingIntent(): PendingIntent {
        val cancelIntent = Intent(context, RecorderService::class.java).apply {
            putExtra(RECORDER_STATE, RecorderState.Paused.name)
        }
        return PendingIntent.getService(
            context, PAUSE_REQUEST_CODE, cancelIntent, flag
        )
    }
}