package com.example.kotlinstart.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Race::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun raceDao(): RaceDao
}