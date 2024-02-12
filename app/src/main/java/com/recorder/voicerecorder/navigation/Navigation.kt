package com.recorder.voicerecorder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.recorder.voicerecorder.presentation.screens.player_screen.PlayerScreen
import com.recorder.voicerecorder.presentation.screens.recorder_screen.RecorderScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Recorder.route
    ) {

        composable(route = Screens.Recorder.route) {
            RecorderScreen(navigate = { navController.navigate(Screens.Player.route) })
        }

        composable(route = Screens.Player.route) {
            PlayerScreen(onBackPressed = { navController.popBackStack() })
        }
    }
}