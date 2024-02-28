package com.example.book_madness.model

data class BookDetails(
    val id: Int = 0,
    val name: String = "",
    val genre: String = "",
    val rating: String? = null,
    val paper: Boolean = false,
    val startDate: String? = null,
    val finishDate: String? = null,
    val author: String? = null,
    val notes: String? = null
)

val ratingList = listOf(
    "5",
    "4.5",
    "4",
    "3.5",
    "3",
    "2.5",
    "2",
    "1.5",
    "1",
    "0.5"
)
