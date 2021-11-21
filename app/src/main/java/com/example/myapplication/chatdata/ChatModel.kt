package com.example.myapplication.chatdata

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.testutil.messagesList

class ChatModel: ViewModel() {

    val currentUser: MutableLiveData<Int> by lazy {

        MutableLiveData<Int>()
    }

    val messages: MutableLiveData<MutableList<ChatItem>> by lazy {

        MutableLiveData<MutableList<ChatItem>>()
    }

    private val firstUserMessageCounter: MutableLiveData<Int> by lazy {

        MutableLiveData<Int>()
    }
    private val secondUserMessageCounter: MutableLiveData<Int> by lazy {

        MutableLiveData<Int>()
    }

    init {

        currentUser.value = 1
        countMessages(messagesList)

        messagesList[0] = getNewChatHeader()
        messages.value = messagesList

//        firstUserMessageCounter.value = 0
//        secondUserMessageCounter.value = 0


    }

    private fun getNewChatHeader():ChatItem.ChatHeader {
        val chatHeader = ChatItem.ChatHeader(0,0)
        firstUserMessageCounter.value?.let { chatHeader.firstUserMessageCount = it }
        secondUserMessageCounter.value?.let { chatHeader.secondUserMessageCount = it }

        return chatHeader
    }

    private fun countMessages(list: List<ChatItem>) {

        var firstUserMessages = 0
        var secondUserMessages = 0

        for (item in list) {

            when (item.userId) {

                1 -> firstUserMessages++
                2 -> secondUserMessages++
            }
        }
        firstUserMessageCounter.value = firstUserMessages
        secondUserMessageCounter.value = secondUserMessages
    }


    fun changeUser(userId: Int) {
        Log.d("ChatModel changeUser", "start")
        if (currentUser.value != userId && userId in 1..2) {
            Log.d("ChatModel changeUser", "pass")
            currentUser.value = userId
        }
    }

    fun addMessage(message: String) {

        val userId = currentUser.value

        if (userId != null) {

            val chatMessage = ChatItem.ChatMessage(userId, message)

            val messageList = messages.value

            if (messageList != null) {

                messageList.add(chatMessage)
                countMessages(messageList)
                messageList[0] = getNewChatHeader()

                messages.value = messageList
            }
            else {

                val newList = mutableListOf<ChatItem>(getNewChatHeader(), chatMessage)
                messages.value = newList

            }
        }

    }
}