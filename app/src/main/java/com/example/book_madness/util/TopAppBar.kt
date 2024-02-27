package com.example.book_madness.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.book_madness.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMadnessTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier
            .background(MaterialTheme.colorScheme.outline)
            .padding(bottom = dimensionResource(id = R.dimen.extra_small))
    )
}
