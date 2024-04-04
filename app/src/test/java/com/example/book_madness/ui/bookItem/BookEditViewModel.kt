package com.example.book_madness.ui.bookItem

import androidx.lifecycle.SavedStateHandle
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.model.BookDetails
import com.example.book_madness.ui.navigation.BookMadnessDestinationsArgs
import com.example.book_madness.util.toBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class BookEditViewModelTest {
    private lateinit var viewModel: BookEditViewModel
    private lateinit var mockBooksRepository: BooksRepository
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val args = mapOf(BookMadnessDestinationsArgs.BOOK_ID_ARG to 1)
        savedStateHandle = SavedStateHandle(args)
        mockBooksRepository = mock(BooksRepository::class.java)
        viewModel = BookEditViewModel(savedStateHandle, mockBooksRepository)
    }

    @Test
    fun testInitializationWithValidBookId() = runBlocking {
        val bookId = 1
        val bookDetails = BookDetails(name = "Book 1", genre = "Fantasy")
        `when`(mockBooksRepository.getBookByIdStream(eq(bookId))).thenReturn(flowOf(bookDetails.toBook()))
        viewModel = BookEditViewModel(savedStateHandle, mockBooksRepository)
        val expectedUiState = BookUiState(bookDetails = bookDetails, isEntryValid = true)
        assertEquals(expectedUiState, viewModel.bookUiState)
    }

    @Test
    fun testSaveBookWithValidInput_bookIsSaved() = runBlocking {
        val bookDetails = BookDetails(name = "Book 1", genre = "Fantasy")
        viewModel.updateUiState(bookDetails)
        `when`(mockBooksRepository.updateBook(bookDetails.toBook())).thenReturn(Unit)
        viewModel.saveBook()
        verify(mockBooksRepository).updateBook(bookDetails.toBook())
    }

    @Test
    fun testSaveBookWithInvalidInput_bookIsNotSaved() = runBlocking {
        val bookDetails = BookDetails(name = "", genre = "Fantasy")
        viewModel.updateUiState(bookDetails)
        viewModel.saveBook()
        verify(mockBooksRepository, never()).updateBook(bookDetails.toBook())
    }
}
