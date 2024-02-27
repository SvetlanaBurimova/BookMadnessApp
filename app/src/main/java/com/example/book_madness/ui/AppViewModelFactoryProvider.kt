package com.example.book_madness.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.book_madness.BookMadnessApplication
import com.example.book_madness.ui.bookItem.BookDetailsViewModel
import com.example.book_madness.ui.bookItem.BookEntryViewModel
import com.example.book_madness.ui.home.HomeViewModel

// Provides Factory to create instance of ViewModel for the entire app
object AppViewModelFactoryProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(bookMadnessApplication().container.booksRepository)
        }
        initializer {
            BookEntryViewModel(bookMadnessApplication().container.booksRepository)
        }
        initializer {
            BookDetailsViewModel(
                this.createSavedStateHandle(),
                bookMadnessApplication().container.booksRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [BookMadnessApplication].
 */
fun CreationExtras.bookMadnessApplication(): BookMadnessApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as BookMadnessApplication)
