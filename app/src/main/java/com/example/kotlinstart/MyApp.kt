package com.example.kotlinstart

import android.app.Application
import androidx.room.Room
import com.example.kotlinstart.database.AppDatabase

class MyApp : Application() {  // Наследуемся от Application
    companion object {
        lateinit var database: AppDatabase  // Общая база данных для всего приложения
            private set
    }

    override fun onCreate() {
        super.onCreate()
        // Инициализируем Room при старте приложения
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "race_db"  // Имя файла БД
        ).build()
    }
}
