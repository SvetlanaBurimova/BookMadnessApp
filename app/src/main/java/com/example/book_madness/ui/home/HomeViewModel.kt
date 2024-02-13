package com.example.book_madness.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_madness.data.source.Book
import com.example.book_madness.data.BooksRepository
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(val bookList: List<Book> = listOf())

class HomeViewModel(booksRepository: BooksRepository) : ViewModel() {
    val homeUiState: StateFlow<HomeUiState> =
        booksRepository.getAllBooksStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
