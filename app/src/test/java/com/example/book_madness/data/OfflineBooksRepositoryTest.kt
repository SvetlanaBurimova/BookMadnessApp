package com.example.book_madness.data

import com.example.book_madness.data.source.BookDao
import com.example.book_madness.util.fakeData.FakeDataSource.bookList
import com.example.book_madness.util.fakeData.FakeDataSource.bookOne
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class OfflineBooksRepositoryTest {
    @Mock
    private lateinit var mockBookDao: BookDao
    private lateinit var offlineBooksRepository: OfflineBooksRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        offlineBooksRepository = OfflineBooksRepository(mockBookDao)
    }

    @Test
    fun testGetAllBooksStream() = runBlocking {
        `when`(mockBookDao.getAllBooks()).thenReturn(flowOf(bookList))
        val actualBooks = offlineBooksRepository.getAllBooksStream().toList().first()

        assertEquals(bookList, actualBooks)
    }

    @Test
    fun testGetAllBooksOrderedByNameStream() = runBlocking {
        `when`(mockBookDao.getAllBooksOrderedByName()).thenReturn(flowOf(bookList))
        val actualBooks = offlineBooksRepository.getAllBooksOrderedByNameStream().toList().first()

        assertEquals(bookList, actualBooks)
    }

    @Test
    fun testGetAllBooksOrderedByRatingStream() = runBlocking {
        `when`(mockBookDao.getAllBooksOrderedByRating()).thenReturn(flowOf(bookList))
        val actualBooks = offlineBooksRepository.getAllBooksOrderedByRatingStream().toList().first()

        assertEquals(bookList, actualBooks)
    }

    @Test
    fun testGetAllBooksWithRating() = runBlocking {
        val rating = "5"
        `when`(mockBookDao.getAllBooksWithRating(rating)).thenReturn(flowOf(bookList))

        val actualBooks = offlineBooksRepository.getAllBooksWithRating(rating).toList().first()

        assertEquals(bookList, actualBooks)
    }

    @Test
    fun testGetAllBooksWithoutStartAndFinishDateStream() = runBlocking {
        `when`(mockBookDao.getAllBooksWithoutStartAndFinishDate()).thenReturn(flowOf(bookList))

        val actualBooks = offlineBooksRepository.getAllBooksWithoutStartAndFinishDateStream().toList().first()

        assertEquals(bookList, actualBooks)
    }

    @Test
    fun testGetAllFinishedBooksByYearStream() = runBlocking {
        val year = "2023"
        `when`(mockBookDao.getAllFinishedBooksByYear(year)).thenReturn(flowOf(bookList))
        val actualBooks = offlineBooksRepository.getAllFinishedBooksByYearStream(year).toList().first()

        assertEquals(bookList, actualBooks)
    }

    @Test
    fun testGetBookByIdStream() = runBlocking {
        val bookId = 1
        `when`(mockBookDao.getBookById(bookId)).thenReturn(flowOf(bookOne))
        val actualBook = offlineBooksRepository.getBookByIdStream(bookId).toList().first()

        assertEquals(bookOne, actualBook)
    }

    @Test
    fun testGetBookByNameStream() = runBlocking {
        val bookName = "Book 1"
        `when`(mockBookDao.getBookByName(bookName)).thenReturn(flowOf(bookOne))
        val actualBook = offlineBooksRepository.getBookByNameStream(bookName).toList().first()

        assertEquals(bookOne, actualBook)
    }

    @Test
    fun testInsertBook() = runBlocking {
        offlineBooksRepository.insertBook(bookOne)

        verify(mockBookDao).insert(bookOne)
    }

    @Test
    fun testDeleteBook() = runBlocking {
        offlineBooksRepository.deleteBook(bookOne)

        verify(mockBookDao).delete(bookOne)
    }

    @Test
    fun testUpdateBook() = runBlocking {
        offlineBooksRepository.updateBook(bookOne)

        verify(mockBookDao).update(bookOne)
    }
}