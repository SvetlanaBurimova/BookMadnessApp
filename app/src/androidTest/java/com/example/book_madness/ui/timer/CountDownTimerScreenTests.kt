package com.example.book_madness.ui.timer

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.book_madness.R
import com.example.book_madness.data.WorkManagerRepository
import com.example.book_madness.util.onNodeWithStringId
import com.example.book_madness.ui.theme.AppTheme
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountDownTimerScreenTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setContent() {
        composeTestRule.setContent {
            AppTheme {
                Surface {
                    CountDownTimerScreen(
                        viewModel = CountTimerViewModel(
                            WorkManagerRepository(ApplicationProvider.getApplicationContext())
                        ),
                        bottomNavigationBar = { },
                    )
                }
            }
        }
    }

    @Test
    fun displayTimerScreenElements() = runTest {
        composeTestRule.onNodeWithStringId(R.string.reading_time_title).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.enjoy_title).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.timer_description).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.start_button).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.reminder_button).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Timer progress indicator").assertIsDisplayed()
        composeTestRule.onNodeWithText("30:00").assertIsDisplayed()
    }

    @Test
    fun startTimer_timeIsChanged() = runTest {
        composeTestRule.onNodeWithText("30:00").assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.start_button).performClick()
        composeTestRule.onNodeWithText("30:00").assertDoesNotExist()
    }
}
