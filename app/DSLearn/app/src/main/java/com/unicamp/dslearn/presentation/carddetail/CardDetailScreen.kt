package com.unicamp.dslearn.presentation.carddetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CardDetailScreen(cardId: Int) {
    Scaffold { paddingValues ->
        CardDetailsScreen(Modifier.padding(paddingValues))
    }
}

@Composable
private fun CardDetailsScreen(modifier: Modifier = Modifier) {
}