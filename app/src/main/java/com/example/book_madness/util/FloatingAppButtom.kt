package com.example.book_madness.util

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
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = { /* do something */ },
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.book_entry_title)
        )
    }
}
