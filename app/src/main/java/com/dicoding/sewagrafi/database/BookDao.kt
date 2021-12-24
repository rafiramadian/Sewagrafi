package com.dicoding.sewagrafi.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookDao {
    @Insert
    fun insert(book: Book)

    @Delete
    fun delete(book: Book)

    @Query("SELECT * from book WHERE book.uid = :uid")
    fun getAllBooks(uid: String?) : LiveData<List<Book>>

    @Query("SELECT * from book WHERE book.merk_kamera = :merk")
    fun getBookDetail(merk: String?) : LiveData<List<Book>>
}