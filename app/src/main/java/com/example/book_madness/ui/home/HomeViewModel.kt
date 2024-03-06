package com.example.book_madness.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.source.Book
import com.example.book_madness.model.FilterType
import com.example.book_madness.util.WhileUiSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(val bookList: List<Book> = listOf())

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    private var filterType = MutableStateFlow(FilterType.ID)

    fun filterBooks(filter: FilterType) {
        filterType.value = filter
    }

    val homeUiState = filterType.flatMapLatest { type ->
        when(type) {
            FilterType.ID -> booksRepository.getAllBooksStream()
            FilterType.NAME -> booksRepository.getAllBooksOrderedByNameStream()
            FilterType.RATING -> booksRepository.getAllBooksOrderedByRatingStream()
            FilterType.TBR -> booksRepository.getAllBooksWithoutStartAndFinishDateStream()
            FilterType.YEAR_2023 -> booksRepository.getAllFinishedBooksByYearStream("%2023")
            FilterType.YEAR_2024 -> booksRepository.getAllFinishedBooksByYearStream("%2024")
        }.map { HomeUiState(it) }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = HomeUiState()
    )

    suspend fun deleteItem(book: Book) {
        booksRepository.deleteBook(book)
    }
}
