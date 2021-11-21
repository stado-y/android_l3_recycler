package com.example.myapplication.chatdata


sealed class ChatItem {

    abstract val userId: Int
    abstract val type: String

    data class ChatMessage (val id: Int, val message: String): ChatItem() {

        override val userId: Int
            get() = id

        override val type: String
            get() = "ChatMessage"
    }

    data class ChatHeader(var firstUserMessageCount: Int, var secondUserMessageCount: Int): ChatItem() {

        override val userId: Int
            get() = -1

        override val type: String
            get() = "ChatHeader"
    }
}