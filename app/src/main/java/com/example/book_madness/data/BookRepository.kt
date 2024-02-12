package com.example.book_madness.data

import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    fun getAllBooksStream(): Flow<List<Book>>

    fun getBookByIdStream(id: Int): Flow<Book>

    fun getBookByNameStream(name: String): Flow<Book>

    suspend fun insertBook(book: Book)

    suspend fun deleteBook(book: Book)

    suspend fun updateBook(book: Book)
}
