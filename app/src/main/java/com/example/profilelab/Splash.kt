
package com.example.profilelab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.LottieAnimationView
import com.example.profilelab.models.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(supportActionBar!=null)
            this.supportActionBar?.hide()

        val animationView: LottieAnimationView = findViewById(R.id.lottie)
        animationView.setAnimation("basketball.json")
        animationView.loop(true)
        animationView.playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            firebaseAuthCheck()
        }, 2000)
    }

    private fun firebaseAuthCheck() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val mInHome = Intent(this@Splash, MainActivity::class.java)
            startActivity(mInHome)
            finish()
        }else{
            val mInHome = Intent(this@Splash, Login::class.java)
            startActivity(mInHome)
            finish()
        }
    }

    private fun checkDatabase(): Boolean {
        val sharedPreference = getSharedPreferences("Ali_app_data", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()

        return if (sharedPreference.getBoolean("isDatabaseCreated", false)) {
            Log.e("Database", "Database Was Created")
            true
        } else {
            Log.e("Database", "Database Not created")
            try {
                prepopulateDatabase()
                editor.putBoolean("isDatabaseCreated", true)
                editor.apply()
                Log.e("Database", "Database Done")
                true
            } catch (e: Exception) {
                editor.putBoolean("isDatabaseCreated", false)
                editor.apply()
                Log.e("Database", e.message.toString())
                false
            }
        }
    }

    private fun prepopulateDatabase(): Boolean {
        Log.e("Database", "Database Prepopulating")
        val db = AppDB.getDatabase(this)
        val sport1 = Sport(1, "Basketball")
        val sport2 = Sport(2, "Football")
        val sport3 = Sport(3, "Volleyball")
        val sport4 = Sport(4, "Tennis")
        val sport5 = Sport(5, "Badminton")
        val sport6 = Sport(6, "Table Tennis")
        val sport7 = Sport(7, "Squash")

        GlobalScope.launch(Dispatchers.IO) {
            db.sportDao().insertAll(listOf(sport1, sport2, sport3, sport4, sport5, sport6, sport7))
        }


        val court1 = Court(1, "Central Park")
        val court2 = Court(2, "Polito")
        val court3 = Court(3, "Ruffini")
        val court4 = Court(4, "Garibaldi")
        val court5 = Court(5, "San Siro")

        GlobalScope.launch(Dispatchers.IO) {
            db.courtDao().insertAll(listOf(court1, court2, court3, court4, court5))
        }

        val courtSport1 = CourtSport(1,1, 1)
        val courtSport2 = CourtSport(2,2, 1)
        val courtSport3 = CourtSport(3,3, 1)
        val courtSport5 = CourtSport(4,5, 1)

        val courtSport6 = CourtSport(5,2,2)
        val courtSport7 = CourtSport(6,3,2)
        val courtSport8 = CourtSport(7,4,2)
        val courtSport9 = CourtSport(8,5,2)

        val courtSport10 = CourtSport(9,1,3)
        val courtSport11 = CourtSport(10,2,3)
        val courtSport14 = CourtSport(11,5,3)

        val courtSport15 = CourtSport(12,1,4)
        val courtSport16 = CourtSport(13,2,4)
        val courtSport17 = CourtSport(14,3,4)
        val courtSport18 = CourtSport(15,4,4)
        val courtSport19 = CourtSport(16,5,4)

        val courtSport20 = CourtSport(17,1,5)
        val courtSport21 = CourtSport(18,2,5)

        val courtSport22 = CourtSport(19,1,6)
        val courtSport23 = CourtSport(20,2,6)
        val courtSport29 = CourtSport(21,4,6)

        val courtSport24 = CourtSport(22,1,7)
        val courtSport25 = CourtSport(23,2,7)
        val courtSport26 = CourtSport(24,3,7)
        val courtSport27 = CourtSport(25,4,7)
        val courtSport28 = CourtSport(26,5,5)


        GlobalScope.launch(Dispatchers.IO) {
            db.courtSportDao().insertAll(listOf(courtSport1, courtSport2, courtSport3, courtSport5, courtSport6, courtSport7, courtSport8, courtSport9, courtSport10, courtSport11, courtSport14, courtSport15, courtSport16, courtSport17, courtSport18, courtSport19, courtSport20, courtSport21, courtSport22, courtSport23, courtSport29, courtSport24, courtSport25, courtSport26, courtSport27, courtSport28))
        }



        val timeSlot1 = TimeSlot(1, "08:00", " 10:00")
        val timeSlot2 = TimeSlot(2, "10:00", " 12:00")
        val timeSlot3 = TimeSlot(3, "12:00", " 14:00")
        val timeSlot4 = TimeSlot(4, "14:00", " 16:00")
        val timeSlot5 = TimeSlot(5, "16:00", " 18:00")
        val timeSlot6 = TimeSlot(6, "18:00", " 20:00")
        val timeSlot7 = TimeSlot(7, "20:00", " 22:00")

        GlobalScope.launch(Dispatchers.IO) {
            db.timeSlotDao().insertAll(listOf(timeSlot1, timeSlot2, timeSlot3, timeSlot4, timeSlot5, timeSlot6, timeSlot7))
        }


        val reservation1 = Reservation(1, 1, "2023-04-26", 1, 1, "sth is needed", 1)
        val reservation2 = Reservation(2, 1, "2023-04-26", 3, 2, "ball is missed", 0)
        val reservation3 = Reservation(3, 1, "2023-04-26", 5, 3, "", 0)
        val reservation4 = Reservation(4, 1, "2023-04-26", 23, 4, "we might not come", 1)

        GlobalScope.launch(Dispatchers.IO) {
            db.reservationDao().insertAll(listOf(reservation1, reservation2, reservation3, reservation4))
        }
        db.close()
        Log.e("DB", "Database created")

        return true
    }
}