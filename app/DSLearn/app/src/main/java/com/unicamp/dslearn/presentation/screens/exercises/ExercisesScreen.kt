package com.unicamp.dslearn.presentation.screens.exercises

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.unicamp.dslearn.R
import com.unicamp.dslearn.core.model.ExerciseModel
import com.unicamp.dslearn.presentation.composables.Exercise
import com.unicamp.dslearn.presentation.composables.SearchBarTrailingIcon
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    viewModel: ExercisesViewModel = koinViewModel(),
    onExerciseClick: (ExerciseModel) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var selectedTopic by remember { mutableStateOf<String?>(null) }
    var selectedCompany by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedDifficulty, selectedTopic, selectedCompany) {
        viewModel.updateFilters(
            difficulty = selectedDifficulty,
            topic = selectedTopic,
            company = selectedCompany
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { ExercisesTopBar(scrollBehavior, "Exercises") }
    ) { paddingValues ->
        val exercisePagingItems: LazyPagingItems<ExerciseModel> =
            viewModel.exerciseListState.collectAsLazyPagingItems()

        ExercisesContent(
            modifier = Modifier.padding(paddingValues),
            exercisePagingItems = exercisePagingItems,
            searchQuery = viewModel.searchQueryState,
            selectedDifficulty = selectedDifficulty,
            selectedTopic = selectedTopic,
            selectedCompany = selectedCompany,
            onDifficultySelected = {
                selectedDifficulty = if (selectedDifficulty == it) null else it
            },
            onTopicSelected = { selectedTopic = if (selectedTopic == it) null else it },
            onCompanySelected = { selectedCompany = if (selectedCompany == it) null else it },
            onExerciseClick = onExerciseClick,
            showTopicFilter = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicExercisesScreen(
    topicName: String,
    viewModel: ExercisesViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onExerciseClick: (ExerciseModel) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var selectedCompany by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedDifficulty, selectedCompany) {
        viewModel.updateFilters(
            difficulty = selectedDifficulty,
            topic = topicName,
            company = selectedCompany
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExercisesTopBarWithBack(
                scrollBehavior = scrollBehavior,
                title = "$topicName Exercises",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        val exercisePagingItems: LazyPagingItems<ExerciseModel> =
            viewModel.exerciseListState.collectAsLazyPagingItems()

        ExercisesContent(
            modifier = Modifier.padding(paddingValues),
            exercisePagingItems = exercisePagingItems,
            searchQuery = viewModel.searchQueryState,
            selectedDifficulty = selectedDifficulty,
            selectedTopic = topicName,
            selectedCompany = selectedCompany,
            onDifficultySelected = {
                selectedDifficulty = if (selectedDifficulty == it) null else it
            },
            onTopicSelected = { },
            onCompanySelected = { selectedCompany = if (selectedCompany == it) null else it },
            onExerciseClick = onExerciseClick,
            showTopicFilter = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExercisesContent(
    modifier: Modifier = Modifier,
    exercisePagingItems: LazyPagingItems<ExerciseModel>,
    searchQuery: androidx.compose.foundation.text.input.TextFieldState,
    selectedDifficulty: String?,
    selectedTopic: String?,
    selectedCompany: String?,
    onDifficultySelected: (String) -> Unit,
    onTopicSelected: (String) -> Unit,
    onCompanySelected: (String) -> Unit,
    onExerciseClick: (ExerciseModel) -> Unit,
    showTopicFilter: Boolean
) {
    var showFilterMenu by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            SearchBar(
                modifier = Modifier.align(Alignment.Center),
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val isLoadingCards =
                                    exercisePagingItems.loadState.refresh is LoadState.Loading
                                SearchBarTrailingIcon(isLoadingCards = isLoadingCards) {
                                    searchQuery.clearText()
                                }

                                Box {
                                    IconButton(onClick = { showFilterMenu = true }) {
                                        Badge(
                                            containerColor = if (selectedDifficulty != null ||
                                                (showTopicFilter && selectedTopic != null) ||
                                                selectedCompany != null
                                            )
                                                MaterialTheme.colorScheme.primary
                                            else Color.Transparent
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Menu,
                                                contentDescription = "Filters",
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }

                                    DropdownMenu(
                                        expanded = showFilterMenu,
                                        onDismissRequest = { showFilterMenu = false },
                                        modifier = Modifier.widthIn(min = 300.dp, max = 350.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = "Difficulty",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp,
                                                    vertical = 8.dp
                                                )
                                            )

                                            FilterChipRowInMenu(
                                                options = listOf("Easy", "Medium", "Hard"),
                                                selectedOption = selectedDifficulty,
                                                onOptionSelected = onDifficultySelected
                                            )

                                            if (showTopicFilter) {
                                                HorizontalDivider(
                                                    modifier = Modifier.padding(
                                                        vertical = 12.dp
                                                    )
                                                )

                                                Text(
                                                    text = "Topic",
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.padding(
                                                        horizontal = 8.dp,
                                                        vertical = 8.dp
                                                    )
                                                )

                                                FlexibleFilterChipGrid(
                                                    options = listOf(
                                                        "Array",
                                                        "String",
                                                        "Tree",
                                                        "Graph",
                                                        "DP",
                                                        "Sorting",
                                                        "Hash Table",
                                                        "Binary Search",
                                                        "Stack",
                                                        "Queue"
                                                    ),
                                                    selectedOption = selectedTopic,
                                                    onOptionSelected = onTopicSelected
                                                )
                                            }

                                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                                            Text(
                                                text = "Company",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp,
                                                    vertical = 8.dp
                                                )
                                            )

                                            FlexibleFilterChipGrid(
                                                options = listOf(
                                                    "Google",
                                                    "Meta",
                                                    "Amazon",
                                                    "Microsoft",
                                                    "Apple",
                                                    "Netflix",
                                                    "Adobe",
                                                    "Uber"
                                                ),
                                                selectedOption = selectedCompany,
                                                onOptionSelected = onCompanySelected
                                            )

                                            if (selectedDifficulty != null || (showTopicFilter && selectedTopic != null) || selectedCompany != null) {
                                                HorizontalDivider(
                                                    modifier = Modifier.padding(
                                                        vertical = 12.dp
                                                    )
                                                )
                                                TextButton(
                                                    onClick = {
                                                        onDifficultySelected("")
                                                        if (showTopicFilter) onTopicSelected("")
                                                        onCompanySelected("")
                                                        showFilterMenu = false
                                                    },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    colors = ButtonDefaults.textButtonColors(
                                                        contentColor = MaterialTheme.colorScheme.error
                                                    )
                                                ) {
                                                    Text("Clear All Filters")
                                                }
                                            }
                                        }
                                    }
                                }
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
                content = { },
            )
        }

        if (selectedDifficulty != null || (showTopicFilter && selectedTopic != null) || selectedCompany != null) {
            ActiveFiltersRow(
                selectedDifficulty = selectedDifficulty,
                selectedTopic = if (showTopicFilter) selectedTopic else null,
                selectedCompany = selectedCompany,
                onDifficultyRemoved = { onDifficultySelected("") },
                onTopicRemoved = { onTopicSelected("") },
                onCompanyRemoved = { onCompanySelected("") },
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            items(exercisePagingItems.itemCount) { index ->
                exercisePagingItems[index]?.let { exercise ->
                    val gradient = getGradientForIndex(index)
                    Exercise(
                        name = exercise.title,
                        content = exercise.url,
                        gradient = gradient,
                        isCompleted = exercise.completed
                    ) {
                        onExerciseClick(exercise)
                    }
                }
            }

            item {
                if (exercisePagingItems.loadState.append is LoadState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterChipRowInMenu(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = selectedOption == option,
                onClick = { onOptionSelected(option) },
                label = { Text(option) }
            )
        }
    }
}

@Composable
private fun FlexibleFilterChipGrid(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val rows = mutableListOf<List<String>>()
        var currentRow = mutableListOf<String>()
        var currentRowLength = 0

        options.forEach { option ->
            val chipLength = option.length
            val estimatedWidth = when {
                chipLength < 6 -> 70
                chipLength < 10 -> 90
                else -> 110
            }

            if (currentRow.isEmpty() || currentRowLength + estimatedWidth < 320) {
                currentRow.add(option)
                currentRowLength += estimatedWidth
            } else {
                rows.add(currentRow.toList())
                currentRow = mutableListOf(option)
                currentRowLength = estimatedWidth
            }
        }
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }

        rows.forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowOptions.forEach { option ->
                    FilterChip(
                        selected = selectedOption == option,
                        onClick = { onOptionSelected(option) },
                        label = { Text(option) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - rowOptions.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActiveFiltersRow(
    selectedDifficulty: String?,
    selectedTopic: String?,
    selectedCompany: String?,
    onDifficultyRemoved: () -> Unit,
    onTopicRemoved: () -> Unit,
    onCompanyRemoved: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier
    ) {
        selectedDifficulty?.let {
            item {
                AssistChip(
                    onClick = onDifficultyRemoved,
                    label = { Text("Difficulty: $it") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove filter",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }

        selectedTopic?.let {
            item {
                AssistChip(
                    onClick = onTopicRemoved,
                    label = { Text("Topic: $it") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove filter",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }

        selectedCompany?.let {
            item {
                AssistChip(
                    onClick = onCompanyRemoved,
                    label = { Text("Company: $it") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove filter",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExercisesTopBar(scrollBehavior: TopAppBarScrollBehavior, title: String) {
    MediumTopAppBar(
        modifier = Modifier.padding(start = 24.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExercisesTopBarWithBack(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    onBackClick: () -> Unit
) {
    MediumTopAppBar(
        modifier = Modifier.padding(start = 8.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        scrollBehavior = scrollBehavior
    )
}

fun getGradientForIndex(index: Int): Pair<Color, Color> {
    val spectrum = listOf(
        Color(0xFFEC4899), Color(0xFF8B5CF6), Color(0xFF3B82F6),
        Color(0xFF22D3EE), Color(0xFF10B981), Color(0xFFF59E0B),
        Color(0xFFEF4444), Color(0xFFFB923C), Color(0xFFA855F7)
    )

    val goldenRatio = 1.618033988749895
    val position1 = (index * goldenRatio) % spectrum.size
    val position2 = ((index + 1) * goldenRatio + 2.5) % spectrum.size

    return spectrum[position1.toInt()] to spectrum[position2.toInt()]
}