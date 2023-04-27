package com.example.profilelab

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.profilelab.dao.*
import com.example.profilelab.entities.*

@Database(entities = [Sport::class, Court::class, CourtSport::class,  TimeSlot::class, Reservation::class], version = 1)
abstract class AppDB : RoomDatabase(){
    abstract fun sportDao(): SportDao
    abstract fun courtDao(): CourtDao
    abstract fun courtSportDao(): CourtSportsDao
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
                    "g39_reservation_app_db"
                )//.createFromAsset("database/g39_reservation_app_db.db")
                    .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

    }
}