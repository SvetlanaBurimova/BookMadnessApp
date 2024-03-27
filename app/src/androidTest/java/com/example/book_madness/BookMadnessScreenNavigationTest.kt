package com.example.book_madness

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.book_madness.data.BooksRepository
import com.example.book_madness.data.OfflineBooksRepository
import com.example.book_madness.data.source.BookDatabase
import com.example.book_madness.fake.FakeDataSource.bookOne
import com.example.book_madness.ui.navigation.BookMadnessDestinations
import com.example.book_madness.ui.navigation.BookMadnessScreenRoutes
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class BookMadnessScreenNavigationTest {

    //To access to an empty activity, the code uses ComponentActivity instead of MainActivity.
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val activity get() = composeTestRule.activity

    private lateinit var navController: TestNavHostController
    private lateinit var booksRepository: BooksRepository

    @Test
    fun bookMadnessNavHost_verifyStartDestination() {
        setupBookMadnessNavHost()

        navController.assertCurrentRouteName(BookMadnessScreenRoutes.HOME_SCREEN)
    }

    @Test
    fun bookMadnessNavHost_verifyBackNavigationNotShownOnStartScreen() {
        setupBookMadnessNavHost()
        val backButtonText = activity.getString(R.string.back_button)

        composeTestRule.onNodeWithContentDescription(backButtonText).assertDoesNotExist()
    }

    @Test
    fun bookMadnessNavHost_clickFloatingButton_navigateToBookEntryScreen() {
        setupBookMadnessNavHost()
        navigateToBookEntryScreen()

        navController.assertCurrentRouteName(BookMadnessScreenRoutes.BOOK_ADD_SCREEN)
    }

    @Test
    fun bookMadnessNavHost_clickBackOnBookEntryScreen_navigateToStartDestination() {
        setupBookMadnessNavHost()
        navigateToBookEntryScreen()
        performNavigateUp()

        navController.assertCurrentRouteName(BookMadnessScreenRoutes.HOME_SCREEN)
    }

    @Test
    fun bookMadnessNavHost_clickOnBook_navigateToBookDetailScreen() = runTest {
        setUpBooksRepository()
        addBookOne()
        setupBookMadnessNavHost()
        navigateToBookDetailScreen()

        navController.assertCurrentRouteName(BookMadnessDestinations.BOOK_DETAIL_ROUTE)
    }

    @Test
    fun bookMadnessNavHost_clickBackOnDetailsScreen_navigateToStartDestination() = runTest {
        setUpBooksRepository()
        addBookOne()
        setupBookMadnessNavHost()
        navigateToBookDetailScreen()
        performNavigateUp()

        navController.assertCurrentRouteName(BookMadnessScreenRoutes.HOME_SCREEN)
    }

    @Test
    fun bookMadnessNavHost_clickOnEditButton_navigateToBookEditScreen() = runTest {
        setUpBooksRepository()
        addBookOne()
        setupBookMadnessNavHost()
        navigateToBookEditScreen()

        navController.assertCurrentRouteName(BookMadnessDestinations.BOOK_EDIT_ROUTE)
    }

    @Test
    fun bookMadnessNavHost_clickBackOnEditScreen_navigateToBookDetailsScreen() = runTest {
        setUpBooksRepository()
        addBookOne()
        setupBookMadnessNavHost()
        navigateToBookEditScreen()
        performNavigateUp()

        navController.assertCurrentRouteName(BookMadnessDestinations.BOOK_DETAIL_ROUTE)
    }

    @Test
    fun bookMadnessNavHost_clickOnStatsBottomButton_verifyStatsScreen() {
        setupBookMadnessNavHost()
        composeTestRule.onNodeWithStringId(R.string.book_stats_title).performClick()

        navController.assertCurrentRouteName(BookMadnessScreenRoutes.STATISTICS_SCREEN)
    }

    @Test
    fun bookMadnessNavHost_clickOnTimerBottomButton_verifyTimerScreen() {
        setupBookMadnessNavHost()
        composeTestRule.onNodeWithStringId(R.string.timer_screen_title).performClick()

        navController.assertCurrentRouteName(BookMadnessScreenRoutes.COUNT_DOWN_TIMER_SCREEN)
    }

    private fun setUpBooksRepository() {
        booksRepository =
            OfflineBooksRepository(
                BookDatabase.getDatabase(ApplicationProvider.getApplicationContext()).bookDao()
            )
    }

    private fun setupBookMadnessNavHost() {
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

    private fun performNavigateUp() {
        val backButton = activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backButton).performClick()
    }

    private fun navigateToBookEntryScreen() {
        val floatingButton = activity.getString(R.string.floating_book_entry_button)
        composeTestRule.onNodeWithContentDescription(floatingButton).performClick()
    }

    private fun navigateToBookDetailScreen() {
        composeTestRule.onNodeWithText(bookOne.name).performClick()
    }

    private fun navigateToBookEditScreen() {
        navigateToBookDetailScreen()
        composeTestRule.onNodeWithStringId(R.string.edit_book_button).performClick()
    }

    private suspend fun addBookOne() {
        booksRepository.insertBook(bookOne)
    }
}
