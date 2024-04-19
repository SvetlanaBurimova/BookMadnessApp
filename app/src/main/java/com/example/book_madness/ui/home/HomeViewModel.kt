package com.example.book_madness.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.source.Book
import com.example.book_madness.model.util.FilterType
import com.example.book_madness.util.WhileUiSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(val bookList: List<Book> = listOf())

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var filterType = MutableStateFlow(FilterType.ID)
        private set

    fun filterBooks(filter: FilterType) {
        filterType.value = filter
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    val homeUiState =
        filterType.flatMapLatest { type ->
            snapshotFlow { searchQuery }.combine(
                when (type) {
                    FilterType.ID -> booksRepository.getAllBooksStream()
                    FilterType.NAME -> booksRepository.getAllBooksOrderedByNameStream()
                    FilterType.RATING -> booksRepository.getAllBooksOrderedByRatingStream()
                    FilterType.TBR -> booksRepository.getAllBooksWithoutStartAndFinishDateStream()
                    FilterType.YEAR_2023 -> booksRepository.getAllFinishedBooksByYearStream("%2023")
                    FilterType.YEAR_2024 -> booksRepository.getAllFinishedBooksByYearStream("%2024")
                }
            ) { searchQuery, books ->
                when {
                    searchQuery.isNotEmpty() -> books.filter { book ->
                            book.name.contains(searchQuery, ignoreCase = true) }
                    else -> books
                }
            }.map { HomeUiState(it) }
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = HomeUiState()
        )

    suspend fun deleteBook(book: Book) {
        booksRepository.deleteBook(book)
    }
}
