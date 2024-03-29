package com.recorder.voicerecorder.services

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class RecordingPlayer(private val context: Context) {

    val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    suspend fun prepareMediaPlayer(path: String): MediaPlayer {
        return withContext(Dispatchers.IO) {
            mediaPlayer.apply {
                stop()
                reset()
                setDataSource(context, Uri.parse(path))
                prepare()
            }
            mediaPlayer
        }
    }

    private val recordFolder = "${context.filesDir}/"

    fun readFiles(): List<File> {
        return File(recordFolder).listFiles()?.filter { it.name.contains(".mp3") } ?: emptyList()
    }

    fun deleteFile(path: String) {
        File(path).delete()
    }
}