package com.example.book_madness

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.book_madness.ui.navigation.BookMadnessNavHost

// Top level composable that represents screens for the application.
@Composable
fun BookMadnessApp(navController: NavHostController = rememberNavController()) {
    BookMadnessNavHost(navController = navController)
}
