package com.yourcompany.android.tictactoe.domain.model

data class MessageState(
    val localUser: String,
    val messages: List<Message> = emptyList()
)

data class Message(
    val sender: String,
    val content: String
)
