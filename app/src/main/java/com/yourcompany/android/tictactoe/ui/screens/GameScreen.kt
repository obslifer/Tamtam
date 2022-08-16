package com.yourcompany.android.tictactoe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.yourcompany.android.tictactoe.domain.model.GameState
import com.yourcompany.android.tictactoe.viewmodel.TicTacToeViewModel

@Composable
fun GameScreen(viewModel: TicTacToeViewModel) {
  val state: GameState by viewModel.state.observeAsState(GameState.Uninitialized)

  if (state.isOver) {
    GameOverScreen(
      playerWon = state.playerWon,
      onNewGameClick = { viewModel.newGame() }
    )
  } else {
    OngoingGameScreen(
      playerTurn = state.playerTurn,
      board = state.board,
      onBucketClick = { position -> viewModel.play(state.playerTurn, position) }
    )
  }
}

@Composable
fun GameOverScreen(
  playerWon: Int,
  onNewGameClick: () -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (playerWon > 0) {
      Text(text = "Player $playerWon won!")
    } else {
      Text(text = "It's a tie!")
    }
    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = onNewGameClick
    ) {
      Text(text = "New game!")
    }
  }
}

@Composable
fun OngoingGameScreen(
  playerTurn: Int,
  board: Array<Array<Int>>,
  onBucketClick: (position: Pair<Int, Int>) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = "Player Turn: $playerTurn")
    Board(
      board = board,
      onBucketClick = { position -> onBucketClick(position) }
    )
  }
}

@Composable
fun Board(
  board: Array<Array<Int>>,
  onBucketClick: (position: Pair<Int, Int>) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxSize()
  ) {
    for (i in board.indices) {
      Column(modifier = Modifier.weight(1f)) {
        for (j in board.indices) {
          Bucket(
            modifier = Modifier
              .fillMaxSize()
              .weight(1f),
            player = board[i][j],
            onClick = { onBucketClick(i to j) }
          )
        }
      }
    }
  }
}

@Composable
fun Bucket(
  modifier: Modifier,
  player: Int,
  onClick: () -> Unit
) {
  OutlinedButton(
    modifier = modifier,
    colors = ButtonDefaults.buttonColors(getPlayerColor(player)),
    onClick = onClick
  ) {}
}

private fun getPlayerColor(player: Int): Color {
  return when (player) {
    0 -> Color.White
    1 -> Color.Red
    2 -> Color.Green
    else -> throw IllegalArgumentException("Missing color for player $player")
  }
}