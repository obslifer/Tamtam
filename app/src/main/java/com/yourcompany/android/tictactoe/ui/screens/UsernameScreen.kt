package com.yourcompany.android.tictactoe.ui.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourcompany.android.tictactoe.viewmodel.MessagingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameScreen(viewModel: MessagingViewModel, onUsernameSet: () -> Unit) {
    val usernameState = remember { mutableStateOf("") }
    Log.d(MessagingViewModel.TAG, "nane")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Enter your username", fontSize = 20.sp, modifier = Modifier.padding(8.dp))
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            BasicTextField(
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
            )
        }
        Button(
            onClick = {
                viewModel.setLocalUsername(usernameState.value)
                onUsernameSet()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set Username")
        }
    }
}