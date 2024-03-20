package com.example.book_madness.util

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.example.book_madness.R
import com.example.book_madness.ui.bookItem.BookDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMadnessTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    showFilterIcon: Boolean = false,
    showShareButton: Boolean = false,
    bookDetailsUiState: BookDetailsUiState = BookDetailsUiState(),
    onFilterAllBooks: () -> Unit = {},
    onFilterAllBooksByName: () -> Unit = {},
    onFilterAllBooksByRating: () -> Unit = {},
    onFilterAllBooksByTBR: () -> Unit = {},
    onFilterAllBooksByYear2023: () -> Unit = {},
    onFilterAllBooksByYear2024: () -> Unit = {},
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (showFilterIcon)
                FilterBooksMenu(
                    onFilterAllBooks,
                    onFilterAllBooksByName,
                    onFilterAllBooksByRating,
                    onFilterAllBooksByTBR,
                    onFilterAllBooksByYear2023,
                    onFilterAllBooksByYear2024
                )
            if (showShareButton) {
                ShareButton(
                    bookDetailsUiState,
                    LocalContext.current
                )
            }
        },
        modifier = modifier
            .background(MaterialTheme.colorScheme.outline)
            .padding(bottom = dimensionResource(id = R.dimen.extra_small))
    )
}

@Composable
fun ShareButton(
    bookDetailsUiState: BookDetailsUiState,
    context: Context,
) {
    val shareBookName =
        "Book details: \nName: ${bookDetailsUiState.bookDetails.toBook().name}"

    val shareBookRating =
        if (bookDetailsUiState.bookDetails.toBook().rating != null) {
            "\nMy rating for it: ${bookDetailsUiState.bookDetails.toBook().rating}"
        } else {
            "\nNo rating yet"
        }

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(
            Intent.EXTRA_TEXT,
            shareBookName + shareBookRating
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)

    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null
            )
        }
    ) { closeMenu ->
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.share)) },
            onClick = {
                ContextCompat.startActivity(context, shareIntent, null)
                closeMenu()
            }
        )
    }
}

@Composable
private fun FilterBooksMenu(
    onFilterAllBooks: () -> Unit,
    onFilterAllBooksByName: () -> Unit,
    onFilterAllBooksByRating: () -> Unit,
    onFilterAllBooksByTBR: () -> Unit,
    onFilterAllBooksByYear2023: () -> Unit,
    onFilterAllBooksByYear2024: () -> Unit,
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                painter = painterResource(id = R.drawable.filter_list),
                contentDescription = null
            )
        }
    ) { closeMenu ->
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.filter_all_books)) },
            onClick = { onFilterAllBooks(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.filter_books_by_name)) },
            onClick = { onFilterAllBooksByName(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.filter_books_by_rating)) },
            onClick = { onFilterAllBooksByRating(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.tbr)) },
            onClick = { onFilterAllBooksByTBR(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.filter_books_by_year_2023)) },
            onClick = { onFilterAllBooksByYear2023(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.filter_books_by_year_2024)) },
            onClick = { onFilterAllBooksByYear2024(); closeMenu() }
        )
    }
}

@Composable
private fun TopAppBarDropdownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            content { expanded = !expanded }
        }
    }
}
