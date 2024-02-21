package com.example.book_madness.data

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
