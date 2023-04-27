package com.example.profilelab.dao
import androidx.room.*
import com.example.profilelab.entities.FullReservation
import com.example.profilelab.entities.Reservation

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations")
    fun getAll(): List<Reservation>

    @Query("SELECT * FROM reservations WHERE id IN (:reservationIds)")
    fun loadAllByIds(reservationIds: IntArray): List<Reservation>

    @Query("SELECT * FROM reservations WHERE user_id = :userId")
    fun findByUserId(userId: Int): List<Reservation>

    @Query("SELECT * FROM reservations WHERE court_sports_id = :courtSportsId")
    fun findByCourtSportsId(courtSportsId: Int): List<Reservation>

    @Query("SELECT * FROM reservations WHERE time_slot_id = :timeSlotId")
    fun findByTimeSlotId(timeSlotId: Int): List<Reservation>

    @Query("SELECT * FROM reservations WHERE status = :status")
    fun findByStatus(status: Boolean): List<Reservation>

    @Query("SELECT time_slot_id FROM reservations WHERE date_ = :date_")
    fun findByDate(date_: String): List<Int>

    @Query("SELECT * FROM reservations " +
            "INNER JOIN court_sports ON reservations.court_sports_id = court_sports.id " +
            "INNER JOIN time_slots ON reservations.time_slot_id = time_slots.id " +
            "INNER JOIN courts ON court_sports.court_id = courts.id " +
            "INNER JOIN sports ON court_sports.sport_id = sports.id "+
            "ORDER BY date_ DESC")
    fun getInDetails(): List<FullReservation>

    @Insert
    fun insert(reservation: Reservation) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(reservations: List<Reservation>): List<Long>

    @Insert
    fun insertAll(vararg reservations: Reservation)

    @Delete
    fun delete(reservation: Reservation)

    @Update
    fun update(reservation: Reservation)

    @Query("UPDATE reservations SET status = :status WHERE id = :id")
    fun update(id: Int, status: Boolean)

    @Query("DELETE FROM reservations")
    fun deleteAll()

}