package com.dicoding.sewagrafi.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Kamera(
    @PrimaryKey
    @ColumnInfo(name = "merk_kamera")
    var merk: String,

    @ColumnInfo(name = "jenis_kamera")
    var jenis: String? = null,

    @ColumnInfo(name = "harga_sewa")
    var hargaSewa: String? = null,

    @ColumnInfo(name = "spesifikasi")
    var spesifikasi: String? = null,

    @ColumnInfo(name = "status")
    var status: Int? = 0,

    @ColumnInfo(name = "gambar_kamera")
    var gambar: Int? = 0
) : Parcelable