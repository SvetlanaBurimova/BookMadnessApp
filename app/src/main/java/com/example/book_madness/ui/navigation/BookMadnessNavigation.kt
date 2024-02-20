package com.example.book_madness.ui.navigation

import com.example.book_madness.R
import com.example.book_madness.ui.navigation.BookMadnessDestinationsArgs.BOOK_ID_ARG
import com.example.book_madness.ui.navigation.BookMadnessScreenRoutes.BOOK_DETAIL_SCREEN

object BookMadnessScreenRoutes {
    const val HOME_SCREEN = "home"
    const val BOOK_DETAIL_SCREEN = "bookDetails"
}

object BookMadnessTitlesResId {
    val HOME_SCREEN = R.string.book_list
    val BOOK_DETAIL_SCREEN = R.string.book_detail_title
}

object BookMadnessDestinationsArgs {
    const val BOOK_ID_ARG = "bookId"
}

object BookMadnessDestinations {
    const val BOOK_DETAIL_ROUTE = "$BOOK_DETAIL_SCREEN/{$BOOK_ID_ARG}"
}
