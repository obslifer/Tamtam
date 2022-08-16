package com.yourcompany.android.tictactoe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yourcompany.android.tictactoe.domain.model.GameState
import com.yourcompany.android.tictactoe.domain.model.TicTacToe

class TicTacToeViewModel : ViewModel() {
  private var game = TicTacToe()

  private val _state = MutableLiveData(
    GameState(game.playerTurn, game.playerWon, game.isOver, game.board)
  )
  val state: LiveData<GameState> = _state

  fun newGame() {
    game = TicTacToe()
    _state.value = GameState(game.playerTurn, game.playerWon, game.isOver, game.board)
  }

  fun play(player: Int, position: Pair<Int, Int>) {
    game.play(player, position)
    _state.value = GameState(game.playerTurn, game.playerWon, game.isOver, game.board)
  }
}