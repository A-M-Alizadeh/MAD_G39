package com.example.profilelab

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.profilelab.databinding.ActivityMainBinding
import com.example.profilelab.fragments.CalendarFrag
import com.example.profilelab.fragments.MyReserve
import com.example.profilelab.fragments.Profile
import com.example.profilelab.models.FireUser
import com.example.profilelab.view_models.FireUserViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(CalendarFrag())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.calendar -> replaceFragment(CalendarFrag())
                R.id.my_reservations -> replaceFragment(MyReserve())
                R.id.profile -> replaceFragment(Profile())
                else -> {

                }
            }
            true
        }
        binding.bottomNavigationView.selectedItemId = R.id.my_reservations

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)// set drawable icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id == FirebaseAuth.getInstance().currentUser?.uid)
                            navView.menu.add(document.data["nickname"].toString()+":"+document.data["username"].toString()).isEnabled =
                                false
                    }}
//            navView.menu.add("Really ?")
            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.firstItem -> {
                        val third = Intent(applicationContext, Friends::class.java)
                        startActivity(third)
                    }
                    R.id.secondtItem -> {
                        val secondActivityIntent = Intent(applicationContext, AboutUs::class.java)
                        startActivity(secondActivityIntent)
                    }
                    R.id.thirdItem -> {
                        val third = Intent(applicationContext, Courts::class.java)
                        startActivity(third)
                    }
                    R.id.signOutItem -> {
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this@MainActivity, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                true
            }

//            drawerLayout.findViewById<NavigationView>(R.id.nav_view).getHeaderView(0).findViewById<ImageView>(R.id.nav_header_image)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if(binding.drawerLayout.isDrawerOpen(binding.navView))
                    binding.drawerLayout.closeDrawer(binding.navView)
                else
                    binding.drawerLayout.openDrawer(binding.navView)
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}