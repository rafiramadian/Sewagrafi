package com.dicoding.sewagrafi.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    @Insert
    fun insert(history: History)

    @Query("SELECT * from history WHERE history.uid = :uid")
    fun getAllHistory(uid: String?) : LiveData<List<History>>

    @Query("SELECT count(*) from history WHERE history.merk_kamera = :merk AND history.uid = :uid AND history.tanggal_pinjam = :tanggalPinjam AND history.tanggal_kembali = :tanggalKembali AND history.jam_pinjam = :waktuPinjam AND history.jam_kembali = :waktuKembali")
    suspend fun checkHistory(
        merk: String?,
        uid: String?,
        tanggalPinjam: String?,
        tanggalKembali: String?,
        waktuPinjam: String?,
        waktuKembali: String?
    ) : Int
}