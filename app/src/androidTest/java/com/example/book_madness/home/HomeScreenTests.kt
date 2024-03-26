package com.example.book_madness.home

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.core.app.ApplicationProvider
import com.example.book_madness.R
import com.example.book_madness.data.OfflineBooksRepository
import com.example.book_madness.data.source.BookDatabase
import com.example.book_madness.fake.FakeDataSource.bookList
import com.example.book_madness.fake.FakeDataSource.bookOne
import com.example.book_madness.fake.FakeDataSource.bookThree
import com.example.book_madness.fake.FakeDataSource.bookTwo
import com.example.book_madness.onNodeWithStringId
import com.example.book_madness.ui.home.HomeScreen
import com.example.book_madness.ui.home.HomeViewModel
import com.example.book_madness.ui.theme.AppTheme
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class HomeScreenTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val activity get() = composeTestRule.activity

    private val booksRepository =
        OfflineBooksRepository(
            BookDatabase.getDatabase(ApplicationProvider.getApplicationContext()).bookDao()
        )

    @Test
    fun displayBooks_whenRepositoryHasData() = runTest {
        addThreeBooks()
        setContent()

        val ratingIcon = composeTestRule.activity.getString(R.string.search_icon)
        val bookTwoStartDate = composeTestRule.activity.getString(R.string.start_screen_start_date_field, bookTwo.startDate!!)
        val bookTwoFinishDate = composeTestRule.activity.getString(R.string.start_screen_finish_date_field, bookTwo.finishDate)
        val bookThreeStartDate = composeTestRule.activity.getString(R.string.start_screen_start_date_field, bookThree.startDate!!)

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
        addThreeBooks()
        setContent()

        val searchButton = composeTestRule.activity.getString(R.string.search_icon)

        composeTestRule.onNodeWithContentDescription(searchButton).performClick()
        findTextField(R.string.search_field_label).performTextInput("wi")

        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookTwo.name).assertIsDisplayed()
    }

    @Test
    fun searchInvalidBookName_displayEmptyScreen() = runTest {
        addThreeBooks()
        setContent()

        val searchButton = composeTestRule.activity.getString(R.string.search_icon)

        composeTestRule.onNodeWithContentDescription(searchButton).performClick()
        findTextField(R.string.search_field_label).performTextInput("Wombat")

        composeTestRule.onNodeWithStringId(R.string.no_books_description).assertIsDisplayed()
    }

    @Test
    fun filterBooksByYear2023_displayEmptyScreen() = runTest {
        addThreeBooks()
        setContent()

        val filterButton = composeTestRule.activity.getString(R.string.filter_button)
        val filterByYear2023 = composeTestRule.activity.getString(R.string.books_by_year_2023_filter_button)

        composeTestRule.onNodeWithContentDescription(filterButton).performClick()
        composeTestRule.onNodeWithText(filterByYear2023).performClick()

        composeTestRule.onNodeWithStringId(R.string.no_books_description).assertIsDisplayed()
    }

    @Test
    fun filterBooksByYear2024_displayEmptyScreen() = runTest {
        addThreeBooks()
        setContent()

        val filterButton = composeTestRule.activity.getString(R.string.filter_button)
        val filterByYear2024 = composeTestRule.activity.getString(R.string.books_by_year_2024_filter_button)

        composeTestRule.onNodeWithContentDescription(filterButton).performClick()
        composeTestRule.onNodeWithText(filterByYear2024).performClick()

        composeTestRule.onNodeWithText(bookOne.name).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(bookTwo.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookThree.name).assertIsNotDisplayed()
    }

    @Test
    fun filterBooksByTBR_displayEmptyScreen() = runTest {
        addThreeBooks()
        setContent()

        val filterButton = composeTestRule.activity.getString(R.string.filter_button)
        val filterByTBR = composeTestRule.activity.getString(R.string.tbr_filter_button)

        composeTestRule.onNodeWithContentDescription(filterButton).performClick()
        composeTestRule.onNodeWithText(filterByTBR).performClick()

        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookTwo.name).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(bookThree.name).assertIsNotDisplayed()
    }

    @Test
    fun noBooks_SearchAndFilterButtonsAreVisible() {
        setContent()

        val filterButton = composeTestRule.activity.getString(R.string.filter_button)
        val searchButton = composeTestRule.activity.getString(R.string.search_icon)

        composeTestRule.onNodeWithContentDescription(filterButton).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(searchButton).assertIsDisplayed()
    }

    @Test
    fun noBooks_EmptyScreenTextIsVisible() {
        setContent()

        composeTestRule.onNodeWithStringId(R.string.no_books_description).assertIsDisplayed()
    }

    private fun findTextField(text: Int): SemanticsNodeInteraction {
        return composeTestRule.onNode(
            hasSetTextAction() and hasText(activity.getString(text))
        )
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

    private suspend fun addThreeBooks() {
        bookList.forEach {
            booksRepository.insertBook(it)
        }
    }
}
