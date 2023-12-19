package com.tegas.mygoeruapps.data.database

import android.content.Context
import androidx.room.Room

class DataBaseModule(private val ctx: Context) {
    private val db = Room.databaseBuilder(ctx, TeacherDb::class.java, "teacher.db")
        .allowMainThreadQueries()
        .build()

    val teacherDao = db.teacherDao()
}