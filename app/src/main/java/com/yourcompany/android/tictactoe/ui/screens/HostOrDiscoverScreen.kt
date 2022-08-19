package com.yourcompany.android.tictactoe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourcompany.android.tictactoe.viewmodel.TicTacToeViewModel

@Composable
fun HostOrDiscoverScreen(viewModel: TicTacToeViewModel) {
  Column(
    modifier = Modifier
      .padding(16.dp)
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = { viewModel.startHosting() }
    ) {
      Text(text = "Host")
    }
    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = { viewModel.startDiscovering() }
    ) {
      Text(text = "Discover")
    }
  }
}