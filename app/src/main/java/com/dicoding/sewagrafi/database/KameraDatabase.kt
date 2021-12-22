package com.dicoding.sewagrafi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Kamera::class,Favorite::class],
    version = 1
)
abstract class KameraDatabase : RoomDatabase() {
    abstract fun KameraDao(): KameraDao
    abstract fun FavoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: KameraDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): KameraDatabase {
            if (INSTANCE == null) {
                synchronized(KameraDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        KameraDatabase::class.java,"kamera_db")
                        .build()
                }
            }
            return INSTANCE as KameraDatabase
        }
    }

}