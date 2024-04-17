package com.example.book_madness.ui.stats

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.book_madness.R
import com.example.book_madness.model.StatisticDetails
import com.example.book_madness.ui.AppViewModelFactoryProvider
import com.example.book_madness.ui.navigation.BookMadnessTitlesResId
import com.example.book_madness.ui.BookMadnessFloatingActionButton
import com.example.book_madness.ui.BookMadnessTopAppBar

@Composable
fun StatisticsScreen(
    navigateToBookEntry: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory)
) {
    val statisticsUiState by viewModel.statisticsUiState.collectAsState()

    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(BookMadnessTitlesResId.STATISTICS_SCREEN),
                canNavigateBack = false
            )
        },
        floatingActionButton = { BookMadnessFloatingActionButton(navigateToBookEntry = navigateToBookEntry) },
        bottomBar = { bottomNavigationBar() }
    )
    { innerPadding ->
        StatisticBody(
            bookListWithRating = statisticsUiState.statisticDetails,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun StatisticBody(
    bookListWithRating: StatisticDetails,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        BookImage()
        BooksAmountByCategory(
            categoryTitleResId = R.string.tbr_books_category_label,
            booksAmount = bookListWithRating.tbrBooks ?: 0
        )
        BooksAmountByCategory(
            categoryTitleResId = R.string.completed_books_category_label,
            booksAmount = bookListWithRating.allCompletedBooks ?: 0
        )
    }
}

@Composable
fun BookImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.statistic_screen_book_image),
        contentDescription = stringResource(id = R.string.statistic_screen_book_image),
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.small))
            .aspectRatio(14f / 10f)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Composable
fun BooksAmountByCategory(
    @StringRes categoryTitleResId: Int,
    booksAmount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(categoryTitleResId),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.large))
        )
        Text(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.large))
                .drawBehind {
                    drawCircle(
                        color = Color.LightGray,
                        radius = this.size.maxDimension
                    )
                },
            text = booksAmount.toString(),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
