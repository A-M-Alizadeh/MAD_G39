package com.example.profilelab

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import java.io.FileDescriptor
import java.io.IOException

class ShowProfileActivity : AppCompatActivity() {

    lateinit var frame: ImageView;
    var image_uri: Uri? = null;
    lateinit var fName_tv: TextView
    lateinit var nName_tv: TextView
    lateinit var interests_tv: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)

        frame = findViewById<ImageView>(R.id.profile_photo)
        fName_tv = findViewById<TextView>(R.id.fullname)
        nName_tv = findViewById<TextView>(R.id.nickname)
        interests_tv = findViewById<TextView>(R.id.interests)

        val sharedPreference =  getSharedPreferences("PROFILE_DATA", Context.MODE_PRIVATE)
        if (sharedPreference.contains("IMAGE_URI") && sharedPreference.contains("FNAME") && sharedPreference.contains("NNAME") && sharedPreference.contains("INTERESTS")) {
            Log.i("------> stuff exist", "Loaded from shared preferences")
            image_uri = Uri.parse(sharedPreference.getString("IMAGE_URI",null))

            if ( uriToBitmap(image_uri!!) == null)
                frame.setBackgroundResource(R.drawable.default_profile)
            else
                frame.setImageBitmap(uriToBitmap(image_uri!!))

            fName_tv.setText(sharedPreference.getString("FNAME",null))
            nName_tv.setText(sharedPreference.getString("NNAME",null))
            interests_tv.setText(sharedPreference.getString("INTERESTS",null))
        }else{
            Log.i("------> else block worked", "Loaded from default")
            frame.setBackgroundResource(R.drawable.default_profile)
            fName_tv.setText(R.string.default_fullName)
            nName_tv.setText(R.string.default_nickname)
            interests_tv.setText(R.string.default_interests)
        }
    }

    override fun onRestart() {
        super.onRestart()
        val sharedPreference =  getSharedPreferences("PROFILE_DATA", Context.MODE_PRIVATE)
        if (sharedPreference.contains("IMAGE_URI") && sharedPreference.contains("FNAME") && sharedPreference.contains("NNAME") && sharedPreference.contains("INTERESTS")) {
            Log.i("------> stuff exist", "Loaded from shared preferences")
            image_uri = Uri.parse(sharedPreference.getString("IMAGE_URI",null))
            frame.setImageBitmap(uriToBitmap(image_uri!!))
            fName_tv.setText(sharedPreference.getString("FNAME",null))
            nName_tv.setText(sharedPreference.getString("NNAME",null))
            interests_tv.setText(sharedPreference.getString("INTERESTS",null))
        }else{
            Log.i("------> else block worked", "Loaded from default")
            frame.setBackgroundResource(R.drawable.default_profile)
            fName_tv.setText(R.string.default_fullName)
            nName_tv.setText(R.string.default_nickname)
            interests_tv.setText(R.string.default_interests)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.edit_icon -> {
                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
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

}