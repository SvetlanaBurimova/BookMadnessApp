package com.example.book_madness.ui.navigation

import com.example.book_madness.R
import com.example.book_madness.ui.navigation.BookMadnessDestinationsArgs.BOOK_ID_ARG
import com.example.book_madness.ui.navigation.BookMadnessScreenRoutes.BOOK_DETAIL_SCREEN
import com.example.book_madness.ui.navigation.BookMadnessScreenRoutes.BOOK_EDIT_SCREEN

object BookMadnessScreenRoutes {
    const val HOME_SCREEN = "home"
    const val BOOK_DETAIL_SCREEN = "bookDetails"
    const val BOOK_ADD_SCREEN = "bookEntry"
    const val BOOK_EDIT_SCREEN = "itemEdit"
    const val STATISTICS_SCREEN = "statistics"
    const val COUNT_DOWN_TIMER_SCREEN = "timer"
}

object BookMadnessTitlesResId {
    val HOME_SCREEN = R.string.book_list
    val BOOK_DETAIL_SCREEN = R.string.book_detail_title
    val BOOK_ADD_SCREEN = R.string.book_entry_title
    val BOOK_EDIT_SCREEN = R.string.edit_book
    val STATISTICS_SCREEN = R.string.book_stats
    val COUNT_DOWN_TIMER_SCREEN = R.string.book_count_down_timer
}

object BookMadnessDestinationsArgs {
    const val BOOK_ID_ARG = "bookId"
}

object BookMadnessDestinations {
    const val BOOK_DETAIL_ROUTE = "$BOOK_DETAIL_SCREEN/{$BOOK_ID_ARG}"
    const val BOOK_EDIT_ROUTE = "$BOOK_EDIT_SCREEN/{$BOOK_ID_ARG}"
}
