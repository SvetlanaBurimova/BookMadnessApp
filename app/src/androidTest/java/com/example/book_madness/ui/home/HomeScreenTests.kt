package com.example.book_madness.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.book_madness.R
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.OfflineBooksRepository
import com.example.book_madness.data.source.BookDatabase
import com.example.book_madness.util.fakeData.FakeDataSource.bookList
import com.example.book_madness.util.fakeData.FakeDataSource.bookOne
import com.example.book_madness.util.fakeData.FakeDataSource.bookThree
import com.example.book_madness.util.fakeData.FakeDataSource.bookTwo
import com.example.book_madness.util.onNodeWithStringId
import com.example.book_madness.ui.theme.AppTheme
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val activity get() = composeTestRule.activity

    private lateinit var booksRepository: BooksRepository

    @Test
    fun displayBooks_whenRepositoryHasData() = runTest {
        setUpBooksRepository()
        addThreeBooks()
        setContent()

        val ratingIcon = activity.getString(R.string.search_icon)
        val bookTwoStartDate = activity.getString(R.string.start_screen_start_date_field, bookTwo.startDate!!)
        val bookTwoFinishDate = activity.getString(R.string.start_screen_finish_date_field, bookTwo.finishDate)
        val bookThreeStartDate = activity.getString(R.string.start_screen_start_date_field, bookThree.startDate!!)

        composeTestRule.onNodeWithContentDescription(ratingIcon).assertIsDisplayed()

        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookOne.rating!!).assertIsDisplayed()

        composeTestRule.onNodeWithText(bookTwo.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookTwo.rating!!).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookTwoStartDate).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookTwoFinishDate).assertIsDisplayed()

        composeTestRule.onNodeWithText(bookThree.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookThreeStartDate).assertIsDisplayed()
    }

    @Test
    fun swipeToDelete_deletedBookIsNotDisplayed() = runTest {
        setUpBooksRepository()
        addThreeBooks()
        setContent()

        composeTestRule.onNodeWithText(bookThree.name).performTouchInput {
            swipeLeft()
        }

        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookTwo.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookThree.name).assertDoesNotExist()
    }

    @Test
    fun searchValidBookName_displayBooks() = runTest {
        setUpBooksRepository()
        addThreeBooks()
        setContent()

        val searchButton = activity.getString(R.string.search_icon)

        composeTestRule.onNodeWithContentDescription(searchButton).performClick()
        composeTestRule.onNodeWithStringId(R.string.search_field_label).performTextInput("wi")

        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookTwo.name).assertIsDisplayed()
    }

    @Test
    fun searchInvalidBookName_displayEmptyScreen() = runTest {
        setUpBooksRepository()
        addThreeBooks()
        setContent()

        val searchButton = activity.getString(R.string.search_icon)

        composeTestRule.onNodeWithContentDescription(searchButton).performClick()
        composeTestRule.onNodeWithStringId(R.string.search_field_label).performTextInput("Wombat")

        composeTestRule.onNodeWithStringId(R.string.no_books_description).assertIsDisplayed()
    }

    @Test
    fun filterBooksByYear2023_displayEmptyScreen() = runTest {
        setUpBooksRepository()
        addThreeBooks()
        setContent()

        val filterButton = activity.getString(R.string.filter_button)

        composeTestRule.onNodeWithContentDescription(filterButton).performClick()
        composeTestRule.onNodeWithStringId(R.string.books_by_year_2023_filter_button).performClick()

        composeTestRule.onNodeWithStringId(R.string.no_books_description).assertIsDisplayed()
    }

    @Test
    fun filterBooksByYear2024_displayEmptyScreen() = runTest {
        setUpBooksRepository()
        addThreeBooks()
        setContent()

        val filterButton = activity.getString(R.string.filter_button)

        composeTestRule.onNodeWithContentDescription(filterButton).performClick()
        composeTestRule.onNodeWithStringId(R.string.books_by_year_2024_filter_button).performClick()

        composeTestRule.onNodeWithText(bookOne.name).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(bookTwo.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookThree.name).assertIsNotDisplayed()
    }

    @Test
    fun filterBooksByTBR_displayEmptyScreen() = runTest {
        setUpBooksRepository()
        addThreeBooks()
        setContent()

        val filterButton = activity.getString(R.string.filter_button)

        composeTestRule.onNodeWithContentDescription(filterButton).performClick()
        composeTestRule.onNodeWithStringId(R.string.tbr_filter_button).performClick()

        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookTwo.name).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(bookThree.name).assertIsNotDisplayed()
    }

    @Test
    fun noBooks_SearchAndFilterButtonsAreVisible() {
        setUpBooksRepository()
        setContent()

        val filterButton = activity.getString(R.string.filter_button)
        val searchButton = activity.getString(R.string.search_icon)

        composeTestRule.onNodeWithContentDescription(filterButton).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(searchButton).assertIsDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            AppTheme {
                Surface {
                    HomeScreen(
                        viewModel = HomeViewModel(booksRepository),
                        navigateToBookEntry = { },
                        bottomNavigationBar = { },
                        navigateToBookDetails = { }
                    )
                }
            }
        }
    }

    private fun setUpBooksRepository() {
        booksRepository =
            OfflineBooksRepository(
                BookDatabase.getDatabase(ApplicationProvider.getApplicationContext()).bookDao()
            )
    }

    private suspend fun addThreeBooks() {
        bookList.forEach {
            booksRepository.insertBook(it)
        }
    }
}
