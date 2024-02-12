package com.recorder.voicerecorder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.recorder.voicerecorder.presentation.screens.player_screen.PlayerScreen
import com.recorder.voicerecorder.presentation.screens.recorder_screen.RecorderScreen
import com.recorder.voicerecorder.services.recorder.RecorderState

@Composable
fun Navigation(
    navController: NavHostController,
    state: RecorderState,
    hours: String,
    minutes: String,
    seconds: String
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Recorder.route
    ) {

        composable(route = Screens.Recorder.route) {
            RecorderScreen(
                state = state,
                hours = hours,
                minutes = minutes,
                seconds = seconds,
                navigate = { navController.navigate(Screens.Player.route) }
            )
        }

        composable(route = Screens.Player.route) {
            PlayerScreen(onBackPressed = { navController.popBackStack() })
        }
    }
}