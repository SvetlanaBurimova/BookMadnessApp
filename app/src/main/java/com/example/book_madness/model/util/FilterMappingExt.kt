package com.example.book_madness.model.util

import com.example.book_madness.R

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

enum class FilterType {
    ID,
    NAME,
    RATING,
    TBR,
    YEAR_2023,
    YEAR_2024
}

val filterOptions = listOf(
    FilterType.ID,
    FilterType.NAME,
    FilterType.RATING,
    FilterType.TBR,
    FilterType.YEAR_2023,
    FilterType.YEAR_2024
)
