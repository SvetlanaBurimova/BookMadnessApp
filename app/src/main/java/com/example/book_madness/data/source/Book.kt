package com.example.book_madness.data.source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val genre: String,
    val rating: String? = null,
    val paper: Boolean = false,
    @ColumnInfo(name = "start_date")
    val startDate: String? = null,
    @ColumnInfo(name = "finish_date")
    val finishDate: String? = null,
    val author: String? = null,
    val notes: String? = null
)
