package com.example.profilelab.dao

import androidx.room.*
import com.example.profilelab.entities.Sport

@Dao
interface SportDao {
    @Query("SELECT * FROM sports")
    fun getAll(): List<Sport>

    @Query("SELECT * FROM sports WHERE id IN (:sportIds)")
    fun loadAllByIds(sportIds: IntArray): List<Sport>

    @Query("SELECT * FROM sports WHERE title LIKE :title LIMIT 1")
    fun findByName(title: String): Sport

    @Query("SELECT * FROM sports WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): Sport

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(sport: Sport): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(sports: List<Sport>): List<Long>

    @Delete
    fun delete(sport: Sport)

    @Query("DELETE FROM sports")
    fun deleteAll()

    @Update
    fun update(sport: Sport)

    @Query("UPDATE sports SET title = :title WHERE id = :id")
    fun update(id: Int, title: String)


}