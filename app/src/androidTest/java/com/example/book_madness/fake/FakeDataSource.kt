package com.example.book_madness.fake

import com.example.book_madness.data.source.Book

object FakeDataSource {
    val book1 =
        Book(id = 1, name = "The Fourth Wing", genre = "Fantasy", rating = "5.0")
    val book2 =
        Book(id = 2, name = "One Dark Window", genre = "Fantasy", startDate = "13.03.2024")

    val bookList = listOf(
        book1, book2
    )
}
