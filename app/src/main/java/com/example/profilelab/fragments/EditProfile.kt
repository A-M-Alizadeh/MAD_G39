package com.example.profilelab.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.profilelab.R
import java.io.FileDescriptor
import java.io.IOException


class EditProfile : Fragment() {

    lateinit var frame: ImageButton
    var imageUri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    lateinit var nameTv: EditText
    lateinit var nicknameET: EditText
    lateinit var interestsTv: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if (
            context?.let { ActivityCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA) } == PackageManager.PERMISSION_DENIED ||
            context?.let { ActivityCompat.checkSelfPermission(it, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED
        ) {
            val permissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {

                }
                else {
                    // Do otherwise
                }
            }

            permissionLauncher.launch(android.Manifest.permission.CAMERA)
            permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val viewThis: View = inflater.inflate(R.layout.activity_edit_profile, container, false)

        frame = viewThis.findViewById(R.id.profile_photo)
//        val editBtn = viewThis.findViewById<ImageButton>(R.id.edit_photo)
        nameTv = viewThis.findViewById(R.id.fullname)
        nicknameET = viewThis.findViewById(R.id.nickname)
        interestsTv = viewThis.findViewById(R.id.interests)

        frame.setOnClickListener { onClick(it) }
//        editBtn.setOnClickListener { onClick(it) }

        frame.setBackgroundResource(R.drawable.default_profile)
        nameTv.setText(R.string.default_fullName)
        nicknameET.setText(R.string.default_nickname)
        interestsTv.setText(R.string.default_interests)

        initializeData()

        return viewThis
    }

    private fun onClick(v: View) {
        when (v.id) {
            else -> {
                registerForContextMenu(v)
                requireActivity().openContextMenu(v)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_button -> {

                saveData()

                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                fragmentTransaction.replace(R.id.frame_layout, Profile())
                fragmentTransaction.commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.edit_photo_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater): Boolean {
//        inflater.inflate(R.menu.edit_profile_menu, menu)
//        return true
//    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.gallery -> {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
                return true
            }
            R.id.camera -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (
                    context?.let { ActivityCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA) } == PackageManager.PERMISSION_DENIED ||
                    context?.let { ActivityCompat.checkSelfPermission(it, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED
                ) {
                    val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, 121)
                }
                openCamera()

//                }
                return true
            }
            else -> return false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            //imageView.setImageURI(image_uri);
            val bitmap = uriToBitmap(imageUri!!)
            frame.setImageBitmap(bitmap)
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            //imageView.setImageURI(image_uri);
            val bitmap = uriToBitmap(imageUri!!)
            frame.setImageBitmap(bitmap)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("image_uri", imageUri.toString())
        outState.putString("fname_et", nameTv.text.toString())
        outState.putString("nname_et", nicknameET.text.toString())
        outState.putString("interests_et", interestsTv.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        val sharedPreference =  requireActivity().getSharedPreferences("PROFILE_DATA", Context.MODE_PRIVATE)

        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString("image_uri",null))
        }

        if ( imageUri == null)
            frame.setBackgroundResource(R.drawable.default_profile)
        else
            frame.setImageBitmap(uriToBitmap(imageUri!!))

        if (savedInstanceState != null) {
            nameTv.setText(savedInstanceState.getString("fname_et"))
        }
        if (savedInstanceState != null) {
            nicknameET.setText(savedInstanceState.getString("nname_et"))
        }
        if (savedInstanceState != null) {
            interestsTv.setText(savedInstanceState.getString("interests_et"))
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    private fun saveData() {
        val sharedPreference =  requireActivity().getSharedPreferences("PROFILE_DATA", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("IMAGE_URI", imageUri.toString())
        editor.putString("FNAME", nameTv.text.toString())
        editor.putString("NNAME", nicknameET.text.toString())
        editor.putString("INTERESTS", interestsTv.text.toString())
        editor.apply()
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

            frame.setImageBitmap(uriToBitmap(imageUri!!))
            nameTv.setText(sharedPreference.getString("FNAME",null))
            nicknameET.setText(sharedPreference.getString("NNAME",null))
            interestsTv.setText(sharedPreference.getString("INTERESTS",null))
        } else {
            Log.i("------> else block worked", "Loaded from default")
            frame.setBackgroundResource(R.drawable.default_profile)
            nameTv.setText(R.string.default_fullName)
            nicknameET.setText(R.string.default_nickname)
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