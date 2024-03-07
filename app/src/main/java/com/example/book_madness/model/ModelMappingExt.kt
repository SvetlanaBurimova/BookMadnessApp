package com.example.book_madness.model

import com.example.book_madness.data.source.Book
import com.example.book_madness.ui.bookItem.BookUiState
import java.util.concurrent.TimeUnit

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

fun Book.toBookUiState(isEntryValid: Boolean): BookUiState = BookUiState(
    bookDetails = this.toBookDetails(),
    isEntryValid = isEntryValid
)

fun Long.toFormatTime(): String = String.format(
    "%02d:%02d",
    TimeUnit.MILLISECONDS.toMinutes(this),
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)
