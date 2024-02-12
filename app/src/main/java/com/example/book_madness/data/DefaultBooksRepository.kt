package com.example.book_madness.data

import kotlinx.coroutines.flow.Flow

class DefaultBooksRepository(private val bookDao: BookDao) : BooksRepository {
    override fun getAllBooksStream(): Flow<List<Book>> = bookDao.getAllBooks()

    override fun getBookByIdStream(id: Int): Flow<Book> = bookDao.getBookById(id)

    override fun getBookByNameStream(name: String): Flow<Book> = bookDao.getBookByName(name)

    override suspend fun insertBook(book: Book) = bookDao.insert(book)

    override suspend fun deleteBook(book: Book) = bookDao.delete(book)

    override suspend fun updateBook(book: Book) = bookDao.update(book)
}
