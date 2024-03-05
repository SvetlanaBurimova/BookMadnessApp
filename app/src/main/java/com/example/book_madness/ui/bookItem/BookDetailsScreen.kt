package com.example.book_madness.ui.bookItem

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.book_madness.R
import com.example.book_madness.data.source.Book
import com.example.book_madness.model.toBook
import com.example.book_madness.ui.AppViewModelFactoryProvider
import com.example.book_madness.ui.navigation.BookMadnessTitlesResId
import com.example.book_madness.ui.theme.AppTheme
import com.example.book_madness.util.BookMadnessRatingIcon
import com.example.book_madness.util.BookMadnessTopAppBar
import kotlinx.coroutines.launch

@Composable
fun BookDetailsScreen(
    modifier: Modifier = Modifier,
    navigateToEditBook: (Int) -> Unit,
    navigateBack: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit,
    viewModel: BookDetailsViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(BookMadnessTitlesResId.BOOK_DETAIL_SCREEN),
                navigateUp = navigateBack
            )
        },
        bottomBar = { bottomNavigationBar() }
    ) { innerPadding ->
        BookDetailsBody(
            bookDetailsUiState = uiState.value,
            onEdit = { navigateToEditBook(uiState.value.bookDetails.id) },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteBook()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun BookDetailsBody(
    bookDetailsUiState: BookDetailsUiState,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = dimensionResource(id = R.dimen.medium),
                end = dimensionResource(id = R.dimen.medium)
            )
    ) {
        BookDetails(
            book = bookDetailsUiState.bookDetails.toBook(),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onEdit,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Text(stringResource(R.string.edit_book))
        }
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.medium))
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.medium))
            )
        }
    }
}

@Composable
fun BookDetails(
    book: Book,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(
            top = dimensionResource(id = R.dimen.medium)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium))
        ) {
            BookDetailsRow(
                labelResID = R.string.book_name,
                bookDetail = book.name
            )
            Row {
                Text(text = stringResource(R.string.book_rating))
                Spacer(modifier = Modifier.weight(1f))

                BookDetailsRatingIcons(book.rating ?: "0")
            }

            BookDetailsRow(
                labelResID = R.string.book_genre,
                bookDetail = book.genre
            )

            BookDetailsRow(
                labelResID = R.string.book_paper_format,
                bookDetail = if (book.paper) {
                    stringResource(id = R.string.yes)
                } else stringResource(id = R.string.no)
            )

            BookDetailsRow(
                labelResID = R.string.book_details_start_date,
                bookDetail = book.startDate ?: ""
            )

            BookDetailsRow(
                labelResID = R.string.book_details_finish_date,
                bookDetail = book.finishDate ?: ""
            )

            BookDetailsRow(
                labelResID = R.string.book_author,
                bookDetail = book.author ?: ""
            )

            BookDetailsRow(
                labelResID = R.string.book_notes,
                bookDetail = book.notes ?: ""
            )
        }
    }
}

@Composable
fun BookDetailsRatingIcons(rating: String) {
    var bookRating = rating.toDouble()

    for (star in 1..5) {
        if (bookRating >= 1.0) {
            BookMadnessRatingIcon(painterResource(id = R.drawable.round_star))
        } else if (bookRating >= 0.5) {
            BookMadnessRatingIcon(painterResource(id = R.drawable.round_star_half))
        } else {
            BookMadnessRatingIcon(painterResource(id = R.drawable.round_star_border))
        }
        bookRating--
    }
}

@Composable
private fun BookDetailsRow(
    @StringRes labelResID: Int,
    bookDetail: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = stringResource(labelResID),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = bookDetail,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.medium))
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    modifier: Modifier = Modifier,
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BookDetailsScreenPreview() {
    AppTheme {
        BookDetails(
            Book(
                id = 2,
                name = "Powerless",
                genre = "Fantasy",
                rating = "3.5",
                paper = true,
                startDate = "12.01.2024",
                author = "Someone",
                notes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            )
        )
    }
}
