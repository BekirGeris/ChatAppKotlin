package com.example.chatappkotlin.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chatappkotlin.R
import com.example.chatappkotlin.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentProfileBinding

    lateinit var getContent : ActivityResultLauncher<String>

    lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>

    var selectedUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        binding.profile.setOnClickListener(this)
        binding.update.setOnClickListener(this)

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // Handle the returned Uri
            selectedUri = uri
            if (selectedUri != null) {
                val bitmap = when {
                    Build.VERSION.SDK_INT < 28 -> {
                        MediaStore.Images.Media.getBitmap(activity!!.contentResolver,selectedUri)
                    }
                    else -> {
                        val source = ImageDecoder.createSource(activity!!.contentResolver, selectedUri!!)
                        ImageDecoder.decodeBitmap(source)
                    }
                }
                binding.profile.setImageBitmap(bitmap)
            }

        }

        requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions  ->
            permissions.forEach { actionMap ->
                when (actionMap.key) {
                    Manifest.permission.READ_EXTERNAL_STORAGE ->
                        if (actionMap.value) {
                            goToGallery()
                        }
                }
            }
        }

        return binding.root
    }

    override fun onClick(v: View?) {
        val userName = binding.textView.text.toString()

        if (v == binding.profile) {
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //izin yoksa
                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            } else {
                //izin varsa
                goToGallery()
            }
        } else if (v == binding.update) {
            if (selectedUri != null && userName.isNotEmpty()) {

            } else {
                Toast.makeText(context, "Bilgileri giriniz.", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun goToGallery() {
        getContent.launch("image/*")
    }
}


