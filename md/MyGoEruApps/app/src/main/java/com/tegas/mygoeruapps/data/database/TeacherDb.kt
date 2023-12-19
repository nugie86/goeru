package com.tegas.mygoeruapps.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tegas.mygoeruapps.data.response.GuruItem


@Database(entities = [GuruItem::class], version = 2, exportSchema = false)
abstract class TeacherDb: RoomDatabase() {
    abstract fun teacherDao(): TeacherDao

    companion object {
        private const val DATABASE_NAME = "teacher.db"

        @Volatile
        private var instance: TeacherDb? = null

        fun getInstance(context: Context): TeacherDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): TeacherDb {
            return Room.databaseBuilder(context, TeacherDb::class.java, DATABASE_NAME)
                .addMigrations(Migration1to2()) // Add your migration here
                .build()
        }
    }
}