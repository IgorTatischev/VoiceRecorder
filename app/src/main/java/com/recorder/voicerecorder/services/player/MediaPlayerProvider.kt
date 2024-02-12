package com.recorder.voicerecorder.services.player

import android.content.Context
import java.io.File

class MediaPlayerProvider (context: Context) {

    private val recordFolder = "${context.filesDir}/"

    fun readFiles(): List<File> {
        return File(recordFolder).listFiles()?.filter { it.name.contains(".mp3") } ?: emptyList()
    }

    fun deleteFile(path: String) {
        File(path).delete()
    }
}