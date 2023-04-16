package com.example.profilelab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.profilelab.databinding.ActivityDbtestBinding
import com.example.profilelab.entities.Court
import com.example.profilelab.entities.Reservation
import com.example.profilelab.entities.Sport
import com.example.profilelab.entities.TimeSlot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class DBTestActivity : AppCompatActivity() {

//    private lateinit var binding : ActivityDbtestBinding
    private lateinit var db : AppDB
    lateinit var writeBtn: Button
    lateinit var readBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityDbtestBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_dbtest)
        db = AppDB.getDatabase(this)

        writeBtn = findViewById(R.id.btn_write)
        readBtn = findViewById(R.id.btn_read)

        writeBtn.setOnClickListener {
            writeDate()
        }
        readBtn.setOnClickListener {
            readData()
        }

    }

    private fun writeDate() {
//        val sport = Sport(null, "Basketball")
//        var sportResult: Long = 0
//        var courtResult: Long = 0
//        var timeSlotResult: Long = 0
//        var reservationResult: Long = 0

//        GlobalScope.launch(Dispatchers.IO) {
//            sportResult = db.sportDao().insert(sport)
//            Log.w("------------------", sportResult.toString())
//        }

//        val court = Court(null, "Court 1", 1)
//        GlobalScope.launch(Dispatchers.IO) {
//            courtResult = db.courtDao().insert(court)
//        }

//        val timeSlot = TimeSlot(null, 10, 11)
//        GlobalScope.launch(Dispatchers.IO) {
//            timeSlotResult = db.timeSlotDao().insert(timeSlot)
//        }

//        val reservation = Reservation(null, "John", "2022-12-27 10:00", true, 1)
//        GlobalScope.launch(Dispatchers.IO) {
//            reservationResult = db.reservationDao().insert(reservation)
//        }

//        Toast.makeText(this, "Data written Successfully", Toast.LENGTH_SHORT).show()
//        Log.w("Sport------------------", sportResult.toString())
//        Log.w("Court------------------", courtResult.toString())
//        Log.w("Slot------------------", timeSlotResult.toString())
//        Log.w("Reserve------------------", reservationResult.toString())
    }

    private fun readData(){
//        lateinit var sport: Sport
//        GlobalScope.launch(Dispatchers.IO) {
//            sport = db.sportDao().findById(1)
//            Log.w("Sport------------------", sport.toString())
//        }



    }
}