package com.example.book_madness.ui.bookItem

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.book_madness.R
import com.example.book_madness.data.OfflineBooksRepository
import com.example.book_madness.data.source.BookDatabase
import com.example.book_madness.util.onNodeWithStringId
import com.example.book_madness.ui.theme.AppTheme
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookEntryEditScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            AppTheme {
                Surface {
                    BookEntryScreen(
                        viewModel =
                            BookEntryViewModel(
                                OfflineBooksRepository(
                                    BookDatabase.getDatabase(ApplicationProvider.getApplicationContext())
                                        .bookDao())),
                        navigateBack = { }
                    )
                }
            }
        }
    }

    @Test
    fun emptyRequiredFields_saveButtonIsNotEnabled() {
        val bookTestName = "ABRA"
        val bookTestGenre = "cadabra"

        composeTestRule.onNodeWithStringId(R.string.save_button).assertIsNotEnabled()

        composeTestRule.onNodeWithStringId(R.string.book_name_required_field).performTextInput(bookTestName)
        composeTestRule.onNodeWithStringId(R.string.save_button).assertIsNotEnabled()

        composeTestRule.onNodeWithStringId(R.string.book_genre_required_field).performTextInput(bookTestGenre)
        composeTestRule.onNodeWithStringId(R.string.book_name_required_field).performTextClearance()
        composeTestRule.onNodeWithStringId(R.string.save_button).assertIsNotEnabled()
    }

    @Test
    fun enterDataIntoRequiredFields_saveButtonIsEnabled() = runTest {
        val bookTestName = "ABRA"
        val bookTestGenre = "cadabra"

        composeTestRule.onNodeWithStringId(R.string.book_genre_required_field).performTextInput(bookTestGenre)
        composeTestRule.onNodeWithStringId(R.string.book_name_required_field).performTextInput(bookTestName)


        composeTestRule.onNodeWithStringId(R.string.save_button).assertIsEnabled()
    }

    @Test
    fun allFieldsAreDisplayed() = runTest {
        composeTestRule.onNodeWithStringId(R.string.book_name_required_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_genre_required_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_rating_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_paper_format_box).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_details_start_date_button).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_details_finish_date_button).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_author_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.book_notes_field).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.required_fields_label).assertIsDisplayed()
    }
}
