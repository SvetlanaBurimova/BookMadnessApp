package com.example.book_madness.util

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.book_madness.R
import com.example.book_madness.ui.bookItem.BookDetailsUiState
import com.example.book_madness.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMadnessTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    canNavigateBack: Boolean = true,
    showFilterIcon: Boolean = false,
    showShareButton: Boolean = false,
    showSearchButton: Boolean = false,
    expandedInitially: Boolean = false,
    onFilterAllBooks: () -> Unit = {},
    onFilterAllBooksByName: () -> Unit = {},
    onFilterAllBooksByRating: () -> Unit = {},
    onFilterAllBooksByTBR: () -> Unit = {},
    onFilterAllBooksByYear2023: () -> Unit = {},
    onFilterAllBooksByYear2024: () -> Unit = {},
    onSearchDisplayChanged: (String) -> Unit = {},
    navigateUp: () -> Unit = {},
    bookDetailsUiState: BookDetailsUiState = BookDetailsUiState(),
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    BackArrowIcon()
                }
            }
        },
        actions = {
            if (showSearchButton)
                ExpandableSearchView(
                    searchQuery = searchQuery,
                    onSearchDisplayChanged = onSearchDisplayChanged,
                    expandedInitially = expandedInitially,
                    modifier = modifier.weight(1f)
                )
            if (showFilterIcon)
                DropdownBookFilters(
                    onFilterAllBooks,
                    onFilterAllBooksByName,
                    onFilterAllBooksByRating,
                    onFilterAllBooksByTBR,
                    onFilterAllBooksByYear2023,
                    onFilterAllBooksByYear2024
                )
            if (showShareButton)
                ShareButton(
                    bookDetailsUiState,
                    LocalContext.current
                )
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
                contentDescription = stringResource(id = R.string.share_icon_button)
            )
        }
    ) { closeMenu ->
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.share_button)) },
            onClick = {
                ContextCompat.startActivity(context, shareIntent, null)
                closeMenu()
            }
        )
    }
}

@Composable
fun ExpandableSearchView(
    searchQuery: String,
    onSearchDisplayChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    expandedInitially: Boolean = false,
) {
    val (expanded, onExpandedChanged) = remember {
        mutableStateOf(expandedInitially)
    }

    when (expanded) {
        true -> ExpandedSearchView(
            onSearchDisplayChanged = onSearchDisplayChanged,
            onExpandedChanged = onExpandedChanged,
            searchQuery = searchQuery,
            modifier = modifier
        )
        false -> {
            onSearchDisplayChanged("")
            IconButton(
                onClick = { onExpandedChanged(true) }
            ) {
                SearchIcon()
            }
        }
    }
}

@Composable
fun ExpandedSearchView(
    searchQuery: String,
    onSearchDisplayChanged: (String) -> Unit,
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val textFieldFocusRequester = remember { FocusRequester() }

    SideEffect { textFieldFocusRequester.requestFocus() }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(
            onClick = { onExpandedChanged(false) }
        ) { BackArrowIcon() }
        TextField(
            value = searchQuery,
            onValueChange = { onSearchDisplayChanged(it) },
            modifier = Modifier.focusRequester(textFieldFocusRequester),
            label = { Text(text = stringResource(id = R.string.search_field_label)) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchDisplayChanged("") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )
    }
}

@Composable
private fun DropdownBookFilters(
    onFilterAllBooks: () -> Unit,
    onFilterAllBooksByName: () -> Unit,
    onFilterAllBooksByRating: () -> Unit,
    onFilterAllBooksByTBR: () -> Unit,
    onFilterAllBooksByYear2023: () -> Unit,
    onFilterAllBooksByYear2024: () -> Unit
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                painter = painterResource(id = R.drawable.filter_list),
                contentDescription = stringResource(id = R.string.filter_button)
            )
        }
    ) { closeMenu ->
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.all_books_filter_button)) },
            onClick = { onFilterAllBooks(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.books_by_name_filter_button)) },
            onClick = { onFilterAllBooksByName(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.books_by_rating_filter_button)) },
            onClick = { onFilterAllBooksByRating(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.tbr_filter_button)) },
            onClick = { onFilterAllBooksByTBR(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.books_by_year_2023_filter_button)) },
            onClick = { onFilterAllBooksByYear2023(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.books_by_year_2024_filter_button)) },
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

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            content { expanded = !expanded }
        }
    }
}

@Composable
fun BackArrowIcon() {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = stringResource(id = R.string.back_button)
    )
}

@Composable
fun SearchIcon() {
    Icon(
        imageVector = Icons.Default.Search,
        contentDescription = stringResource(id = R.string.search_icon)
    )
}

@Preview
@Composable
fun ExpandedSearchViewPreview() {
    AppTheme {
        Surface {
            ExpandableSearchView(
                onSearchDisplayChanged = {},
                expandedInitially = true,
                searchQuery = ""
            )
        }
    }
}
