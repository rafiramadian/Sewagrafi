package com.dicoding.sewagrafi.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.sewagrafi.database.Favorite
import com.dicoding.sewagrafi.database.FavoriteDao
import com.dicoding.sewagrafi.database.KameraDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {

    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = KameraDatabase.getDatabase(application)
        mFavoriteDao = db.FavoriteDao()
    }

    fun getAllFavorites(uid: String?): LiveData<List<Favorite>> = mFavoriteDao.getAllFavorites(uid)

    suspend fun checkKamera(merk: String) : Int = mFavoriteDao.checkKamera(merk)

    fun insert(favorite: Favorite) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }

    fun delete(favorite: Favorite) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }

}