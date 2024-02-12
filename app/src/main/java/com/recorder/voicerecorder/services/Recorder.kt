package com.recorder.voicerecorder.services

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class Recorder(private val context: Context) {

    var mediaRecorder: MediaRecorder? = null

    private fun initRecorder() {
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
    }

    suspend fun prepareRecorder(): MediaRecorder? {
        return withContext(Dispatchers.IO) {

            if (mediaRecorder == null) {
                initRecorder()
            }
            val file = File(context.filesDir, "recording${System.currentTimeMillis()}.mp3")
            mediaRecorder?.apply {
                setOutputFile(FileOutputStream(file).fd)
                prepare()
            }
            mediaRecorder
        }
    }
}