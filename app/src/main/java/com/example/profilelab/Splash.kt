
package com.example.profilelab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.profilelab.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(supportActionBar!=null)
            this.supportActionBar?.hide()

        val animationView: LottieAnimationView = findViewById(R.id.lottie)
        animationView.setAnimation("basketball.json")
        animationView.loop(true)
        animationView.playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            if (checkDatabase()) {
                val mInHome = Intent(this@Splash, MainActivity::class.java)
                this@Splash.startActivity(mInHome)
                this@Splash.finish()
            } else {
                Toast.makeText(this, "Database not created", Toast.LENGTH_SHORT).show()
            }

        }, 2000)
    }

    fun checkDatabase(): Boolean {
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

    private fun prepopulateDatabase() {
        Log.e("Database", "Database Prepopulating")
        var db = AppDB.getDatabase(this)
        var sport1 = Sport(1, "Basketball")
        var sport2 = Sport(2, "Football")
        var sport3 = Sport(3, "Volleyball")
        var sport4 = Sport(4, "Tennis")
        var sport5 = Sport(5, "Badminton")
        var sport6 = Sport(6, "Table Tennis")
        var sport7 = Sport(7, "Squash")

        GlobalScope.launch(Dispatchers.IO) {
            db.sportDao().insertAll(listOf(sport1, sport2, sport3, sport4, sport5, sport6, sport7))
        }


        var court1 = Court(1, "Central Park")
        var court2 = Court(2, "Polito")
        var court3 = Court(3, "Ruffini")
        var court4 = Court(4, "Garibaldi")
        var court5 = Court(5, "San Siro")

        GlobalScope.launch(Dispatchers.IO) {
            db.courtDao().insertAll(listOf(court1, court2, court3, court4, court5))
        }

        var courtSport1 = CourtSport(1,1, 1)
        var courtSport2 = CourtSport(2,2, 1)
        var courtSport3 = CourtSport(3,3, 1)
        var courtSport5 = CourtSport(4,5, 1)

        var courtSport6 = CourtSport(5,2,2)
        var courtSport7 = CourtSport(6,3,2)
        var courtSport8 = CourtSport(7,4,2)
        var courtSport9 = CourtSport(8,5,2)

        var courtSport10 = CourtSport(9,1,3)
        var courtSport11 = CourtSport(10,2,3)
        var courtSport14 = CourtSport(11,5,3)

        var courtSport15 = CourtSport(12,1,4)
        var courtSport16 = CourtSport(13,2,4)
        var courtSport17 = CourtSport(14,3,4)
        var courtSport18 = CourtSport(15,4,4)
        var courtSport19 = CourtSport(16,5,4)

        var courtSport20 = CourtSport(17,1,5)
        var courtSport21 = CourtSport(18,2,5)

        var courtSport22 = CourtSport(19,1,6)
        var courtSport23 = CourtSport(20,2,6)
        var courtSport29 = CourtSport(21,4,6)

        var courtSport24 = CourtSport(22,1,7)
        var courtSport25 = CourtSport(23,2,7)
        var courtSport26 = CourtSport(24,3,7)
        var courtSport27 = CourtSport(25,4,7)
        var courtSport28 = CourtSport(26,5,5)


        GlobalScope.launch(Dispatchers.IO) {
            db.courtSportDao().insertAll(listOf(courtSport1, courtSport2, courtSport3, courtSport5, courtSport6, courtSport7, courtSport8, courtSport9, courtSport10, courtSport11, courtSport14, courtSport15, courtSport16, courtSport17, courtSport18, courtSport19, courtSport20, courtSport21, courtSport22, courtSport23, courtSport29, courtSport24, courtSport25, courtSport26, courtSport27, courtSport28))
        }



        var timeSlot1 = TimeSlot(1, "08:00", " 10:00")
        var timeSlot2 = TimeSlot(2, "10:00", " 12:00")
        var timeSlot3 = TimeSlot(3, "12:00", " 14:00")
        var timeSlot4 = TimeSlot(4, "14:00", " 16:00")
        var timeSlot5 = TimeSlot(5, "16:00", " 18:00")
        var timeSlot6 = TimeSlot(6, "18:00", " 20:00")
        var timeSlot7 = TimeSlot(7, "20:00", " 22:00")

        GlobalScope.launch(Dispatchers.IO) {
            db.timeSlotDao().insertAll(listOf(timeSlot1, timeSlot2, timeSlot3, timeSlot4, timeSlot5, timeSlot6, timeSlot7))
        }


        var reservation1 = Reservation(1, 1, "2023-04-26", 1, 1, "sth is needed", 1)
        var reservation2 = Reservation(2, 1, "2023-04-26", 3, 2, "ball is missed", 0)
        var reservation3 = Reservation(3, 1, "2023-04-26", 5, 3, "", 0)
        var reservation4 = Reservation(4, 1, "2023-04-26", 23, 4, "we might not come", 1)

        GlobalScope.launch(Dispatchers.IO) {
            db.reservationDao().insertAll(listOf(reservation1, reservation2, reservation3, reservation4))
        }

        Log.e("DB", "Database created")

    }
}