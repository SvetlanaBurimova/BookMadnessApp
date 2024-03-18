package com.example.book_madness

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.book_madness.data.source.Book
import com.example.book_madness.data.source.BookDao
import com.example.book_madness.data.source.BookDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var bookDao: BookDao
    private lateinit var bookDatabase: BookDatabase
    private val book1 =
        Book(id = 1, name = "The Fourth Wing", genre = "Fantasy", rating = "5.0")
    private val book2 =
        Book(id = 2, name = "One Dark Window", genre = "Fantasy", startDate = "13.03.2024")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        bookDatabase = Room.inMemoryDatabaseBuilder(context, BookDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        bookDao = bookDatabase.bookDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        bookDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsBookIntoDB() = runBlocking {
        addOneItemToDb()
        val allItems = bookDao.getAllBooks().first()
        assertEquals(allItems[0], book1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllBooks_returnsAllBooksFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = bookDao.getAllBooks().first()
        assertEquals(allItems[0], book1)
        assertEquals(allItems[1], book2)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetBook_returnsBookFromDB() = runBlocking {
        addOneItemToDb()
        val item = bookDao.getBookById(1)
        assertEquals(item.first(), book1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteBooks_deletesAllBooksFromDB() = runBlocking {
        addTwoItemsToDb()
        bookDao.delete(book1)
        bookDao.delete(book2)
        val allItems = bookDao.getAllBooks().first()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateBooks_updatesBooksInDB() = runBlocking {
        addTwoItemsToDb()
        bookDao.update(
            Book(
                id = 1,
                name = "The Hero of Ages",
                genre = "Fantasy",
                rating = "3"
            )
        )
        bookDao.update(
            Book(
                id = 2,
                name = "One Dark Window",
                genre = "Fantasy",
                startDate = "13.03.2024",
                paper = true
            )
        )

        val allItems = bookDao.getAllBooks().first()
        assertEquals(
            allItems[0],
            Book(
                id = 1,
                name = "The Hero of Ages",
                genre = "Fantasy",
                rating = "3"
            )
        )
        assertEquals(
            allItems[1],
            Book(
                id = 2,
                name = "One Dark Window",
                genre = "Fantasy",
                startDate = "13.03.2024",
                paper = true
            )
        )
    }

    private suspend fun addOneItemToDb() {
        bookDao.insert(book1)
    }

    private suspend fun addTwoItemsToDb() {
        bookDao.insert(book1)
        bookDao.insert(book2)
    }
}
