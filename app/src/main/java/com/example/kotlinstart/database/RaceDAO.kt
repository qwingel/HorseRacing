package com.example.kotlinstart.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceDao {
    @Insert
    suspend fun insertRace(race: Race)

    // Получить все забеги (сортировка по дате)
    @Query("SELECT * FROM Race ORDER BY date DESC")
    fun getAllRaces(): Flow<List<Race>>  // Flow для автоматических обновлений

    // Получить забег по ID
    @Query("SELECT * FROM Race WHERE id = :raceId")
    fun getRaceById(raceId: Int): Flow<Race?>
}