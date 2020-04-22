package com.example.mywisatadestination.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.mywisatadestination.data.model.Lokasi
import org.jetbrains.anko.db.*

class FavoriteDatabase(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FavoriteMatch.db", null, 1) {
    companion object {
        private var instance: FavoriteDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): FavoriteDatabase {
            if (instance == null) {
                instance =
                    FavoriteDatabase(
                        ctx.applicationContext
                    )
            }
            return instance as FavoriteDatabase
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(
            Lokasi.TABLE_FAVORITE, true,
            Lokasi.ID to INTEGER + PRIMARY_KEY,
            Lokasi.DESKRIPSI to TEXT,
            Lokasi.FOTO to TEXT,
            Lokasi.LATITUDE to REAL,
            Lokasi.LONGITUDE to REAL,
            Lokasi.NAMA to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(Lokasi.TABLE_FAVORITE, true)
    }
}