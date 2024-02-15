package com.example.book_madness.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.book_madness.R

data class BottomNavigationItem(
    val title: String,
    val unselectedIcon: Painter,
    val selectedIcon: Painter
)

@Composable
fun BottomNavigationBar() {

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val items = listOf(
        BottomNavigationItem(
            title = stringResource(id = R.string.book_list),
            unselectedIcon = painterResource(id = R.drawable.outline_library_books_24),
            selectedIcon = painterResource(id = R.drawable.baseline_library_books_24)
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.book_stats),
            unselectedIcon = painterResource(id = R.drawable.outline_pie_chart_24),
            selectedIcon = painterResource(id = R.drawable.baseline_pie_chart_24)
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.book_settings),
            unselectedIcon = painterResource(id = R.drawable.outline_settings_24),
            selectedIcon = painterResource(id = R.drawable.baseline_settings_24)
        )
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
               selected = selectedTabIndex == index,
               onClick = {
                   selectedTabIndex = index
//                 navController.navigate(item.title)
                },
               label = { Text(text = item.title) },
               icon = {
                   Icon(
                       painter = if (index == selectedTabIndex) item.selectedIcon else item.unselectedIcon,
                       contentDescription = item.title
                   )
               }
            )
        }
    }
}
