package com.example.book_madness.ui.bookItem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.book_madness.R
import com.example.book_madness.data.BookDetails
import com.example.book_madness.ui.AppViewModelFactoryProvider
import com.example.book_madness.ui.navigation.BookMadnessTitlesResId
import com.example.book_madness.ui.theme.AppTheme
import com.example.book_madness.util.BookMadnessTopAppBar

@Composable
fun BookEntryScreen(
    viewModel: BookEntryViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(BookMadnessTitlesResId.BOOK_ADD_SCREEN)
            )
        }
    ) { innerPadding ->
        BookEntryBody(
            bookUiState = viewModel.bookUiState,
            onBookValueChange = viewModel::updateUiState,
            onSaveClick = { },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun BookEntryBody(
    bookUiState: BookUiState,
    onBookValueChange: (BookDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.large))
    ) {
        BookInputForm(
            bookDetails = bookUiState.bookDetails,
            onValueChange = onBookValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = bookUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@Composable
fun BookInputForm(
    bookDetails: BookDetails,
    modifier: Modifier = Modifier,
    onValueChange: (BookDetails) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium))
    ) {
        OutlinedTextField(
            value = bookDetails.name,
            onValueChange = { onValueChange(bookDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.book_name_required)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.genre,
            onValueChange = { onValueChange(bookDetails.copy(genre = it)) },
            label = { Text(stringResource(R.string.book_genre_required)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.rating,
            onValueChange = { onValueChange(bookDetails.copy(rating = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.book_rating)) },
            modifier = modifier
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.book_paper_format))
            Checkbox(
                checked = bookDetails.paper!!,
                onCheckedChange = { onValueChange(bookDetails.copy(paper = it)) }
            )
        }
        OutlinedTextField(
            value = bookDetails.startDate!!,
            onValueChange = { onValueChange(bookDetails.copy(startDate = it)) },
            label = { Text(stringResource(R.string.book_details_start_date)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.finishDate!!,
            onValueChange = { onValueChange(bookDetails.copy(finishDate = it)) },
            label = { Text(stringResource(R.string.book_details_finish_date)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.author!!,
            onValueChange = { onValueChange(bookDetails.copy(author = it)) },
            label = { Text(stringResource(R.string.book_author)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.notes!!,
            onValueChange = { onValueChange(bookDetails.copy(notes = it)) },
            label = { Text(stringResource(R.string.book_notes)) },
            modifier = modifier
        )
        Text(
            text = stringResource(R.string.required_fields),
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.medium))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookEntryScreenPreview() {
    AppTheme {
        BookEntryBody(
            bookUiState = BookUiState(
               BookDetails(
                   id = 1,
                   name = "Fourth Wing",
                   genre = "Fantasy",
                   paper = true,
                   rating = "4.5",
                   startDate = "02.11.2023",
                   finishDate = "23.01.2024",
                   notes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                           "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                           "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
               )
            ),
            onBookValueChange = { },
            onSaveClick = { }
        )
    }
}
