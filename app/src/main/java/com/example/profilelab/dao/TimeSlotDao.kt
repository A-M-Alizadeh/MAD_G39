package com.example.profilelab.dao
import androidx.room.*
import com.example.profilelab.entities.TimeSlot

@Dao
interface TimeSlotDao {
    @Query("SELECT * FROM time_slots")
    fun getAll(): List<TimeSlot>

    @Query("SELECT * FROM time_slots WHERE id IN (:timeSlotIds)")
    fun loadAllByIds(timeSlotIds: IntArray): List<TimeSlot>

    @Query("SELECT * FROM time_slots WHERE start_time LIKE :start_time LIMIT 1")
    fun findByStartTime(start_time: String): TimeSlot

    @Query("SELECT * FROM time_slots WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): TimeSlot

    @Query("SELECT * FROM time_slots WHERE end_time LIKE :end_time LIMIT 1")
    fun findByEndTime(end_time: String): TimeSlot

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(timeSlot: TimeSlot): Long

    @Delete
    fun delete(timeSlot: TimeSlot)

    @Query("DELETE FROM time_slots")
    fun deleteAll()

    @Update
    fun update(timeSlot: TimeSlot)


}