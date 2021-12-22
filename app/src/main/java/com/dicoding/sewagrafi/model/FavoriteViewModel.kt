package com.dicoding.sewagrafi.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.sewagrafi.database.Favorite
import com.dicoding.sewagrafi.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val mFavoritesRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorite(uid: String?): LiveData<List<Favorite>> = mFavoritesRepository.getAllFavorites(uid)

    suspend fun checkKamera(merk: String) : Int = mFavoritesRepository.checkKamera(merk)

    fun insert(favorite: Favorite) {
        mFavoritesRepository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        mFavoritesRepository.delete(favorite)
    }
}