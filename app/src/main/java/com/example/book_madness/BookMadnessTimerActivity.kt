package com.example.book_madness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.book_madness.model.selectedTabIndex
import com.example.book_madness.ui.navigation.BookMadnessScreenRoutes
import com.example.book_madness.ui.theme.AppTheme

class BookMadnessTimerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BookMadnessApp(BookMadnessScreenRoutes.COUNT_DOWN_TIMER_SCREEN)
                    selectedTabIndex = 2
                }
            }
        }
    }
}
