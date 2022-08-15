package com.yourcompany.android.tictactoe

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.yourcompany.android.tictactoe.routing.Screen
import com.yourcompany.android.tictactoe.routing.TicTacToeRouter
import com.yourcompany.android.tictactoe.ui.screens.GameScreen
import com.yourcompany.android.tictactoe.ui.screens.HostOrDiscoverScreen
import com.yourcompany.android.tictactoe.ui.screens.WaitingScreen
import com.yourcompany.android.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {

  override fun onStart() {
    super.onStart()
    if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
      requestPermissions(
        REQUIRED_PERMISSIONS,
        REQUEST_CODE_REQUIRED_PERMISSIONS
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

  override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<String>, grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) return
    grantResults.forEach { grantResult ->
      if (grantResult == PackageManager.PERMISSION_DENIED) {
        Toast.makeText(this, "Required permissions needed", Toast.LENGTH_LONG).show()
        finish()
        return
      }
    }
    recreate()
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
        is Screen.Game -> GameScreen()
      }
    }
  }

  companion object {
    private const val REQUEST_CODE_REQUIRED_PERMISSIONS = 149
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