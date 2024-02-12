package com.recorder.voicerecorder.services.recorder

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.recorder.voicerecorder.R
import com.recorder.voicerecorder.util.Constants.ACTION_PAUSE_RECORDING
import com.recorder.voicerecorder.util.Constants.ACTION_RESUME_RECORDING
import com.recorder.voicerecorder.util.Constants.ACTION_START_RECORDING
import com.recorder.voicerecorder.util.Constants.ACTION_STOP_RECORDING
import com.recorder.voicerecorder.util.Constants.NOTIFICATION_CHANNEL_ID
import com.recorder.voicerecorder.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.recorder.voicerecorder.util.Constants.NOTIFICATION_ID
import com.recorder.voicerecorder.util.Constants.RECORDER_STATE
import com.recorder.voicerecorder.util.pad
import java.io.File
import java.io.FileOutputStream
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RecorderService : Service() {

    private val binder = RecorderServiceBinder()

    inner class RecorderServiceBinder : Binder() {
        fun getService(): RecorderService = this@RecorderService
    }

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var helper: ServiceHelper

    private var mediaRecorder: MediaRecorder? = null
    private lateinit var timer: Timer
    private var duration: Duration = Duration.ZERO

    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set

    var recorderState = mutableStateOf(RecorderState.Stopped)
        private set

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()

        helper = ServiceHelper(applicationContext)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(applicationContext.getString(R.string.name))
                .setContentText(applicationContext.getString(R.string.time))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .addAction(
                    0,
                    getString(R.string.pause),
                    helper.pausePendingIntent()
                )
                .addAction(
                    0,
                    getString(R.string.stop),
                    helper.stopPendingIntent()
                )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(RECORDER_STATE)) {
            RecorderState.Started.name -> start()
            RecorderState.Active.name -> resume()
            RecorderState.Paused.name -> pause()
            RecorderState.Stopped.name -> stop()
        }

        when (intent?.action) {
            ACTION_START_RECORDING -> start()

            ACTION_PAUSE_RECORDING -> pause()

            ACTION_RESUME_RECORDING -> resume()

            ACTION_STOP_RECORDING -> stop()

        }
        return START_STICKY
    }

    private fun start() {
        startRecorder()
        setPauseButton()
        startForegroundService()
        startTime { hours, minutes, seconds ->
            updateNotification(hours = hours, minutes = minutes, seconds = seconds)
        }
    }

    private fun resume() {
        resumeRecorder()
        setPauseButton()
        startTime { hours, minutes, seconds ->
            updateNotification(hours = hours, minutes = minutes, seconds = seconds)
        }
    }

    private fun pause() {
        pauseRecorder()
        setResumeButton()
    }

    private fun stop() {
        stopRecorder()
        stopForegroundService()
    }

    private fun prepareRecorder() {

        val file = File(filesDir, "recording${System.currentTimeMillis()}.mp3")

        mediaRecorder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(this)
            } else {
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(FileOutputStream(file).fd)
                try { prepare() } catch (_: Exception) { }
            }
    }

    private fun startRecorder() {
        recorderState.value = RecorderState.Active
        prepareRecorder()
        mediaRecorder?.start()
    }

    private fun resumeRecorder() {
        recorderState.value = RecorderState.Active
        mediaRecorder?.resume()
    }

    private fun pauseRecorder() {
        mediaRecorder?.pause()
        recorderState.value = RecorderState.Paused
        if (this::timer.isInitialized) {
            timer.cancel()
        }
    }

    private fun stopRecorder() {
        recorderState.value = RecorderState.Stopped
        duration = Duration.ZERO
        updateTimeUnits()
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }


    private fun startTime(onTick: (h: String, m: String, s: String) -> Unit) {
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@RecorderService.hours.value = hours.toInt().pad()
            this@RecorderService.minutes.value = minutes.pad()
            this@RecorderService.seconds.value = seconds.pad()
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                StringBuilder("$hours:$minutes:$seconds")
            ).build()
        )
    }

    @SuppressLint("RestrictedApi")
    private fun setPauseButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                this.getString(R.string.pause),
                helper.pausePendingIntent()
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    private fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                this.getString(R.string.resume),
                helper.resumePendingIntent()
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}

