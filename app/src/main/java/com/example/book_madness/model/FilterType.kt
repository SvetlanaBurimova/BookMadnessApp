package com.example.book_madness.model

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
