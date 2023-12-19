package com.tegas.mygoeruapps.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1to2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Perform the necessary migration operations here
        // For example, you can create the new columns
        database.execSQL("ALTER TABLE teacher ADD COLUMN harga TEXT")
        database.execSQL("ALTER TABLE teacher ADD COLUMN gambar TEXT")
    }
}