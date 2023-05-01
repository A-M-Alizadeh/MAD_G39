package com.example.profilelab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    }

    private fun readData(){
//        GlobalScope.launch(Dispatchers.IO) {
//            val sports = db.courtSportDao().getInDetail(4)
//            for (sport in sports){
//                Log.e("DBTestActivity", "sport: $sport")
//            }
//        }
    }
}