package com.yourcompany.android.tictactoe.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourcompany.android.tictactoe.domain.model.Message
import com.yourcompany.android.tictactoe.domain.model.MessageState
import com.yourcompany.android.tictactoe.viewmodel.MessagingViewModel
@Composable
fun ChatScreen(
    viewModel: MessagingViewModel
) {
    val state: MessageState by viewModel.state.observeAsState(MessageState(localUser = ""))

    Column(modifier = Modifier.fillMaxSize()) {
        MessagesList(messages = state.messages, modifier = Modifier.weight(1f))
        SendMessageBox(onSend = { message -> viewModel.sendMessage(message) })
    }
}

@Composable
fun MessagesList(messages: List<Message>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        messages.forEach { message ->
            Text(
                text = "${message.sender}: ${message.content}",
                fontSize = 16.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun SendMessageBox(onSend: (String) -> Unit) {
    var message by remember { mutableStateOf("") }

    Row(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
            )
            if (message.isEmpty()) {
                Text(
                    text = "Enter message",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
        Button(onClick = {
            if (message.isNotBlank()) {
                onSend(message)
                message = ""
                Log.d(MessagingViewModel.TAG, "Message sent: $message")
            }
        }) {
            Text(text = "Send")
        }
    }
}
