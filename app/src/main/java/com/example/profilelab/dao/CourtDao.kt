package com.example.profilelab.dao
import androidx.room.*
import com.example.profilelab.entities.Court

@Dao
interface CourtDao {
    @Query("SELECT * FROM courts")
    fun getAll(): List<Court>

    @Query("SELECT * FROM courts WHERE id IN (:courtIds)")
    fun loadAllByIds(courtIds: IntArray): List<Court>

    @Query("SELECT * FROM courts WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Court

    @Query("SELECT * FROM courts WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): Court

    @Query("SELECT * FROM courts WHERE sport_id LIKE :sport_id LIMIT 1")
    fun findBySportId(sport_id: Int): Court

    @Query("SELECT * FROM courts WHERE sport_id LIKE :sport_id")
    fun findBySportIdList(sport_id: Int): List<Court>

    @Query("SELECT * FROM courts WHERE sport_id LIKE :sport_id")
    fun findBySportIdArray(sport_id: Int): Array<Court>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(court: Court): Long

    @Delete
    fun delete(court: Court)

    @Query("DELETE FROM courts")
    fun deleteAll()

    @Update
    fun update(court: Court)
}