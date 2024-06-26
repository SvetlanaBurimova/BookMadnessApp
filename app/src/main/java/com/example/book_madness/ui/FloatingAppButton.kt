package com.example.book_madness.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.book_madness.R

@Composable
fun BookMadnessFloatingActionButton(
    navigateToBookEntry: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = { navigateToBookEntry() },
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.floating_book_entry_button),
            modifier = modifier
        )
    }
}
