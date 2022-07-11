package com.example.chatappkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatappkotlin.databinding.FragmentSignBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignFragment : Fragment(), View.OnClickListener{

    lateinit var binding: FragmentSignBinding
    lateinit var auth: FirebaseAuth

    lateinit var email: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        binding.signUpBtn.setOnClickListener(this)
        binding.signInBtn.setOnClickListener(this)

        init()
        return binding.root
    }

    fun init() {
        val user = auth.currentUser

        if (user != null) {
            findNavController().navigate(SignFragmentDirections.navigationSignToNavigationChat())
        }
    }

    override fun onClick(v: View?) {
        email = binding.emailEditText.text.toString()
        password = binding.passwordEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (v == binding.signUpBtn) {
                signUp()
            } else if (v == binding.signInBtn) {
                signIn()
            }
         }else {
            Toast.makeText(activity, "Bilgileri Giriniz", Toast.LENGTH_LONG).show()
        }

    }

    fun signUp() {
        auth.createUserWithEmailAndPassword(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
            .addOnCompleteListener() { task ->
                finishLogin(task)
            }
    }

    fun signIn() {
        auth.signInWithEmailAndPassword(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
            .addOnCompleteListener() { task ->
                finishLogin(task)
            }
    }

    fun finishLogin(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            findNavController().navigate(SignFragmentDirections.navigationSignToNavigationChat())
        } else {
            Toast.makeText(activity, task.exception?.localizedMessage ?: "Failed", Toast.LENGTH_LONG).show()
        }
    }
}