package com.example.profilelab.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.profilelab.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.FileDescriptor
import java.io.IOException

class Profile : Fragment() {

    private lateinit var frame: ImageView
    private var imageUri: Uri? = null
    private lateinit var nameTv: TextView
    private lateinit var surnameTv: TextView
    private lateinit var interestsTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val viewThis: View = inflater.inflate(R.layout.fragment_profile, container, false)

        frame = viewThis.findViewById(R.id.profile_photo)
        nameTv = viewThis.findViewById(R.id.fullname)
        surnameTv = viewThis.findViewById(R.id.nickname)
        interestsTv = viewThis.findViewById(R.id.interests)

        initializeData()

        FirebaseFirestore.getInstance().collection("users")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    var interestz = listOf<String>()
                    for (document in value) {
                        if (document.id == FirebaseAuth.getInstance().currentUser?.uid) {
                            interestz = document.data["interests"] as List<String>
                            nameTv.text = (document.data["username"] as String)
                            surnameTv.text = (document.data["nickname"] as String)
                            interestsTv.text =(interestz.joinToString(", "))
                            break
                        }
                    }
                }
            }

        return viewThis
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_icon -> {

                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                fragmentTransaction.replace(R.id.frame_layout, EditProfile())
                fragmentTransaction.commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeData() {
        val sharedPreference =  requireActivity().getSharedPreferences("PROFILE_DATA", Context.MODE_PRIVATE)

        if (
            sharedPreference.contains("IMAGE_URI") &&
            sharedPreference.contains("FNAME") &&
            sharedPreference.contains("NNAME") &&
            sharedPreference.contains("INTERESTS")
        ) {
            Log.i("------> stuff exist", "Loaded from shared preferences")
            imageUri = Uri.parse(sharedPreference.getString("IMAGE_URI",null))

            if ( uriToBitmap(imageUri!!) == null)
                frame.setBackgroundResource(R.drawable.default_profile)
            else
                frame.setImageBitmap(uriToBitmap(imageUri!!))

            nameTv.text = sharedPreference.getString("FNAME",null)
            surnameTv.text = sharedPreference.getString("NNAME",null)
            interestsTv.text = sharedPreference.getString("INTERESTS",null)
        } else {
            Log.i("------> else block worked", "Loaded from default")
            frame.setBackgroundResource(R.drawable.default_profile)
            nameTv.setText(R.string.default_fullName)
            surnameTv.setText(R.string.default_nickname)
            interestsTv.setText(R.string.default_interests)
        }
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = requireActivity().contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}