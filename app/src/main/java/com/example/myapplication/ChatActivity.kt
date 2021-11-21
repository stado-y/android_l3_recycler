package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.chatdata.ChatItem
import com.example.myapplication.chatdata.ChatModel
import com.example.myapplication.chatrecyclerutil.ChatDiffutil
import com.example.myapplication.chatrecyclerutil.ChatItemDecorator
import com.example.myapplication.chatrecyclerutil.ChatRecyclerAdapter
import com.example.myapplication.chatrecyclerutil.ChatRecyclerAdapter.userBtnClicked
import com.example.myapplication.databinding.ActivityChatBinding


class ChatActivity : AppCompatActivity(), userBtnClicked {

    private val chatModel: ChatModel by viewModels()

    private val diff = ChatDiffutil()

    private lateinit var binding: ActivityChatBinding

    private val chatAdapter = ChatRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerViewInit()
        chatAdapter.mBtnClickListener = this

        binding.sendMessageBtn.setOnClickListener { onBtnSendClick() }
    }

    override fun onStart() {
        super.onStart()

        chatModel.messages.observe(this) {

            val newList = chatModel.messages.value
            if (newList != null) {
                updateAdapterMessageList(newList)
            }
        }

    }

    private fun recyclerViewInit() {

        binding.chatRecyclerView.apply {

            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration( ChatItemDecorator(20) )

        }
    }

    fun onBtnSendClick() {

        val message = binding.textInputView.text.toString()
        chatModel.addMessage(message)

        binding.textInputView.text.clear()
        binding.textInputView.clearFocus()
    }

    private fun updateAdapterMessageList(newList: MutableList<ChatItem>) {

        if (chatAdapter.messageList != newList) {

            diff.setLists(chatAdapter.messageList, newList)
            val diffResult = DiffUtil.calculateDiff(diff)

            val adapterList: MutableList<ChatItem> = mutableListOf()
            adapterList.addAll(newList)
            chatAdapter.messageList = adapterList
            diffResult.dispatchUpdatesTo(chatAdapter)
        }
    }

    override fun onUserBtnClicked(num: Int) {
        Log.d("MainActivity", "userObserver start")
        if (num != chatAdapter.currentUserId) {
            chatModel.changeUser(num)
            Log.d("MainActivity", "userObserver pass")
            chatAdapter.currentUserId = num
            chatAdapter.notifyDataSetChanged()
        }
    }
}


