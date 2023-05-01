package com.example.profilelab.dao

import androidx.room.*
import com.example.profilelab.models.CourtSport
import com.example.profilelab.models.Sport
import com.example.profilelab.view_models.CourtSportIdViewModel

@Dao
interface CourtSportsDao {
    @Query("SELECT * FROM court_sports")
    fun getAll(): List<CourtSport>

    @Query("SELECT * FROM court_sports WHERE id = :id")
    fun getById(id: Int): CourtSport

    @Query("SELECT * FROM court_sports WHERE court_id = :courtId")
    fun getByCourtId(courtId: Int): List<CourtSport>

    @Query("SELECT * FROM court_sports WHERE sport_id = :sportId")
    fun getBySportId(sportId: Int): List<CourtSport>

    @Query("SELECT * FROM court_sports WHERE court_id = :courtId AND sport_id = :sportId")
    fun getByCourtIdAndSportId(courtId: Int, sportId: Int): CourtSportIdViewModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(courtSport: CourtSport): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(courtSports: List<CourtSport>): List<Long>

    @Query("DELETE FROM court_sports")
    fun deleteAll()

    @Query("DELETE FROM court_sports WHERE court_id = :courtId")
    fun deleteByCourtId(courtId: Int)

    @Query("DELETE FROM court_sports WHERE sport_id = :sportId")
    fun deleteBySportId(sportId: Int)

    @Query("DELETE FROM court_sports WHERE court_id = :courtId AND sport_id = :sportId")
    fun deleteByCourtIdAndSportId(courtId: Int, sportId: Int)

    @Query("SELECT * FROM court_sports "+
            "INNER JOIN sports ON court_sports.sport_id = sports.id "+
            "WHERE court_id = :courtId")
    fun getInDetail(courtId: Int): List<Sport>

    @Delete
    fun delete(courtSport: CourtSport)

    @Update
    fun update(courtSport: CourtSport)

}