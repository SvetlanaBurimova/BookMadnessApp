package com.example.book_madness.ui.bookItem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.book_madness.model.BookDetails
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.util.toBook

data class BookUiState(
    val bookDetails: BookDetails = BookDetails(),
    val isEntryValid: Boolean = false
)

class BookEntryViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    var bookUiState by mutableStateOf(BookUiState())
        private set

    fun updateUiState(bookDetails: BookDetails) {
        bookUiState = BookUiState(
            bookDetails = bookDetails,
            isEntryValid = validateInput(bookDetails)
        )
    }

    suspend fun saveBook() {
        if (validateInput(bookUiState.bookDetails)) {
            booksRepository.insertBook(bookUiState.bookDetails.toBook())
        }
    }

    private fun validateInput(uiState: BookDetails): Boolean {
        return uiState.name.isNotBlank() && uiState.genre.isNotBlank()
    }
}
