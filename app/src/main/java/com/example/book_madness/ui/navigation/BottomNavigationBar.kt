package com.example.book_madness.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

var selectedTabIndex = 0

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val items = listOf(
        BottomNavigationItem(
            route = BookMadnessScreenRoutes.HOME_SCREEN,
            titleRes = BookMadnessTitlesResId.HOME_SCREEN,
            unselectedIcon = R.drawable.outline_library_books,
            selectedIcon = R.drawable.library_books
        ),
        BottomNavigationItem(
            route = BookMadnessScreenRoutes.STATISTICS_SCREEN,
            titleRes = BookMadnessTitlesResId.STATISTICS_SCREEN,
            unselectedIcon = R.drawable.outline_pie_chart,
            selectedIcon = R.drawable.pie_chart
        ),
        BottomNavigationItem(
            route = stringResource(id = R.string.book_count_down_timer),
            titleRes = BookMadnessTitlesResId.COUNT_DOWN_TIMER_SCREEN,
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
                label = {
                    Text(
                        text = stringResource(id = item.titleRes)
                    )
                },
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
