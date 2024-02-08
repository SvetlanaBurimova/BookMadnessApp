package com.example.book_madness

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMadnessTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier
            .background(MaterialTheme.colorScheme.outline)
            .padding(bottom = 2.dp)
    )
}

@Composable
fun BookMadnessFloatingActionButton() {
    FloatingActionButton(
        onClick = {  },
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(dimensionResource(id = R.dimen.large))
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.book_entry_title)
        )
    }
}

@Composable
fun BookMadnessEmptyScreen() {
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
