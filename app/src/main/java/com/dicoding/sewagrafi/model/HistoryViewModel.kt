package com.dicoding.sewagrafi.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.sewagrafi.database.Book
import com.dicoding.sewagrafi.database.History
import com.dicoding.sewagrafi.repository.HistoryRepository

class HistoryViewModel(application: Application) : ViewModel() {

    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    suspend fun checkHistory(
        merk: String?,
        uid: String?,
        tanggal_pinjam: String?,
        tanggal_kembali: String?,
        waktu_pinjam: String?,
        waktu_kembali: String?
    ): Int = mHistoryRepository.checkHistory(merk,uid,tanggal_pinjam,tanggal_kembali,waktu_pinjam,waktu_kembali)

    fun getAllHistory(uid: String?): LiveData<List<History>> = mHistoryRepository.getAllHistory(uid)

    fun insert(history: History) {
        mHistoryRepository.insert(history)
    }
}