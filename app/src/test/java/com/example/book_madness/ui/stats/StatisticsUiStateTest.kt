import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.source.Book
import com.example.book_madness.model.StatisticDetails
import com.example.book_madness.ui.stats.StatisticsViewModel
import com.example.book_madness.util.fakeData.FakeDataSource.bookList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class StatisticsViewModelTest {

    @Mock
    private lateinit var booksRepository: BooksRepository
    private lateinit var viewModel: StatisticsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        Dispatchers.setMain(UnconfinedTestDispatcher())

        val flow = MutableStateFlow<List<Book>>(emptyList())
        `when`(booksRepository.getAllBooksStream()).thenReturn(flow)

        viewModel = StatisticsViewModel(booksRepository)
    }

    @Test
    fun correctStatisticDetailsWhenBooksAvailable() = runBlocking {
        val books = bookList
        val expectedStatisticDetails = StatisticDetails(allCompletedBooks = 2, tbrBooks = 1)
        (booksRepository.getAllBooksStream() as MutableStateFlow).value = books
        val statisticDetails = viewModel.statisticsUiState.first().statisticDetails

        assertEquals(expectedStatisticDetails, statisticDetails)
    }

    @Test
    fun correctStatisticDetailsWhenNoBooksAvailable() = runBlocking {
        val expectedStatisticDetails = StatisticDetails(allCompletedBooks = 0, tbrBooks = 0)
        val statisticDetails = viewModel.statisticsUiState.first().statisticDetails

        assertEquals(expectedStatisticDetails, statisticDetails)
    }
}
