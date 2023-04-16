package com.example.profilelab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.profilelab.databinding.ActivityMainBinding
import com.example.profilelab.fragments.Calendar
import com.example.profilelab.fragments.MyReserve
import com.example.profilelab.fragments.Profile

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Calendar())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.calendar -> replaceFragment(Calendar())
                R.id.my_reservations -> replaceFragment(MyReserve())
                R.id.profile -> replaceFragment(Profile())
                else -> {

                }


            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}