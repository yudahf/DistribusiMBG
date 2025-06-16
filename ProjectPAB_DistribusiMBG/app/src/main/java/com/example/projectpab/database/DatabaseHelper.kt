package com.example.projectpab.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create table for sekolah if not exists
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS sekolah (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nama TEXT NOT NULL
            )
        """)

        // Create table for jadwal_pengiriman
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS jadwal_pengiriman (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tanggal_pengiriman TEXT NOT NULL,
                id_sekolah INTEGER NOT NULL,
                FOREIGN KEY (id_sekolah) REFERENCES sekolah(id)
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS jadwal_pengiriman")
        db.execSQL("DROP TABLE IF EXISTS sekolah")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "distribusi_mbg.db"
        private const val DATABASE_VERSION = 1
    }
}
