package com.yourcompany.android.tictactoe

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.google.android.gms.nearby.Nearby
import com.yourcompany.android.tictactoe.routing.Screen
import com.yourcompany.android.tictactoe.routing.MessagingRouter
import com.yourcompany.android.tictactoe.ui.screens.DiscoveringScreen
import com.yourcompany.android.tictactoe.ui.screens.ChatScreen
import com.yourcompany.android.tictactoe.ui.screens.HomeScreen
import com.yourcompany.android.tictactoe.ui.screens.HostingScreen
import com.yourcompany.android.tictactoe.ui.screens.UsernameScreen
import com.yourcompany.android.tictactoe.ui.theme.TicTacToeTheme
import com.yourcompany.android.tictactoe.viewmodel.MessagingViewModel
import com.yourcompany.android.tictactoe.viewmodel.TicTacToeViewModelFactory

class MainActivity : ComponentActivity() {

  private val viewModel: MessagingViewModel by viewModels {
    TicTacToeViewModelFactory(Nearby.getConnectionsClient(applicationContext))
  }

  private val requestMultiplePermissions = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    if (permissions.entries.any { !it.value }) {
      Toast.makeText(this, "Required permissions needed", Toast.LENGTH_LONG).show()
      finish()
    } else {
      recreate()
    }
  }

  override fun onStart() {
    super.onStart()
    if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
      requestMultiplePermissions.launch(REQUIRED_PERMISSIONS)
    }
  }

  private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
    return permissions.isEmpty() || permissions.all {
      ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
  }

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
      when (MessagingRouter.currentScreen) {
        is Screen.Username -> UsernameScreen(viewModel) {
          // Naviguez vers l'écran d'accueil ou tout autre écran nécessaire
          MessagingRouter.navigateTo(Screen.Home)
        }
        is Screen.Home -> HomeScreen(viewModel)
        is Screen.Hosting -> HostingScreen(viewModel)
        is Screen.Discovering -> DiscoveringScreen(viewModel)
        is Screen.Chat -> ChatScreen(viewModel)
      }
    }
  }

  private companion object {
    val REQUIRED_PERMISSIONS =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
          Manifest.permission.BLUETOOTH_SCAN,
          Manifest.permission.BLUETOOTH_ADVERTISE,
          Manifest.permission.BLUETOOTH_CONNECT,
          Manifest.permission.ACCESS_FINE_LOCATION
        )
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
      } else {
        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
      }
  }
}
