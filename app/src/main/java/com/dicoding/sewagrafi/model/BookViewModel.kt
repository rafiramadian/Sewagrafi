package com.dicoding.sewagrafi.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.sewagrafi.database.Book
import com.dicoding.sewagrafi.repository.BookRepository

class BookViewModel(application: Application) : ViewModel() {

    private val mBookRepository: BookRepository = BookRepository(application)

    fun getAllBooks(uid: String?): LiveData<List<Book>> = mBookRepository.getAllBooks(uid)

    fun getBookDetail(merk: String): LiveData<List<Book>> = mBookRepository.getBookDetail(merk)

    fun insert(book: Book) {
        mBookRepository.insert(book)
    }

    fun delete(book: Book) {
        mBookRepository.delete(book)
    }
}