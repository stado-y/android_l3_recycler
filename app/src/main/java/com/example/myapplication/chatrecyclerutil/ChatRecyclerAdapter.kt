package com.example.myapplication.chatrecyclerutil

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.chatdata.ChatItem
import com.example.myapplication.databinding.ChatHeaderLayoutBinding
import com.example.myapplication.databinding.CurrentUserMessageBinding
import com.example.myapplication.databinding.OtherUserMessageBinding


class ChatRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface UserBtnClicked {

        fun onUserBtnClicked(num: Int)

        fun onViewLongClicked(v: View,  pos: Int): Boolean
    }
    lateinit var mBtnClickListener: UserBtnClicked


    var messageList = mutableListOf<ChatItem>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    var currentUserId: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {

        when (type) {

            1 -> {
                val binding = CurrentUserMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderCurrentUserMessage(binding)
            }
            2 -> {
                val binding = OtherUserMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderOtherUserMessage(binding)
            }
            else -> {
                val binding = ChatHeaderLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderChatHeader(binding)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {

       return when (messageList[position].userId) {

           -1 -> CHAT_HEADER
           currentUserId -> CURRENT_USER_MESSAGE
           else -> OTHER_USER_MESSAGE
       }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        when (holder) {
            is ViewHolderCurrentUserMessage -> holder.bind(messageList[pos])
            is ViewHolderOtherUserMessage -> holder.bind(messageList[pos])
            is ViewHolderChatHeader -> holder.bind(messageList[pos])
        }
    }

    override fun getItemCount(): Int {

        return messageList.size
    }


    inner class ViewHolderCurrentUserMessage(private val binding: CurrentUserMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatItem) {
            binding.currentUserTextView.text = (item as ChatItem.ChatMessage).message
            //binding.currentUserTextView.setLongClickable(true)

            binding.currentUserTextView.setOnLongClickListener {

                mBtnClickListener.onViewLongClicked(it, adapterPosition)
            }
        }
    }

    inner class ViewHolderOtherUserMessage(private val binding: OtherUserMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatItem) {
            binding.otherUserTextView.text = (item as ChatItem.ChatMessage).message
            //binding.otherUserTextView.setLongClickable(true)

            binding.otherUserTextView.setOnLongClickListener {

               mBtnClickListener.onViewLongClicked(it, adapterPosition)
            }
        }
    }

    inner class ViewHolderChatHeader(private val binding: ChatHeaderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatItem) {
            val header = item as ChatItem.ChatHeader
            binding.firstUserChatBtn.text = "${ binding.firstUserChatBtn.hint } : ${ header.firstUserMessageCount }"
            binding.secondUserChatBtn.text = "${ binding.secondUserChatBtn.hint } : ${ header.secondUserMessageCount }"

            when (currentUserId) {

                1-> {
                    binding.firstUserChatBtn.setBackgroundResource(R.color.choosenUserColor)
                    binding.secondUserChatBtn.setBackgroundResource(R.color.otherUserColor)
                }
                2-> {
                    binding.secondUserChatBtn.setBackgroundResource(R.color.choosenUserColor)
                    binding.firstUserChatBtn.setBackgroundResource(R.color.otherUserColor)
                }
            }
            binding.firstUserChatBtn.setOnClickListener {

                mBtnClickListener.onUserBtnClicked(1)
                Log.d("firstUserChatBtn", "CLICKED!!!")
            }
            binding.secondUserChatBtn.setOnClickListener {

                mBtnClickListener.onUserBtnClicked(2)
                Log.d("secondUserChatBtn", "CLICKED!!!")
            }
        }
    }


    companion object {
        const val CHAT_HEADER = 0
        const val CURRENT_USER_MESSAGE = 1
        const val OTHER_USER_MESSAGE = 2
    }
}