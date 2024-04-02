package com.example.book_madness.util

import com.example.book_madness.R
import com.example.book_madness.data.source.Book
import com.example.book_madness.model.BookDetails
import com.example.book_madness.model.FilterType
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

fun FilterType.getFilterButtonStringResourceId(): Int {
    return when (this) {
        FilterType.ID -> R.string.all_books_filter_button
        FilterType.NAME -> R.string.books_by_name_filter_button
        FilterType.RATING -> R.string.books_by_rating_filter_button
        FilterType.TBR -> R.string.tbr_filter_button
        FilterType.YEAR_2023 -> R.string.books_by_year_2023_filter_button
        FilterType.YEAR_2024 -> R.string.books_by_year_2024_filter_button
    }
}
