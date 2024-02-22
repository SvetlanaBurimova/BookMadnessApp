package com.example.book_madness.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.navigation.NavHostController
import com.example.book_madness.R

data class BottomNavigationItem(
    val route: String,
    @StringRes val titleRes: Int,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int
)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val items = listOf(
        BottomNavigationItem(
            route = BookMadnessScreenRoutes.HOME_SCREEN,
            titleRes = R.string.book_list,
            unselectedIcon = R.drawable.outline_library_books,
            selectedIcon = R.drawable.library_books
        ),
        BottomNavigationItem(
            route = stringResource(id = R.string.book_stats),
            titleRes = R.string.book_stats,
            unselectedIcon = R.drawable.outline_pie_chart,
            selectedIcon = R.drawable.pie_chart
        ),
        BottomNavigationItem(
            route = stringResource(id = R.string.book_settings),
            titleRes = R.string.book_settings,
            unselectedIcon = R.drawable.outline_settings,
            selectedIcon = R.drawable.settings
        )
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
               selected = selectedTabIndex == index,
               onClick = {
                   selectedTabIndex = index
                   navController.navigate(item.route)
                },
               label = { Text(text = stringResource(id = item.titleRes)
               ) },
               icon = {
                   Icon(
                       painter =
                       if (index == selectedTabIndex) painterResource(id = item.selectedIcon)
                       else painterResource(id = item.unselectedIcon),
                       contentDescription = stringResource(id = item.titleRes)
                   )
               }
            )
        }
    }
}
