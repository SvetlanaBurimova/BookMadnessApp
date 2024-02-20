package com.example.book_madness.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.book_madness.ui.bookItem.BookDetailsScreen
import com.example.book_madness.ui.home.HomeScreen

@Composable
fun BookMadnessNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = BookMadnessScreenRoutes.HOME_SCREEN,
        modifier = modifier
    ) {
        composable(route = BookMadnessScreenRoutes.HOME_SCREEN) {
            HomeScreen(
                navigateToItemEntry = { },
                navigateToItemDetails = {
                    navController.navigate("${BookMadnessScreenRoutes.BOOK_DETAIL_SCREEN}/${it}")
                },
                bottomNavigationBar = { BottomNavigationBar(navController) },
            )
        }
        composable(
            route = BookMadnessDestinations.BOOK_DETAIL_ROUTE,
            arguments = listOf(navArgument(BookMadnessDestinationsArgs.BOOK_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            BookDetailsScreen(
                bottomNavigationBar = { BottomNavigationBar(navController) },
            )
        }
    }
}
