package com.example.profilelab

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.profilelab.dao.CourtDao
import com.example.profilelab.dao.ReservationDao
import com.example.profilelab.dao.SportDao
import com.example.profilelab.dao.TimeSlotDao
import com.example.profilelab.entities.Court
import com.example.profilelab.entities.Reservation
import com.example.profilelab.entities.Sport
import com.example.profilelab.entities.TimeSlot

@Database(entities = [Sport::class, Court::class, TimeSlot::class, Reservation::class], version = 1)
abstract class AppDB : RoomDatabase(){
    abstract fun sportDao(): SportDao
    abstract fun courtDao(): CourtDao
    abstract fun timeSlotDao(): TimeSlotDao
    abstract fun reservationDao(): ReservationDao

    companion object{

        @Volatile
        private var INSTANCE: AppDB? = null

        fun getDatabase(context: Context ): AppDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    "app_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

    }
}