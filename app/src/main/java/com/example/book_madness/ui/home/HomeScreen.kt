package com.example.book_madness.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.book_madness.R
import com.example.book_madness.data.source.Book
import com.example.book_madness.ui.AppViewModelFactoryProvider
import com.example.book_madness.ui.navigation.BottomNavigationBar
import com.example.book_madness.ui.theme.AppTheme
import com.example.book_madness.util.BookMadnessFloatingActionButton
import com.example.book_madness.util.BookMadnessRatingIcon
import com.example.book_madness.util.BookMadnessTopAppBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(R.string.app_name),
            )
        },
        floatingActionButton = { BookMadnessFloatingActionButton() },
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        HomeBody(
            bookList = homeUiState.bookList,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    bookList: List<Book>,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (bookList.isEmpty()) {
            HomeEmptyScreen()
        } else {
            BookList(
                bookList = bookList,
                onItemClick = { },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.small))
            )
        }
    }
}

@Composable
fun HomeEmptyScreen(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.animation_for_empty_screen)
    )
    LottieAnimation(
        composition = composition
    )
    Text(
        text = stringResource(R.string.no_books_description),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun BookList(
    bookList: List<Book>,
    onItemClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = bookList, key = { it.id }) { item ->
            BookItem(
                book = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.small))
                    .clickable { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun BookItem(
    book: Book,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.extra_small))
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.medium))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = book.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = book.rating,
                    style = MaterialTheme.typography.titleLarge
                )

                if(book.rating != "0") {
                    BookMadnessRatingIcon(painterResource(id = R.drawable.round_star))
                } else {
                    BookMadnessRatingIcon(painterResource(id = R.drawable.round_star_border))
                }
            }

            if(book.startDate != null ) {
                ReadingDate(R.string.start_date, book.startDate)
            }
            if(book.finishDate != null) {
                ReadingDate(R.string.finish_date, book.finishDate)
            }
        }
    }
}

@Composable
private fun ReadingDate(id: Int, date: String) {
    Text(
        text = stringResource(id, date),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeBody(
            bookList = listOf(
                Book(
                    id = 1,
                    name = "Fourth Wing",
                    genre = "Fantasy",
                    rating = "4.5",
                    startDate = "02.11.2023",
                    finishDate = "23.01.2024"
                ),
                Book(
                    id = 2,
                    name = "Powerless",
                    genre = "Fantasy",
                    rating = "0",
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenEmptyPreview() {
    AppTheme {
        HomeBody(bookList = emptyList())
    }
}
