package com.example.book_madness.ui.bookItem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.book_madness.data.BookDetails
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.toBook

data class BookUiState(
    val bookDetails: BookDetails = BookDetails(),
    val isEntryValid: Boolean = false
)

class BookEntryViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    var bookUiState by mutableStateOf(BookUiState())
        private set

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

    /**
     * Updates the [bookUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(bookDetails: BookDetails) {
        bookUiState = BookUiState(
            bookDetails = bookDetails,
            isEntryValid = validateInput(bookDetails)
        )
    }

    suspend fun saveBook() {
        if (validateInput()) {
            booksRepository.insertBook(bookUiState.bookDetails.toBook())
        }
    }

    private fun validateInput(uiState: BookDetails = bookUiState.bookDetails): Boolean {
        return uiState.name.isNotBlank() && uiState.genre.isNotBlank()
    }
}
