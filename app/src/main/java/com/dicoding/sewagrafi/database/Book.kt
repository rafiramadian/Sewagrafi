package com.dicoding.sewagrafi.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Book(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "merk_kamera")
    var merk_kamera: String? = null,

    @ColumnInfo(name = "uid")
    var uid: String? = null,

    @ColumnInfo(name = "tanggal_pinjam")
    var tanggal_pinjam: String? = null,

    @ColumnInfo(name = "tanggal_kembali")
    var tanggal_kembali: String? = null,

    @ColumnInfo(name = "jam_pinjam")
    var waktu_pinjam: String? = null,

    @ColumnInfo(name = "jam_kembali")
    var waktu_kembali: String? = null

) : Parcelable