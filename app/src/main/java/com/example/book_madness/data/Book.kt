package com.example.book_madness.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val genre: String,
    val rating: String = "0",
    val paper: Boolean? = null,
    @ColumnInfo(name = "start_date")
    val startDate: String? = null,
    @ColumnInfo(name = "finish_date")
    val finishDate: String? = null,
    val author: String? = null,
    val notes: String? = null
)
