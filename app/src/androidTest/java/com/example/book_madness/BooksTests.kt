package com.example.book_madness

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.OfflineBooksRepository
import com.example.book_madness.data.source.BookDatabase
import com.example.book_madness.util.fakeData.FakeDataSource.bookOne
import com.example.book_madness.util.fakeData.FakeDataSource.bookTwo
import com.example.book_madness.ui.navigation.BookMadnessScreenRoutes
import com.example.book_madness.util.onNodeWithStringId
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BooksTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController
    private val activity get() = composeTestRule.activity

    private lateinit var booksRepository: BooksRepository

    @Test
    fun editBook_displayUpdatedData() = runTest {
        setUpBooksRepository()
        booksRepository.insertBook(bookOne)
        setContent()

        val ratingIcon = activity.getString(R.string.rating_icon)
        val author = "Abra Cadabra"
        val notes = "Lorem ipsum"
        val bookTestName = "Test name"
        val emptyStar = "Empty star"
        val fullStar = "Full star"
        val halfStar = "Half star"

        // Validate that data shows correctly on Home screen
        composeTestRule.onNodeWithContentDescription(ratingIcon).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookOne.rating!!).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookOne.name).performClick()

        // Validate that data shows correctly on Detail screen
        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(emptyStar).assertDoesNotExist()
        composeTestRule.onAllNodesWithContentDescription(fullStar).assertCountEquals(4)
        composeTestRule.onNodeWithContentDescription(halfStar).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookOne.genre).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.no_button).assertIsDisplayed()
        composeTestRule.onNodeWithText(author).assertDoesNotExist()
        composeTestRule.onNodeWithText(notes).assertDoesNotExist()

        // Click on the edit button, edit, and save
        composeTestRule.onNodeWithStringId(R.string.edit_book_button).performClick()
        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookOne.name).performTextReplacement(bookTestName)
        composeTestRule.onNodeWithText(bookOne.genre).assertIsDisplayed()
        // Clear rating
        composeTestRule.onNodeWithContentDescription("Clear rating button").performClick()

        composeTestRule.onNodeWithContentDescription("Rating checkbox").performClick()
        composeTestRule.onNodeWithStringId(R.string.book_author_field).performTextReplacement(author)
        composeTestRule.onNodeWithStringId(R.string.book_notes_field).performTextReplacement(notes)
        composeTestRule.onNodeWithStringId(R.string.save_button).performClick()

        composeTestRule.onNodeWithText(bookTestName).assertIsDisplayed()
        composeTestRule.onAllNodesWithContentDescription(emptyStar).assertCountEquals(5)
        composeTestRule.onNodeWithContentDescription(fullStar).assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription(halfStar).assertDoesNotExist()
        composeTestRule.onNodeWithText(author).assertIsDisplayed()
        composeTestRule.onNodeWithText(notes).assertIsDisplayed()
        performNavigateUp()

        // Verify book is displayed on home screen
        composeTestRule.onNodeWithContentDescription(ratingIcon).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookOne.name).assertDoesNotExist()
        composeTestRule.onNodeWithText(bookOne.rating!!).assertDoesNotExist()
        composeTestRule.onNodeWithText(bookTestName).assertIsDisplayed()
        composeTestRule.onNodeWithText("0").assertIsDisplayed()
    }

    @Test
    fun addBook_deleteBook() {
        setContent()

        val floatingButton = composeTestRule.activity.getString(R.string.floating_book_entry_button)
        val bookTestName = "ABRA"
        val bookTestGenre = "cadabra"

        composeTestRule.onNodeWithContentDescription(floatingButton).performClick()

        composeTestRule.onNodeWithStringId(R.string.book_name_required_field).performTextInput(bookTestName)
        composeTestRule.onNodeWithStringId(R.string.book_genre_required_field).performTextInput(bookTestGenre)
        composeTestRule.onNodeWithStringId(R.string.save_button).performClick()
        composeTestRule.onNodeWithText(bookTestName).assertIsDisplayed()

        // Open book detail screen and click delete
        composeTestRule.onNodeWithText(bookTestName).performClick()
        composeTestRule.onNodeWithStringId(R.string.delete_button).performClick()
        composeTestRule.onNodeWithStringId(R.string.yes_button).performClick()

        // Verify it was deleted
        composeTestRule.onNodeWithText(bookTestName).assertDoesNotExist()
        composeTestRule.onNodeWithStringId(R.string.no_books_description).assertIsDisplayed()
    }

    @Test
    fun addTwoBooks_deleteOneBook() = runTest {
        setUpBooksRepository()
        booksRepository.apply {
            insertBook(bookOne)
            insertBook(bookTwo)
        }
        setContent()

        // Tap no on delete popup
        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookTwo.name).performClick()
        composeTestRule.onNodeWithStringId(R.string.delete_button).performClick()
        composeTestRule.onNodeWithStringId(R.string.no_button).performClick()

        performNavigateUp()

        composeTestRule.onNodeWithText(bookTwo.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookOne.name).performClick()
        composeTestRule.onNodeWithStringId(R.string.delete_button).performClick()
        composeTestRule.onNodeWithStringId(R.string.yes_button).performClick()

        // Verify it was deleted
        composeTestRule.onNodeWithText(bookTwo.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(bookOne.name).assertDoesNotExist()
    }

    private fun setContent() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            BookMadnessApp(
                startDestination = BookMadnessScreenRoutes.HOME_SCREEN,
                navController = navController
            )
        }
    }

    private fun setUpBooksRepository() {
        booksRepository =
            OfflineBooksRepository(
                BookDatabase.getDatabase(ApplicationProvider.getApplicationContext()).bookDao()
            )
    }

    private fun performNavigateUp() {
        val backButtonDescription = activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backButtonDescription).performClick()
    }
}
