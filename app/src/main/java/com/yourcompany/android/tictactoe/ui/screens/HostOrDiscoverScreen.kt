package com.yourcompany.android.tictactoe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourcompany.android.tictactoe.routing.Screen
import com.yourcompany.android.tictactoe.routing.TicTacToeRouter

@Composable
fun HostOrDiscoverScreen() {
  Column(
    modifier = Modifier
      .padding(16.dp)
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = { TicTacToeRouter.navigateTo(Screen.Hosting) }
    ) {
      Text(text = "Host")
    }
    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = { TicTacToeRouter.navigateTo(Screen.Discovering) }
    ) {
      Text(text = "Discover")
    }
  }
}

@Preview
@Composable
fun HostOrDiscoverScreenPreview() {
  HostOrDiscoverScreen()
}