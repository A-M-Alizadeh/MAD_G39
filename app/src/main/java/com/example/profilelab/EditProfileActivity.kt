package com.example.profilelab

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
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileDescriptor
import java.io.IOException
import kotlin.math.log


class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var frame: ImageButton;
    var image_uri: Uri? = null;
    private val RESULT_LOAD_IMAGE = 123
    val IMAGE_CAPTURE_CODE = 654
    lateinit var fName_et: EditText
    lateinit var nName_et: EditText
    lateinit var interests_et: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        //TODO ask for permission of camera upon first launch of application
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED) {
            val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission, 112)
        }
//        }

        frame = findViewById<ImageButton>(R.id.profile_photo)
        val edit_btn = findViewById<ImageButton>(R.id.edit_photo)
        fName_et = findViewById<EditText>(R.id.fullname)
        nName_et = findViewById<EditText>(R.id.nickname)
        interests_et = findViewById<EditText>(R.id.interests)

        frame.setOnClickListener(this)
        edit_btn.setOnClickListener(this)

        frame.setBackgroundResource(R.drawable.default_profile)
        fName_et.setText(R.string.default_fullName)
        nName_et.setText(R.string.default_nickname)
        interests_et.setText(R.string.default_interests)

        val sharedPreference =  getSharedPreferences("PROFILE_DATA", Context.MODE_PRIVATE)
        if (sharedPreference.contains("IMAGE_URI") && sharedPreference.contains("FNAME") && sharedPreference.contains("NNAME") && sharedPreference.contains("INTERESTS")) {
            Log.i("------> stuff exist", "Loaded from shared preferences")
            image_uri = Uri.parse(sharedPreference.getString("IMAGE_URI",null))
            frame.setImageBitmap(uriToBitmap(image_uri!!))
            fName_et.setText(sharedPreference.getString("FNAME",null))
            nName_et.setText(sharedPreference.getString("NNAME",null))
            interests_et.setText(sharedPreference.getString("INTERESTS",null))
        }else{
            Log.i("------> else block worked", "Loaded from default")
            frame.setBackgroundResource(R.drawable.default_profile)
            fName_et.setText(R.string.default_fullName)
            nName_et.setText(R.string.default_nickname)
            interests_et.setText(R.string.default_interests)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            else -> {
                registerForContextMenu(v)
                openContextMenu(v)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.save_button -> {
                saveData()
                val intent = Intent(this, ShowProfileActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show()
        if (fName_et.text.toString() == "" || nName_et.text.toString() == "" || interests_et.text.toString() == "" || image_uri == null) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        } else {
            saveData()
        }

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.edit_photo_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
            
        return super.onPrepareOptionsMenu(menu)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_profile_menu, menu)
        return true
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.gallery -> {
                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
                return true
            }
            R.id.camera -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
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

    //TODO opens camera so that user can capture image
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            //imageView.setImageURI(image_uri);
            val bitmap = uriToBitmap(image_uri!!)
            frame.setImageBitmap(bitmap)
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            image_uri = data.data
            //imageView.setImageURI(image_uri);
            val bitmap = uriToBitmap(image_uri!!)
            frame.setImageBitmap(bitmap)
        }
    }

    //TODO takes URI of the image and returns bitmap
    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("image_uri", image_uri.toString())
        outState.putString("fname_et", fName_et.text.toString())
        outState.putString("nname_et", nName_et.text.toString())
        outState.putString("interests_et", interests_et.text.toString())
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        image_uri = Uri.parse(savedInstanceState.getString("image_uri"))
        frame.setImageBitmap(uriToBitmap(image_uri!!))
        fName_et.setText(savedInstanceState.getString("fname_et"))
        nName_et.setText(savedInstanceState.getString("nname_et"))
        interests_et.setText(savedInstanceState.getString("interests_et"))
    }

    private fun saveData() {
        val sharedPreference =  getSharedPreferences("PROFILE_DATA", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("IMAGE_URI",image_uri.toString())
        editor.putString("FNAME",fName_et.text.toString())
        editor.putString("NNAME",nName_et.text.toString())
        editor.putString("INTERESTS",interests_et.text.toString())
        editor.apply()
        finish()
    }

}