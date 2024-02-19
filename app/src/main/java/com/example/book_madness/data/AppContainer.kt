package com.example.book_madness.data

import android.content.Context
import com.example.book_madness.data.source.BookDatabase

// Define which dependencies we have that are provided in this module
// Interface will be useful for testing
interface AppContainer {
    val booksRepository: BooksRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    // With by lazy { } - initialized and created the first time we access it
    override val booksRepository: BooksRepository by lazy {
        OfflineBooksRepository(BookDatabase.getDatabase(context).bookDao())
    }
}
