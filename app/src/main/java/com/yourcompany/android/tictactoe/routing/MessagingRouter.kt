package com.yourcompany.android.tictactoe.routing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Class defining all possible screens in the app.
 */
sealed class Screen {
  object Home : Screen()
  object Hosting : Screen()
  object Discovering : Screen()
  object Chat : Screen()
  object Username : Screen()
}

/**
 * Allows you to change the screen in the [MainActivity]
 *
 * Also keeps track of the current screen.
 */
object MessagingRouter {
  var currentScreen: Screen by mutableStateOf(Screen.Username)

  fun navigateTo(destination: Screen) {
    currentScreen = destination
  }
}