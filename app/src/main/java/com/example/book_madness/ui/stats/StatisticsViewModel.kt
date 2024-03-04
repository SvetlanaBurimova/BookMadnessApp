package com.example.book_madness.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.source.Book
import com.example.book_madness.model.StatisticDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class StatisticsUiState(
    val statisticDetails: StatisticDetails = StatisticDetails()
)

class StatisticsViewModel(
    booksRepository: BooksRepository
) : ViewModel() {

    val statisticsUiState: StateFlow<StatisticsUiState> =
        booksRepository.getAllBooksStream()
            .map {
                StatisticsUiState(
                    statisticDetails = getBooksWithRating(it)
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = StatisticsUiState()
            )

     private fun getBooksWithRating(tasks: List<Book>): StatisticDetails {
         return if (tasks.isEmpty()) {
             StatisticDetails()
         } else {
             StatisticDetails(
                 tbrBooks =  tasks.count { it.rating == null },
                 allCompletedBooks = tasks.count { it.rating != null }
             )
         }
     }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}