package com.example.book_madness.ui.stats

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.book_madness.R
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.OfflineBooksRepository
import com.example.book_madness.data.source.BookDatabase
import com.example.book_madness.util.fakeData.FakeDataSource.bookList
import com.example.book_madness.util.onNodeWithStringId
import com.example.book_madness.ui.theme.AppTheme
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatisticsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val activity get() = composeTestRule.activity

    private lateinit var booksRepository: BooksRepository

    @Test
    fun displayBookStats_whenRepositoryHasData() = runTest {
        setUpBooksRepository()
        addThreeBooks()
        setContent()

        composeTestRule.onNodeWithStringId(R.string.tbr_books_category_label).assertIsDisplayed()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.completed_books_category_label).assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.statistic_screen_book_image)).assertIsDisplayed()
    }

    @Test
    fun displayBookStats_whenRepositoryEmpty() = runTest {
        setContent()

        composeTestRule.onNodeWithStringId(R.string.tbr_books_category_label).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.completed_books_category_label).assertIsDisplayed()
        composeTestRule.onAllNodesWithText("0").assertCountEquals(2)
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.statistic_screen_book_image)).assertIsDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            AppTheme {
                Surface {
                    StatisticsScreen(
                        viewModel = StatisticsViewModel(booksRepository),
                        navigateToBookEntry = { },
                        bottomNavigationBar = { },
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
