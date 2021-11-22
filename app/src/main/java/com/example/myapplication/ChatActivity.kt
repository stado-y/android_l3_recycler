package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.chatdata.ChatItem
import com.example.myapplication.chatdata.ChatModel
import com.example.myapplication.chatrecyclerutil.ChatDiffutil
import com.example.myapplication.chatrecyclerutil.ChatItemDecorator
import com.example.myapplication.chatrecyclerutil.ChatRecyclerAdapter
import com.example.myapplication.chatrecyclerutil.ChatRecyclerAdapter.UserBtnClicked
import com.example.myapplication.databinding.ActivityChatBinding


class ChatActivity : AppCompatActivity(), UserBtnClicked {

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

        registerForContextMenu(binding.chatRecyclerView)

        binding.sendMessageBtn.setOnClickListener { onBtnSendClick() }
    }

    override fun onStart() {
        super.onStart()

        chatModel.messages.observe(this) {

            val newList = chatModel.messages.value
            if (newList != null) {
                updateAdapterMessageList(newList)

                registerForContextMenu(binding.chatRecyclerView)
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

    private fun onBtnSendClick() {

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

    override fun onViewLongClicked(v: View, pos: Int): Boolean {
        Log.d("MainActivity", "onViewLongClicked start")
        showPopupMenu(v, pos)

        return true
    }

    private fun showPopupMenu(v: View, pos: Int) {
        Log.d("MainActivity", "showPopupMenu start")
        val popupMenu = PopupMenu(this, v)
        popupMenu.inflate(R.menu.chat_messages_menu)
        Log.d("MainActivity", "showPopupMenu VIEW : ${ v }")
        popupMenu.setOnMenuItemClickListener { onMenuItemClicked(pos, it) }
        popupMenu.setOnDismissListener { onMenuDismiss() }

        popupMenu.show()
    }

    private fun onMenuItemClicked(pos: Int, menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {

            R.id.menu_delete -> {

                chatModel.deleteMessage(pos)
            }
        }
        return true
    }
    private fun onMenuDismiss() {

    }

//    override fun onCreateContextMenu(menu: ContextMenu, v: View,
//                                 menuInfo: ContextMenu.ContextMenuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.chat_messages_menu, menu)
//    }
//
//    override fun onContextItemSelected(item: MenuItem): Boolean {
//
//        return when (item.itemId) {
//
//            R.id.menu_cancel -> false
//            R.id.menu_delete -> {
//
//
//                true
//            }
//            else -> super.onContextItemSelected(item)
//        }
//    }
}


