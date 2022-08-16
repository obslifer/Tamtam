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
import com.yourcompany.android.tictactoe.routing.Screen
import com.yourcompany.android.tictactoe.routing.TicTacToeRouter
import com.yourcompany.android.tictactoe.ui.screens.GameScreen
import com.yourcompany.android.tictactoe.ui.screens.HostOrDiscoverScreen
import com.yourcompany.android.tictactoe.ui.screens.WaitingScreen
import com.yourcompany.android.tictactoe.ui.theme.TicTacToeTheme
import com.yourcompany.android.tictactoe.viewmodel.TicTacToeViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: TicTacToeViewModel by viewModels()

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
      requestMultiplePermissions.launch(
        REQUIRED_PERMISSIONS
      )
    }
  }

  private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
    return permissions.all {
      ContextCompat.checkSelfPermission(
        context,
        it
      ) == PackageManager.PERMISSION_GRANTED
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
      when (TicTacToeRouter.currentScreen) {
        is Screen.HostOrDiscover -> HostOrDiscoverScreen()
        is Screen.Hosting -> WaitingScreen("Hosting...")
        is Screen.Discovering -> WaitingScreen("Discovering...")
        is Screen.Game -> GameScreen(viewModel)
      }
    }
  }

  companion object {
    private val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION
      )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION
      )
    } else {
      arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION
      )
    }
  }
}