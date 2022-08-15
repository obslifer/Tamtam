package com.yourcompany.android.tictactoe.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun GameScreen() {
  val p00 = rememberSaveable { mutableStateOf(false) }
  val p01 = rememberSaveable { mutableStateOf(false) }
  val p02 = rememberSaveable { mutableStateOf(false) }
  val p10 = rememberSaveable { mutableStateOf(false) }
  val p11 = rememberSaveable { mutableStateOf(false) }
  val p12 = rememberSaveable { mutableStateOf(false) }
  val p20 = rememberSaveable { mutableStateOf(false) }
  val p21 = rememberSaveable { mutableStateOf(false) }
  val p22 = rememberSaveable { mutableStateOf(false) }

  Row(
    modifier = Modifier.fillMaxSize()
  ) {
    Column(modifier = Modifier.weight(1f)) {
      OutlinedButton(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        colors = ButtonDefaults.buttonColors(if (!p00.value) Color.White else Color.Black),
        onClick = { p00.value = !p00.value }) {}
      OutlinedButton(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        colors = ButtonDefaults.buttonColors(if (!p01.value) Color.White else Color.Black),
        onClick = { p01.value = !p01.value }) {}
      OutlinedButton(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        colors = ButtonDefaults.buttonColors(if (!p02.value) Color.White else Color.Black),
        onClick = { p02.value = !p02.value }) {}
    }
    Column(modifier = Modifier.weight(1f)) {
      OutlinedButton(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        colors = ButtonDefaults.buttonColors(if (!p10.value) Color.White else Color.Black),
        onClick = { p10.value = !p10.value }) {}
      OutlinedButton(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        colors = ButtonDefaults.buttonColors(if (!p11.value) Color.White else Color.Black),
        onClick = { p11.value = !p11.value }) {}
      OutlinedButton(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        colors = ButtonDefaults.buttonColors(if (!p12.value) Color.White else Color.Black),
        onClick = { p12.value = !p12.value }) {}
    }
    Column(modifier = Modifier.weight(1f)) {
      OutlinedButton(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        colors = ButtonDefaults.buttonColors(if (!p20.value) Color.White else Color.Black),
        onClick = { p20.value = !p20.value }) {}
      OutlinedButton(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        colors = ButtonDefaults.buttonColors(if (!p21.value) Color.White else Color.Black),
        onClick = { p21.value = !p21.value }) {}
      OutlinedButton(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        colors = ButtonDefaults.buttonColors(if (!p22.value) Color.White else Color.Black),
        onClick = { p22.value = !p22.value }) {}
    }
  }
}