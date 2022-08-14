package com.yourcompany.android.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.yourcompany.android.tictactoe.routing.Screen
import com.yourcompany.android.tictactoe.routing.TicTacToeRouter
import com.yourcompany.android.tictactoe.ui.screens.GameScreen
import com.yourcompany.android.tictactoe.ui.screens.HostOrDiscoverScreen
import com.yourcompany.android.tictactoe.ui.screens.WaitingScreen
import com.yourcompany.android.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TicTacToeTheme {
        MainActivityScreen()
      }
    }
  }

  @Composable
  private fun MainActivityScreen() {
    Surface {
      when (TicTacToeRouter.currentScreen) {
        is Screen.HostOrDiscover -> HostOrDiscoverScreen()
        is Screen.Waiting -> WaitingScreen()
        is Screen.Game -> GameScreen()
      }
    }
  }
}