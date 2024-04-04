import androidx.lifecycle.SavedStateHandle
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.model.BookDetails
import com.example.book_madness.ui.bookItem.BookDetailsViewModel
import com.example.book_madness.ui.navigation.BookMadnessDestinationsArgs
import com.example.book_madness.util.toBook
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class BookDetailsViewModelTest {
    @Mock
    private lateinit var mockBooksRepository: BooksRepository

    private lateinit var viewModel: BookDetailsViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savedStateHandle = SavedStateHandle(mapOf(BookMadnessDestinationsArgs.BOOK_ID_ARG to 1))
    }

    @Test
    fun testDeleteBook_bookIsDeleted() = runBlocking {
        val bookDetails = BookDetails(id = 1, name = "Book 1", genre = "Fantasy")

        `when`(mockBooksRepository.getBookByIdStream(1)).thenReturn(flowOf(bookDetails.toBook()))
        `when`(mockBooksRepository.deleteBook(bookDetails.toBook())).thenReturn(Unit)

        viewModel = BookDetailsViewModel(savedStateHandle, mockBooksRepository)
        val uiState = viewModel.uiState

        viewModel.deleteBook()

        // Ensure that the correct book is passed to deleteBook
        verify(mockBooksRepository).deleteBook(uiState.value.bookDetails.toBook())
    }
}
