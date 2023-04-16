package com.example.profilelab.dao
import androidx.room.*
import com.example.profilelab.entities.Reservation

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations")
    fun getAll(): List<Reservation>

    @Query("SELECT * FROM reservations WHERE id IN (:reservationIds)")
    fun loadAllByIds(reservationIds: IntArray): List<Reservation>

    @Query("SELECT * FROM reservations WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Reservation

    @Query("SELECT * FROM reservations WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): Reservation

    @Query("SELECT * FROM reservations WHERE time LIKE :time LIMIT 1")
    fun findByTime(time: String): Reservation

    @Query("SELECT * FROM reservations WHERE status LIKE :status LIMIT 1")
    fun findByStatus(status: Boolean): Reservation

    @Query("SELECT * FROM reservations WHERE time_slot_id LIKE :time_slot_id LIMIT 1")
    fun findByTimeSlotId(time_slot_id: Int): Reservation

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(reservation: Reservation): Long

    @Delete
    fun delete(reservation: Reservation)

    @Query("DELETE FROM reservations")
    fun deleteAll()

    @Update
    fun update(reservation: Reservation)
}