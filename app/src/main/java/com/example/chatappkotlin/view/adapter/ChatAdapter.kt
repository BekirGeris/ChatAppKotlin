package com.example.chatappkotlin.view.adapter

import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappkotlin.Message
import com.example.chatappkotlin.databinding.ChatRowBinding
import com.example.chatappkotlin.view.adapter.holder.ChatHolder
import java.util.ArrayList

class ChatAdapter(val chatList: ArrayList<Message>) : RecyclerView.Adapter<ChatHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding = ChatRowBinding.inflate(LayoutInflater.from(parent.context))
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.binding.chatItem.text = chatList.get(position).message
        holder.binding.chatItem.width = Resources.getSystem().getDisplayMetrics().widthPixels;
        if (chatList.get(position).isUser) {
             holder.binding.chatItem.gravity = Gravity.RIGHT
        } else {
            holder.binding.chatItem.gravity = Gravity.LEFT
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

}