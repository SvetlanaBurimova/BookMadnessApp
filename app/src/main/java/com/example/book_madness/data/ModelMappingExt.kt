package com.example.book_madness.data

import com.example.book_madness.data.source.Book

fun Book.toBookDetails(): BookDetails = BookDetails(
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

fun BookDetails.toBook(): Book = Book(
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
