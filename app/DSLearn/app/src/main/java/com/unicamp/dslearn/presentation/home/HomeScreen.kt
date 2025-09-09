package com.unicamp.dslearn.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.unicamp.dslearn.R
import com.unicamp.dslearn.core.model.CardModel
import com.unicamp.dslearn.presentation.composables.Card
import com.unicamp.dslearn.ui.theme.DSLearnTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel(), onCardClick: (Int, String) -> Unit) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { HomeTopBar(scrollBehavior) }
    ) { paddingValues ->
        val cardPagingItems: LazyPagingItems<CardModel> =
            viewModel.cardListState.collectAsLazyPagingItems()

        HomeScreen(
            Modifier.padding(paddingValues),
            cardPagingItems,
            viewModel.searchQueryState,
            onCardClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    cardPagingItems: LazyPagingItems<CardModel>,
    searchQuery: TextFieldState,
    onCardClick: (Int, String) -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        SearchBar(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery.text.toString(),
                    onQueryChange = {
                        searchQuery.edit { replace(0, length, it) }
                    },
                    onSearch = { },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text(text = stringResource(id = R.string.search)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        val isLoadingCards = cardPagingItems.loadState.refresh is LoadState.Loading
                        SearchBarTrailingIcon(isLoadingCards = isLoadingCards) {
                            searchQuery.clearText()
                        }
                    },
                )
            },
            expanded = false,
            onExpandedChange = { },
            shape = SearchBarDefaults.inputFieldShape,
            tonalElevation = SearchBarDefaults.TonalElevation,
            shadowElevation = SearchBarDefaults.ShadowElevation,
            windowInsets = WindowInsets(0, 0, 0, 0),
            content = {

            },
        )

        LazyColumn(modifier = Modifier.padding(top = 24.dp)) {
            items(cardPagingItems.itemCount) { index ->
                cardPagingItems[index]?.let { cardModel ->
                    Card(
                        name = cardModel.name,
                        theory = cardModel.theory,
                        exercises = cardModel.exercises
                    ) {
                        onCardClick(cardModel.id, cardModel.name)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    MediumTopAppBar(
        modifier = Modifier.padding(start = 24.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        title = {
            Text(
                text = stringResource(id = R.string.home),
                style = MaterialTheme.typography.headlineLarge
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun SearchBarTrailingIcon(isLoadingCards: Boolean, onSearchTrailingIconClick: () -> Unit) {
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
fun HomeScreenPreview() {
    DSLearnTheme {
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