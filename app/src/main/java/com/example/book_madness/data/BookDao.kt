package com.example.book_madness.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * from books ORDER BY name ASC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * from books WHERE id = :id")
    fun getBookById(id: Int): Flow<Book>

    @Query("SELECT * from books WHERE name = :name")
    fun getBookByName(name: String): Flow<Book>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: Book)

    @Update
    suspend fun update(book: Book)

    @Delete
    suspend fun delete(book: Book)
}
