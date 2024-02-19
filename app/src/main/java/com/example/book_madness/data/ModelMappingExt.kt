package com.example.book_madness.data

import com.example.book_madness.data.source.Book

fun Book.toBookDetails(): BookDetailsModel = BookDetailsModel(
    id = id,
    name = name,
    genre = genre,
    rating = rating,
    paper = paper,
    startDate = startDate,
    finishDate = finishDate,
    author = author,
    notes = notes
)

fun BookDetailsModel.toBook(): Book = Book(
    id = id,
    name = name,
    genre = genre,
    rating = rating,
    paper = paper,
    startDate = startDate,
    finishDate = finishDate,
    author = author,
    notes = notes
)
