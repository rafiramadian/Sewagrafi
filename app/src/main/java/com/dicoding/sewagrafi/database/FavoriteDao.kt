package com.dicoding.sewagrafi.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    fun insert(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * from favorite WHERE favorite.uid = :uid")
    fun getAllFavorites(uid: String?): LiveData<List<Favorite>>

    @Query("SELECT count(*) from favorite WHERE favorite.merk_kamera = :merk")
    suspend fun checkKamera(merk: String) : Int
}