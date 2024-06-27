package com.yourcompany.android.tictactoe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.nearby.connection.ConnectionsClient

class TicTacToeViewModelFactory(private val connectionsClient: ConnectionsClient) :
  ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(MessagingViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return MessagingViewModel(connectionsClient) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}