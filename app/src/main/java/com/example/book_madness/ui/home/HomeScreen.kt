package com.example.book_madness.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.book_madness.R
import com.example.book_madness.data.source.Book
import com.example.book_madness.model.FilterType
import com.example.book_madness.model.FilterType.ID
import com.example.book_madness.model.FilterType.NAME
import com.example.book_madness.model.FilterType.RATING
import com.example.book_madness.model.FilterType.TBR
import com.example.book_madness.model.FilterType.YEAR_2023
import com.example.book_madness.model.FilterType.YEAR_2024
import com.example.book_madness.ui.AppViewModelFactoryProvider
import com.example.book_madness.ui.navigation.BookMadnessTitlesResId
import com.example.book_madness.ui.theme.AppTheme
import com.example.book_madness.util.BookMadnessFloatingActionButton
import com.example.book_madness.util.BookMadnessRatingIcon
import com.example.book_madness.util.BookMadnessTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigateToBookEntry: () -> Unit,
    navigateToBookDetails: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val (currentFilter, setCurrentFilter) = remember { mutableStateOf(ID) }

    val onFilterSelected: (FilterType) -> Unit = { filter ->
        setCurrentFilter(filter)
        handleFilterSelection(filter, viewModel)
    }

    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(BookMadnessTitlesResId.HOME_SCREEN),
                canNavigateBack = false,
                showFilterIcon = true,
                showSearchButton = true,
                searchQuery = viewModel.searchQuery,
                onSearchDisplayChanged = { viewModel.onSearchQueryChange(it) },
                currentFilter = currentFilter,
                onFilterSelected = onFilterSelected
            )
        },
        floatingActionButton = { BookMadnessFloatingActionButton(navigateToBookEntry = navigateToBookEntry) },
        bottomBar = { bottomNavigationBar() }
    ) { innerPadding ->
        HomeBody(
            bookList = homeUiState.bookList,
            onBookClick = navigateToBookDetails,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteBook(it)
                }
            },
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    bookList: List<Book>,
    onBookClick: (Int) -> Unit,
    onDelete: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (bookList.isEmpty()) {
            HomeEmptyScreen()
        } else {
            BookList(
                bookList = bookList,
                onItemClick = { onBookClick(it.id) },
                onDelete = { onDelete(it) },
                modifier = modifier
            )
        }
    }
}

@Composable
fun HomeEmptyScreen(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.empty_screen_animation)
    )

    LottieAnimation(
        composition = composition,
        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.extra_large))
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
    onDelete: (Book) -> Unit,
    onItemClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = bookList, key = { it.id }) { book ->
            SwipeToDeleteContainer(
                book = book,
                onDelete = { onDelete(book) }
            ) {
                BookItem(
                    book = book,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.small))
                        .clickable { onItemClick(book) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteContainer(
    book: Book,
    onDelete: (Book) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (Book) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved) {
            delay(animationDuration.toLong())
            onDelete(book)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            background = { DeleteBackground() },
            dismissContent = { content(book) },
            directions = setOf(DismissDirection.EndToStart)
        )
    }
}

@Composable
fun DeleteBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null
        )
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = book.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))

                if (book.rating != null) {
                    Text(
                        text = book.rating,
                        style = MaterialTheme.typography.titleLarge
                    )
                    BookMadnessRatingIcon(painterResource(id = R.drawable.round_star))
                } else {
                    Text(
                        text = "0",
                        style = MaterialTheme.typography.titleLarge
                    )
                    BookMadnessRatingIcon(painterResource(id = R.drawable.round_star_border))
                }
            }

            if (book.startDate != null) {
                ReadingDate(R.string.start_screen_start_date_field, book.startDate)
            }
            if (book.finishDate != null) {
                ReadingDate(R.string.start_screen_finish_date_field, book.finishDate)
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
                    genre = "Fantasy"
                )
            ),
            onBookClick = { /* Do nothing */ },
            onDelete = { /* Do nothing */ }
        )
    }
}

fun handleFilterSelection(filter: FilterType, viewModel: HomeViewModel) {
    when (filter) {
        ID -> viewModel.filterBooks(ID)
        NAME -> viewModel.filterBooks(NAME)
        RATING -> viewModel.filterBooks(RATING)
        TBR -> viewModel.filterBooks(TBR)
        YEAR_2023 -> viewModel.filterBooks(YEAR_2023)
        YEAR_2024 -> viewModel.filterBooks(YEAR_2024)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenEmptyPreview() {
    AppTheme {
        HomeBody(
            bookList = emptyList(),
            onBookClick = { /* Do nothing */ },
            onDelete = { /* Do nothing */ }
        )
    }
}
