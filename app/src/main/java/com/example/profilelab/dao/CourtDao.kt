package com.example.profilelab.dao
import androidx.room.*
import com.example.profilelab.models.Court

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(court: Court): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNew(court: Court): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(courts: List<Court>): List<Long>

    @Delete
    fun delete(court: Court)

    @Query("DELETE FROM courts WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM courts WHERE id IN (:ids)")
    fun deleteByIds(ids: IntArray)


    @Query("DELETE FROM courts")
    fun deleteAll()

    @Query("UPDATE courts SET name = :name WHERE id = :id")
    fun update(id: Int, name: String)
}