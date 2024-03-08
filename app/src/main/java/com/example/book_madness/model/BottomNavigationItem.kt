package com.example.book_madness.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomNavigationItem(
    val route: String,
    @StringRes val titleRes: Int,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int
)

var selectedTabIndex: Int = 0
