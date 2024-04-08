package com.example.book_madness.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.source.Book
import com.example.book_madness.model.StatisticDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class StatisticsUiState(
    val statisticDetails: StatisticDetails = StatisticDetails()
)

class StatisticsViewModel(
    booksRepository: BooksRepository
) : ViewModel() {

    val statisticsUiState: StateFlow<StatisticsUiState> = MutableStateFlow(StatisticsUiState())

    init {
        viewModelScope.launch {
            booksRepository.getAllBooksStream().collect { books ->
                val statisticDetails = getStatisticDetails(books)
                (statisticsUiState as MutableStateFlow).value = StatisticsUiState(statisticDetails)
            }
        }
    }

    private fun getStatisticDetails(books: List<Book>): StatisticDetails {
        val tbrBooks = books.count { it.rating == null }
        val allCompletedBooks = books.size - tbrBooks
        return StatisticDetails(allCompletedBooks, tbrBooks)
    }

    fun getStatisticsUiState(): StatisticsUiState {
        return statisticsUiState.value
    }
}
