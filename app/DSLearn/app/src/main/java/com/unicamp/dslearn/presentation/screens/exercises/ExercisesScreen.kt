package com.unicamp.dslearn.presentation.screens.exercises

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.unicamp.dslearn.R
import com.unicamp.dslearn.core.model.TopicModel
import com.unicamp.dslearn.presentation.composables.SearchBarTrailingIcon
import com.unicamp.dslearn.presentation.composables.Topic
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    viewModel: ExercisesViewModel = koinViewModel(),
    onTopicClick: (String, String, Boolean) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { ExercisesTopBar(scrollBehavior) }
    ) { paddingValues ->

        val exercisePagingItems: LazyPagingItems<TopicModel> =
            viewModel.exerciseListState.collectAsLazyPagingItems()

        ExercisesScreen(
            Modifier.padding(paddingValues), exercisePagingItems,
            viewModel.searchQueryState,
            onTopicClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExercisesScreen(
    modifier: Modifier = Modifier,
    exercisePagingItems: LazyPagingItems<TopicModel>,
    searchQuery: TextFieldState,
    onTopicClick: (String, String, Boolean) -> Unit
) {
    Box(modifier = modifier) {
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
                            val isLoadingCards =
                                exercisePagingItems.loadState.refresh is LoadState.Loading
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
                items(exercisePagingItems.itemCount) { index ->
                    exercisePagingItems[index]?.let { topicModel ->
                        Topic(
                            name = topicModel.name,
                            content = topicModel.content
                        ) {
                            onTopicClick(topicModel.name, topicModel.content, topicModel.completed)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExercisesTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    MediumTopAppBar(
        modifier = Modifier.padding(start = 24.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        title = {
            Text(
                text = "Exercises",
                style = MaterialTheme.typography.headlineLarge
            )
        },
        scrollBehavior = scrollBehavior
    )
}


