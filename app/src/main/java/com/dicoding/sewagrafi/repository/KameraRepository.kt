package com.dicoding.sewagrafi.repository

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.dicoding.sewagrafi.database.Kamera
import com.dicoding.sewagrafi.database.KameraDao
import com.dicoding.sewagrafi.database.KameraDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class KameraRepository(context: Context) {
    private val mKameraDao: KameraDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = KameraDatabase.getDatabase(context)
        mKameraDao = db.KameraDao()
    }

    fun getAllKamera() : LiveData<List<Kamera>> = mKameraDao.getAllKamera()

    fun insert(kamera: Kamera) {
        executorService.execute { mKameraDao.insert(kamera) }
    }

}