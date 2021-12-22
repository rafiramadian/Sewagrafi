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

    fun insert(kamera: Kamera) {
        mKameraRepository.insert(kamera)
    }



//    private val listKamera = MutableLiveData<ArrayList<Kamera>>()
//
//    init {
//        val nama = context.resources.getStringArray(R.array.nama)
//        val jenis = context.resources.getStringArray(R.array.jenis)
//        val hargaSewa = context.resources.getStringArray(R.array.harga_sewa)
//        val spesifikasi = context.resources.getStringArray(R.array.spesifikasi)
//        val status = 1
//        val gambar = context.resources.obtainTypedArray(R.array.gambar)
//        val list = ArrayList<Kamera>()
//        for (i in nama.indices) {
//            val kamera = Kamera(
//                nama[i],
//                jenis[i],
//                hargaSewa[i],
//                spesifikasi[i],
//                status,
//                gambar.getResourceId(i,0)
//            )
//            list.add(kamera)
//        }
//        gambar.recycle()
//        listKamera.postValue(list)
//    }
//
//    fun getKamera(): LiveData<ArrayList<Kamera>> {
//        return listKamera
//    }
}