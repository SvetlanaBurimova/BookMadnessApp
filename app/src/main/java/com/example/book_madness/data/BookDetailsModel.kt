package com.example.book_madness.data

data class BookDetailsModel(
    val id: Int,
    val name: String = "",
    val genre: String = "",
    val rating: String = "",
    val paper: Boolean? = false,
    val startDate: String? = null,
    val finishDate: String? = "",
    val author: String? = "",
    val notes: String? = ""
)
