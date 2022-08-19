package com.yourcompany.android.tictactoe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yourcompany.android.tictactoe.viewmodel.TicTacToeViewModel

@Composable
fun HostingScreen(
  viewModel: TicTacToeViewModel
) {
  WaitingScreen(
    title = "Hosting...",
    onStopClick = { viewModel.stopHosting() }
  )
}

@Composable
fun DiscoveringScreen(
  viewModel: TicTacToeViewModel
) {
  WaitingScreen(
    title = "Discovering...",
    onStopClick = { viewModel.stopDiscovering() }
  )
}

@Composable
fun WaitingScreen(
  title: String,
  onStopClick: () -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = title)
    CircularProgressIndicator()
    Button(
      onClick = onStopClick
    ) {
      Text(text = "Stop")
    }
  }
}