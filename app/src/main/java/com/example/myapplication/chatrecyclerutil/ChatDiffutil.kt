package com.example.myapplication.chatrecyclerutil

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.chatdata.ChatItem

public class ChatDiffutil: DiffUtil.Callback() {

    private lateinit var oldList: MutableList<ChatItem>
    private lateinit var newList: MutableList<ChatItem>

    public fun setLists(listOld: MutableList<ChatItem>, listNew: MutableList<ChatItem>) {

        oldList = listOld
        newList = listNew
    }


    override fun getOldListSize(): Int {

        return oldList.size
    }

    override fun getNewListSize(): Int {

        return newList.size
    }

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {

        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {

        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        if (oldItem.type != newItem.type) { return false }
        if (oldItem.type == "ChatMessage") {

            return (oldItem as ChatItem.ChatMessage).message == (newItem as ChatItem.ChatMessage).message
                    && oldItem.userId == newItem.userId
        }
        else {
            return (oldItem as ChatItem.ChatHeader).firstUserMessageCount == (newItem as ChatItem.ChatHeader).firstUserMessageCount
                    && oldItem.secondUserMessageCount == newItem.secondUserMessageCount
        }
    }


}