package com.dicoding.sewagrafi.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.sewagrafi.database.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository(application: Application) {

    private val mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = KameraDatabase.getDatabase(application)
        mHistoryDao = db.HistoryDao()
    }

    suspend fun checkHistory(
        merk: String?,
        uid: String?,
        tanggal_pinjam: String?,
        tanggal_kembali: String?,
        waktu_pinjam: String?,
        waktu_kembali: String?
    ): Int = mHistoryDao.checkHistory(merk,uid,tanggal_pinjam,tanggal_kembali,waktu_pinjam,waktu_kembali)

    fun getAllHistory(uid: String?): LiveData<List<History>> = mHistoryDao.getAllHistory(uid)

    fun insert(history: History) {
        executorService.execute { mHistoryDao.insert(history) }
    }

}