package com.dicoding.sewagrafi.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.sewagrafi.database.Book
import com.dicoding.sewagrafi.database.BookDao
import com.dicoding.sewagrafi.database.KameraDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BookRepository(application: Application) {

    private val mBookDao: BookDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = KameraDatabase.getDatabase(application)
        mBookDao = db.BookDao()
    }

    fun getAllBooks(uid: String?): LiveData<List<Book>> = mBookDao.getAllBooks(uid)

    fun getBookDetail(merk: String?): LiveData<List<Book>> = mBookDao.getBookDetail(merk)

    fun insert(book: Book) {
        executorService.execute { mBookDao.insert(book) }
    }

    fun delete(book: Book) {
        executorService.execute { mBookDao.delete(book) }
    }
}