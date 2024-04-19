package com.example.book_madness.ui.home

import com.example.book_madness.data.BooksRepository
import com.example.book_madness.model.util.FilterType
import com.example.book_madness.util.fakeData.FakeDataSource.bookOne
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @Mock
    private lateinit var booksRepository: BooksRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = HomeViewModel(booksRepository)
    }

    @Test
    fun filterBooks_updatesFilterType() {
        viewModel.filterBooks(FilterType.NAME)
        assertEquals(FilterType.NAME, viewModel.filterType.value)
    }

    @Test
    fun onSearchQueryChange_updatesSearchQuery() {
        viewModel.onSearchQueryChange("New Query")
        assertEquals("New Query", viewModel.searchQuery)
    }

    @Test
    fun deleteBook_callsRepository() = runBlocking {
        viewModel.deleteBook(bookOne)

        verify(booksRepository).deleteBook(bookOne)
    }
}
