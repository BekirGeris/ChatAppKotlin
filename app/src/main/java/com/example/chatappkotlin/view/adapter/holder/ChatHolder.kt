package com.example.chatappkotlin.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappkotlin.databinding.ChatRowBinding

class ChatHolder(var binding: ChatRowBinding) : RecyclerView.ViewHolder(binding.root) {

    var message: String

    init {
        message = binding.chatItem.text.toString()
    }
}