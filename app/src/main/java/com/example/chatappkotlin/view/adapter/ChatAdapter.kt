package com.example.chatappkotlin.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappkotlin.databinding.ChatRowBinding
import com.example.chatappkotlin.view.adapter.holder.ChatHolder
import java.util.ArrayList

class ChatAdapter(val chatList: ArrayList<String>) : RecyclerView.Adapter<ChatHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding = ChatRowBinding.inflate(LayoutInflater.from(parent.context))
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.binding.chatItem.text = chatList.get(position)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

}