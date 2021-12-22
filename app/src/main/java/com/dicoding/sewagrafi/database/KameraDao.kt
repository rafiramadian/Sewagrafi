package com.dicoding.sewagrafi.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KameraDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(kamera: Kamera)

    @Query("SELECT * from kamera")
    fun getAllKamera() : LiveData<List<Kamera>>
}