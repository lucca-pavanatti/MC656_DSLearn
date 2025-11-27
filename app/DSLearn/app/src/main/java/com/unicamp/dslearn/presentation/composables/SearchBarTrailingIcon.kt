package com.unicamp.dslearn.presentation.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unicamp.dslearn.core.ui.theme.DSLearnTheme

@Composable
fun SearchBarTrailingIcon(isLoadingCards: Boolean, onSearchTrailingIconClick: () -> Unit) {
    if (isLoadingCards) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(32.dp),
            color = Color.White
        )
    } else {
        IconButton(onClick = { onSearchTrailingIconClick() }) {
            Icon(
                imageVector = Icons.Default.Clear,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchBarTrailingIconIsLoadingPreview() {
    DSLearnTheme {
        SearchBarTrailingIcon(isLoadingCards = true) { }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarTrailingIconIsNotLoadingPreview() {
    DSLearnTheme {
        SearchBarTrailingIcon(isLoadingCards = false) { }
    }
}