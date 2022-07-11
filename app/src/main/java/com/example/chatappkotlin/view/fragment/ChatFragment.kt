package com.example.chatappkotlin.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.set
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatappkotlin.databinding.FragmentChatBinding
import com.example.chatappkotlin.view.adapter.ChatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var adapter: ChatAdapter
    val list = ArrayList<String>()

    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var databaseReferance: DatabaseReference

    lateinit var user: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReferance = database.reference
        user = auth.currentUser!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)
        binding.send.setOnClickListener { sendMessage(binding.messageEditText.text.toString()) }

        adapter = ChatAdapter(list)

        getData()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    fun sendMessage(message: String) {
        val uuid = UUID.randomUUID().toString()

        databaseReferance.child("chats").child(uuid).child("usermessage").setValue(message)
        databaseReferance.child("chats").child(uuid).child("useremail").setValue(user.email)
        databaseReferance.child("chats").child(uuid).child("time").setValue(ServerValue.TIMESTAMP)

        binding.messageEditText.text.clear()
        getData()
    }

    fun getData() {
        val newReference = database.getReference("chats")

        val query: Query = newReference.orderByChild("time")

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (ds in snapshot.children) {
                    val hashMap = ds.value as HashMap<String, String>
                    val userEmail: String? = hashMap.get("useremail")
                    val userMessage: String? = hashMap.get("usermessage")

                    if (userEmail != null) {
                        list.add(userEmail.split("@").get(0) + ": " + userMessage)
                    }

                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}