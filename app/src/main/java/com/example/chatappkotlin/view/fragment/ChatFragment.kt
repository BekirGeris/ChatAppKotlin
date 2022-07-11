package com.example.chatappkotlin.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappkotlin.R
import com.example.chatappkotlin.databinding.FragmentChatBinding
import com.example.chatappkotlin.view.adapter.ChatAdapter

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)

        val list = ArrayList<String>()
        list.add("dfvfdvfdvfd")
        list.add("dfvfdvfd")
        list.add("dfvfdvfdvytufkyfd")
        adapter = ChatAdapter(list)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        return binding.root
    }
}