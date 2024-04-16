package com.example.book_madness.ui.bookItem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.book_madness.ui.AppViewModelFactoryProvider
import com.example.book_madness.ui.navigation.BookMadnessTitlesResId
import com.example.book_madness.ui.theme.AppTheme
import com.example.book_madness.ui.BookMadnessTopAppBar
import kotlinx.coroutines.launch

@Composable
fun BookEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookEditViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(BookMadnessTitlesResId.BOOK_EDIT_SCREEN),
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
                }
                navigateBack()
            },
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookEditScreenPreview() {
    AppTheme {
        BookEditScreen(navigateBack = { /*Do nothing*/ })
    }
}
