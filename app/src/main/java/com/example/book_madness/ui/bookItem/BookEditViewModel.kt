package com.example.book_madness.ui.bookItem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.model.BookDetails
import com.example.book_madness.ui.navigation.BookMadnessDestinationsArgs
import com.example.book_madness.util.toBook
import com.example.book_madness.util.toBookUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val booksRepository: BooksRepository
) : ViewModel() {
    var bookUiState by mutableStateOf(BookUiState())
        private set

    private val bookId: Int =
        checkNotNull(savedStateHandle[BookMadnessDestinationsArgs.BOOK_ID_ARG])

    init {
        viewModelScope.launch {
            bookUiState = booksRepository.getBookByIdStream(bookId)
                .filterNotNull()
                .first()
                .toBookUiState(true)
        }
    }

    suspend fun saveBook() {
        if (validateInput(bookUiState.bookDetails)) {
            booksRepository.updateBook(bookUiState.bookDetails.toBook())
        }
    }

    fun updateUiState(bookDetails: BookDetails) {
        bookUiState =
            BookUiState(
                bookDetails = bookDetails,
                isEntryValid = validateInput(bookDetails)
            )
    }

    private fun validateInput(uiState: BookDetails): Boolean {
        return uiState.name.isNotBlank() && uiState.genre.isNotBlank()
    }
}
