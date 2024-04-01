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
import androidx.compose.ui.test.performTextReplacement
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.OfflineBooksRepository
import com.example.book_madness.data.source.BookDatabase
import com.example.book_madness.ui.navigation.BookMadnessScreenRoutes
import com.example.book_madness.util.fakeData.FakeDataSource.bookOne
import com.example.book_madness.util.fakeData.FakeDataSource.bookTwo
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
    fun editBook_displayUpdatedAuthorAndNotesOnDetailScreen() = runTest {
        setUpBooksRepository()
        booksRepository.insertBook(bookOne)
        setContent()

        val author = "Abra Cadabra"
        val notes = "Lorem ipsum"

        composeTestRule.onNodeWithText(bookOne.name).performClick()

        // Validate that data shows correctly on Detail screen
        composeTestRule.onNodeWithText(author).assertDoesNotExist()
        composeTestRule.onNodeWithText(notes).assertDoesNotExist()

        // Click on the edit button, edit, and save
        composeTestRule.onNodeWithStringId(R.string.book_author_field).performTextReplacement(author)
        composeTestRule.onNodeWithStringId(R.string.book_notes_field).performTextReplacement(notes)
        composeTestRule.onNodeWithStringId(R.string.save_button).performClick()

        composeTestRule.onNodeWithText(author).assertIsDisplayed()
        composeTestRule.onNodeWithText(notes).assertIsDisplayed()
    }

    @Test
    fun editBook_displayUpdatedNameOnDetailScreen() = runTest {
        setUpBooksRepository()
        booksRepository.insertBook(bookOne)
        setContent()

        val bookTestName = "Test name"

        composeTestRule.onNodeWithText(bookOne.name).performClick()

        // Validate that data shows correctly on Detail screen
        composeTestRule.onNodeWithText(bookOne.name).assertIsDisplayed()

        // Click on the edit button, edit, and save
        composeTestRule.onNodeWithStringId(R.string.edit_book_button).performClick()
        composeTestRule.onNodeWithText(bookOne.name).performTextReplacement(bookTestName)
        composeTestRule.onNodeWithStringId(R.string.save_button).performClick()

        composeTestRule.onNodeWithText(bookOne.name).assertDoesNotExist()
        composeTestRule.onNodeWithText(bookTestName).assertIsDisplayed()
    }


    @Test
    fun editBook_displayUpdatedRatingOnDetailScreen() = runTest {
        setUpBooksRepository()
        booksRepository.insertBook(bookOne)
        setContent()

        val emptyStar = "Empty star"
        val fullStar = "Full star"
        val halfStar = "Half star"

        composeTestRule.onNodeWithText(bookOne.name).performClick()

        // Validate that data shows correctly on Detail screen
        composeTestRule.onNodeWithContentDescription(emptyStar).assertDoesNotExist()
        composeTestRule.onAllNodesWithContentDescription(fullStar).assertCountEquals(4)
        composeTestRule.onNodeWithContentDescription(halfStar).assertIsDisplayed()

        // Click on the edit button, edit, and save
        composeTestRule.onNodeWithStringId(R.string.edit_book_button).performClick()
        composeTestRule.onNodeWithContentDescription("Clear rating button").performClick()
        composeTestRule.onNodeWithStringId(R.string.save_button).performClick()

        composeTestRule.onAllNodesWithContentDescription(emptyStar).assertCountEquals(5)
        composeTestRule.onNodeWithContentDescription(fullStar).assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription(halfStar).assertDoesNotExist()
    }

    @Test
    fun editBook_displayUpdatedNameAndRatingOnHomeScreen() = runTest {
        setUpBooksRepository()
        booksRepository.insertBook(bookOne)
        setContent()

        val bookTestName = "Test name"

        // Validate that data shows correctly on Home screen
        composeTestRule.onNodeWithText(bookOne.name).performClick()

        // Click on the edit button, edit, and save
        composeTestRule.onNodeWithStringId(R.string.edit_book_button).performClick()
        composeTestRule.onNodeWithText(bookOne.name).performTextReplacement(bookTestName)
        composeTestRule.onNodeWithContentDescription("Clear rating button").performClick()
        composeTestRule.onNodeWithStringId(R.string.save_button).performClick()

        performNavigateUp()

        // Verify book is displayed on home screen
        composeTestRule.onNodeWithText(bookTestName).assertIsDisplayed()
        composeTestRule.onNodeWithText("0").assertIsDisplayed()
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
