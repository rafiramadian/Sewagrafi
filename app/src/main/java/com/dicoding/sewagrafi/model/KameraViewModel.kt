package com.dicoding.sewagrafi.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.sewagrafi.R
import com.dicoding.sewagrafi.database.Kamera
import com.dicoding.sewagrafi.repository.KameraRepository

class KameraViewModel(context: Context): ViewModel() {
    private val mKameraRepository: KameraRepository = KameraRepository(context)

    fun getAllKamera(): LiveData<List<Kamera>> = mKameraRepository.getAllKamera()

    fun getKamera(merk: String?): LiveData<List<Kamera>> = mKameraRepository.getKamera(merk)

    fun insert(kamera: Kamera) {
        mKameraRepository.insert(kamera)
    }

    fun update(kamera: Kamera) {
        mKameraRepository.update(kamera)
    }
}