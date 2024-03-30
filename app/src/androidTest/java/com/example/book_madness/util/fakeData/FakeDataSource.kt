package com.example.book_madness.util.fakeData

import com.example.book_madness.data.source.Book

object FakeDataSource {
    val bookOne =
        Book(id = 1, name = "The Fourth Wing", genre = "Fantasy", rating = "4.5")
    val bookTwo =
        Book(id = 2, name = "One Dark Window", genre = "Fantasy", rating = "5.0", startDate = "13.03.2024", finishDate = "24.03.2024", paper = true)
    val bookThree =
        Book(id = 3, name = "Test name", genre = "Detective", startDate = "26.03.2024")

    val bookList = listOf(
        bookOne, bookTwo, bookThree
    )
}
