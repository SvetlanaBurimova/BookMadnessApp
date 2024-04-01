package com.example.book_madness.ui.bookItem

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.book_madness.BookMadnessApp
import com.example.book_madness.R
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.OfflineBooksRepository
import com.example.book_madness.data.source.BookDatabase
import com.example.book_madness.ui.navigation.BookMadnessScreenRoutes
import com.example.book_madness.util.fakeData.FakeDataSource.bookTwo
import com.example.book_madness.util.onNodeWithStringId
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookDetailsScreenTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController
    private lateinit var booksRepository: BooksRepository

    private val activity get() = composeTestRule.activity

    @Test
    fun allElementsAreDisplayed() = runTest {
        setUpBooksRepository()
        booksRepository.insertBook(bookTwo)
        setContent()

        composeTestRule.onNodeWithText(bookTwo.name).performClick()

        composeTestRule.onNodeWithStringId(R.string.book_name_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_genre_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_rating_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_paper_format_box).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_details_start_date_button).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_details_finish_date_button).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_author_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_notes_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.edit_book_button).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.delete_button).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.share_icon_button)).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.share_icon_button)).performClick()
        composeTestRule.onNodeWithStringId(R.string.share_button).performClick()
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
}