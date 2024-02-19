package com.example.book_madness.ui.bookItem

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_madness.data.BookDetailsModel
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.toBook
import com.example.book_madness.data.toBookDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class BookDetailsUiState(val bookDetails: BookDetailsModel? = null)

class BookDetailsViewModel(
// This object is a key-value map that lets write and retrieve objects to and from the saved state.
// These values persist after the process is killed by the system and remain available through the same object.
    savedStateHandle: SavedStateHandle,
    private val booksRepository: BooksRepository,
): ViewModel() {

    private val bookId: Int = checkNotNull(savedStateHandle["bookId"])

    val uiState: StateFlow<BookDetailsUiState> =
        booksRepository.getBookByIdStream(bookId)
            .filterNotNull()
            .map {
                BookDetailsUiState(bookDetails = it.toBookDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BookDetailsUiState()
            )

    suspend fun deleteItem() {
        booksRepository.deleteBook(uiState.value.bookDetails!!.toBook())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
