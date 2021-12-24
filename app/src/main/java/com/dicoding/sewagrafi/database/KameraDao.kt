package com.dicoding.sewagrafi.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface KameraDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(kamera: Kamera)

    @Query("SELECT * from kamera")
    fun getAllKamera() : LiveData<List<Kamera>>

    @Query("SELECT * from kamera WHERE kamera.merk_kamera = :merk")
    fun getKamera(merk: String?) : LiveData<List<Kamera>>

    @Update
    fun update(kamera: Kamera)
}