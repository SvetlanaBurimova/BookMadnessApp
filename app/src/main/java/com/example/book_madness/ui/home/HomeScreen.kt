package com.example.book_madness.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.book_madness.BookMadnessEmptyScreen
import com.example.book_madness.BookMadnessFloatingActionButton
import com.example.book_madness.BookMadnessTopAppBar
import com.example.book_madness.R
import com.example.book_madness.data.Book
import com.example.compose.AppTheme

@Composable
fun HomeScreen(
    bookList: List<Book>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(R.string.app_name),
            )
        },
        floatingActionButton = { BookMadnessFloatingActionButton() }
    ) { innerPadding ->
        HomeBody(
            bookList = bookList,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(bookList: List<Book>,modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (bookList.isEmpty()) {
            BookMadnessEmptyScreen()
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
            Row(modifier = Modifier.fillMaxWidth()) {
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
                    RatingIcon(painterResource(id = R.drawable.round_star_24))
                } else {
                    RatingIcon(painterResource(id = R.drawable.round_star_border_24))
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
private fun RatingIcon(icon: Painter) {
    Icon(
        painter = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(dimensionResource(id = R.dimen.extra_large))
    )
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
        HomeScreen(bookList = listOf(
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
        ))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenEmptyPreview() {
    AppTheme {
        HomeScreen(bookList = emptyList())
    }
}
