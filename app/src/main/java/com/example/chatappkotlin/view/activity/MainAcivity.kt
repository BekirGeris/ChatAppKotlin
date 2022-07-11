package com.example.chatappkotlin.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import com.example.chatappkotlin.R
import com.example.chatappkotlin.view.fragment.ChatFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class MainAcivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile) {
            findNavController(R.id.fragmentContainerView).navigate(ChatFragmentDirections.actionGlobalProfileFragment())
        } else if (item.itemId == R.id.menu_signout) {
            findNavController(R.id.fragmentContainerView).navigate(ChatFragmentDirections.actionGlobalNav1())
            auth.signOut()
        }
        return super.onOptionsItemSelected(item)
    }
}