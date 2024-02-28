package com.example.book_madness.ui.bookItem

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.book_madness.R
import com.example.book_madness.model.BookDetails
import com.example.book_madness.model.ratingList
import com.example.book_madness.ui.AppViewModelFactoryProvider
import com.example.book_madness.ui.navigation.BookMadnessTitlesResId
import com.example.book_madness.ui.theme.AppTheme
import com.example.book_madness.util.BookMadnessTopAppBar
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BookEntryScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: BookEntryViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(BookMadnessTitlesResId.BOOK_ADD_SCREEN),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        BookEntryBody(
            bookUiState = viewModel.bookUiState,
            onBookValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveBook()
                    navigateBack()
                }
            },
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun BookEntryBody(
    bookUiState: BookUiState,
    modifier: Modifier = Modifier,
    onBookValueChange: (BookDetails) -> Unit,
    onSaveClick: () -> Unit
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
    onValueChange: (BookDetails) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium))
    ) {
        OutlinedTextField(
            value = bookDetails.name,
            onValueChange = { onValueChange(bookDetails.copy(name = it)) },
            keyboardOptions =
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
            label = { Text(stringResource(R.string.book_name_required)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.genre,
            onValueChange = { onValueChange(bookDetails.copy(genre = it)) },
            keyboardOptions =
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
            label = { Text(stringResource(R.string.book_genre_required)) },
            modifier = modifier
        )
        RatingDropdownMenu(
            bookDetails = bookDetails,
            onValueChange = onValueChange,
            ratingList = ratingList,
            focusManager = focusManager
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.book_paper_format))
            Checkbox(
                checked = bookDetails.paper,
                onCheckedChange = {
                    onValueChange(bookDetails.copy(paper = it))
                    focusManager.clearFocus()
                }
            )
        }
        CustomDatePicker(
            bookDetails = bookDetails.startDate ?: "",
            focusManager = focusManager,
            fieldNameRes = R.string.book_details_start_date,
            modifier = modifier,
            onClearButtonClick = { onValueChange(bookDetails.copy(startDate = null)) },
            onAccept = {
                if (it != null) {
                    onValueChange(
                        bookDetails.copy(
                            startDate = Instant
                                .ofEpochMilli(it)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                                .format(DateTimeFormatter.ofPattern("dd MMM uuuu"))
                        )
                    )
                }
            }
        )
        CustomDatePicker(
            bookDetails = bookDetails.finishDate ?: "",
            focusManager = focusManager,
            fieldNameRes = R.string.book_details_finish_date,
            modifier = modifier,
            onClearButtonClick = { onValueChange(bookDetails.copy(finishDate = null)) },
            onAccept = {
                if (it != null) {
                    onValueChange(
                        bookDetails.copy(
                            finishDate = Instant
                                .ofEpochMilli(it)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                                .format(DateTimeFormatter.ofPattern("dd MMM uuuu"))
                        )
                    )
                }
            }
        )
        OutlinedTextField(
            value = bookDetails.author ?: "",
            onValueChange = { onValueChange(bookDetails.copy(author = it)) },
            keyboardOptions =
            KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(R.string.book_author)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.notes ?: "",
            onValueChange = { onValueChange(bookDetails.copy(notes = it)) },
            keyboardOptions =
            KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            label = { Text(stringResource(R.string.book_notes)) },
            singleLine = false,
            modifier = modifier
        )
        Text(
            text = stringResource(R.string.required_fields),
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.medium))
        )
    }
}

@Composable
fun CustomDatePicker(
    bookDetails: String,
    focusManager: FocusManager,
    @StringRes fieldNameRes: Int,
    modifier: Modifier = Modifier,
    onClearButtonClick: () -> Unit,
    onAccept: (Long?) -> Unit
) {
    val isOpen = remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            readOnly = true,
            value = bookDetails,
            label = { Text(stringResource(fieldNameRes)) },
            onValueChange = { /* Do nothing */ },
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    isOpen.value = true
                }
            }
        )
        ClearIconButton(
            focusManager = focusManager,
            onClearButtonClick = { onClearButtonClick() }
        )
    }

    if (isOpen.value) {
        CustomDatePickerDialog(
            onAccept = {
                onAccept(it)
                isOpen.value = false
                focusManager.clearFocus()
            },
            onCancel = {
                isOpen.value = false
                focusManager.clearFocus()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = { /* Do nothing */ },
        confirmButton = {
            Button(onClick = { onAccept(state.selectedDateMillis) }) {
                Text(stringResource(id = R.string.accept))
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RatingDropdownMenu(
    bookDetails: BookDetails,
    ratingList: List<String>,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
    onValueChange: (BookDetails) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) expanded = true
            }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = bookDetails.rating ?: "",
                onValueChange = { /* Do nothing */ },
                label = { Text(stringResource(R.string.book_rating)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    focusManager.clearFocus()
                }
            ) {
                ratingList.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onValueChange(bookDetails.copy(rating = selectionOption))
                            expanded = false
                            focusManager.clearFocus()
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
        ClearIconButton(
            focusManager = focusManager,
            onClearButtonClick = { onValueChange(bookDetails.copy(rating = null)) }
        )
    }
}

@Composable
fun ClearIconButton(
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
    onClearButtonClick: () -> Unit
) {
    IconButton(
        onClick = {
            onClearButtonClick()
            focusManager.clearFocus()
        }
    ) {
        Icon(
            painterResource(id = R.drawable.clear), contentDescription = null,
            modifier = modifier.size(dimensionResource(id = R.dimen.extra_large)),
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
                    notes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
                )
            ),
            onBookValueChange = { /* Do nothing */ },
            onSaveClick = { /* Do nothing */ }
        )
    }
}
