package com.example.book_madness.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.book_madness.R

@Composable
fun BookMadnessRatingIcon(
    icon: Painter,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = icon,
        contentDescription = stringResource(id = R.string.rating_icon),
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier.size(dimensionResource(id = R.dimen.extra_large))
    )
}
