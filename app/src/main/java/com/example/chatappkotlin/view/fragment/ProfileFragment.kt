package com.example.chatappkotlin.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.chatappkotlin.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.HashMap

class ProfileFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentProfileBinding

    lateinit var getContent : ActivityResultLauncher<String>

    lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>

    var selectedUri: Uri? = null

    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var storage: StorageReference
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference
        storage = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }

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

        getData()

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
                upload(userName)
            } else {
                Toast.makeText(context, "Bilgileri giriniz.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun upload(userName: String) {
        val imageName = UUID.randomUUID().toString()
        val newReference = storage.child("images/$imageName.jpg")
        newReference.putFile(selectedUri!!)
            .addOnSuccessListener { task ->
                val profileImageReference = FirebaseStorage.getInstance().getReference("images/$imageName.jpg")
                profileImageReference.downloadUrl.addOnSuccessListener { uploadedImageUrl ->
                    val profileInfoName = UUID.randomUUID().toString()
                    databaseReference.child("profiles").child(profileInfoName).child("useremail").setValue(auth.currentUser!!.email.toString())
                    databaseReference.child("profiles").child(profileInfoName).child("username").setValue(userName)
                    databaseReference.child("profiles").child(profileInfoName).child("profileImageUri").setValue(uploadedImageUrl.toString())
                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToNav2())
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun goToGallery() {
        getContent.launch("image/*")
    }
    private fun getData() {
        val newReference = database.getReference("profiles")

        newReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (s in snapshot.children) {
                    val hashMap: HashMap<String, String> = s.value as HashMap<String, String>
                    val userEmail = hashMap.get("useremail")

                    if (userEmail != null && auth.currentUser != null && userEmail.equals(auth.currentUser!!.email.toString())) {
                        val userName = hashMap.get("username")
                        val userImageUri = hashMap.get("profileImageUri")

                        if (userName != null && userImageUri != null) {
                            binding.textView.setText(userName)
                            Picasso.get().load(userImageUri).into(binding.profile);
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}


