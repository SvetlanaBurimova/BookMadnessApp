package com.example.book_madness.data

data class BookDetails(
    val id: Int = 0,
    val name: String = "",
    val genre: String = "",
    val rating: String = "",
    val paper: Boolean? = false,
    val startDate: String? = "",
    val finishDate: String? = "",
    val author: String? = "",
    val notes: String? = ""
)
