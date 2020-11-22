package com.fizarum.get10kusd.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fizarum.get10kusd.data.db.dao.DailyWageDao
import com.fizarum.get10kusd.data.db.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dailyWageDao(): DailyWageDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private const val name = "app_db"

        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context, name).also {
                instance = it
            }
        }

        @Suppress("SameParameterValue")
        private fun buildDatabase(appContext: Context, name: String): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, name)
                .fallbackToDestructiveMigration().build()
        }
    }
}