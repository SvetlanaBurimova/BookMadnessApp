package com.example.book_madness.ui.bookItem

import com.example.book_madness.data.BooksRepository
import com.example.book_madness.model.BookDetails
import com.example.book_madness.util.toBook
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class BookEntryViewModelTest {
    @Mock
    private lateinit var mockBooksRepository: BooksRepository
    private lateinit var viewModel: BookEntryViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = BookEntryViewModel(mockBooksRepository)
    }

    @Test
    fun testUpdateUiState() {
        val bookDetails = BookDetails(name = "Book 1", genre = "Fantasy")
        viewModel.updateUiState(bookDetails)

        val expectedState = BookDetails(name = "Book 1", genre = "Fantasy")
        val expectedUiState = BookUiState(bookDetails = expectedState, isEntryValid = true)

        assertEquals(expectedUiState, viewModel.bookUiState)
    }

    @Test
    fun testSaveBookWithValidInput_bookIsSaved() = runBlocking {
        val bookDetails = BookDetails(name = "Book 1", genre = "Fantasy")
        viewModel.updateUiState(bookDetails)
        viewModel.saveBook()

        verify(mockBooksRepository).insertBook(bookDetails.toBook())
    }

    @Test
    fun testSaveBookWithInvalidInput_bookIsNotSaved() = runBlocking {
        val bookDetails = BookDetails(name = "", genre = "Fantasy")
        viewModel.updateUiState(bookDetails)
        viewModel.saveBook()

        // Verify that insertBook is not called when input is invalid
        verify(mockBooksRepository, never()).insertBook(bookDetails.toBook())
    }
}
