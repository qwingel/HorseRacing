package com.example.kotlinstart.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Race")
data class Race(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Int,
    val time: Int,
    val winner: Int,
    val duration: Double
)