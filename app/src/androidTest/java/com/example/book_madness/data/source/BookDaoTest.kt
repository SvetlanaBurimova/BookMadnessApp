package com.example.book_madness.data.source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.book_madness.util.fakeData.FakeDataSource.bookOne
import com.example.book_madness.util.fakeData.FakeDataSource.bookTwo
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
        assertEquals(allItems[0], bookOne)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllBooks_returnsAllBooksFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = bookDao.getAllBooks().first()
        assertEquals(allItems[0], bookOne)
        assertEquals(allItems[1], bookTwo)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetBook_returnsBookFromDB() = runBlocking {
        addOneItemToDb()
        val item = bookDao.getBookById(1)
        assertEquals(item.first(), bookOne)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteBooks_deletesAllBooksFromDB() = runBlocking {
        addTwoItemsToDb()
        bookDao.delete(bookOne)
        bookDao.delete(bookTwo)
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
        bookDao.insert(bookOne)
    }

    private suspend fun addTwoItemsToDb() {
        bookDao.insert(bookOne)
        bookDao.insert(bookTwo)
    }
}
