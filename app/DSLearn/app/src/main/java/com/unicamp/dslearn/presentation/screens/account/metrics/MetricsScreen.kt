package com.unicamp.dslearn.presentation.screens.account.metrics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.exitUntilCollapsedScrollBehavior
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unicamp.dslearn.core.model.UserMetricsModel
import com.unicamp.dslearn.presentation.composables.LoadingIndicator
import com.unicamp.dslearn.presentation.screens.home.getGradientForIndex
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricsScreen(
    viewModel: MetricsViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val scrollBehavior = exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MetricsTopBar(onBack = onBack, scrollBehavior = scrollBehavior)
        }
    ) { paddingValues ->
        when (uiState) {
            is MetricsUiState.Loaded -> {
                (uiState as MetricsUiState.Loaded).userMetrics?.let {
                    MetricsContent(
                        metrics = it,
                        modifier = Modifier.padding(paddingValues)
                    )
                }

            }

            MetricsUiState.Loading -> {
                LoadingIndicator()
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MetricsTopBar(
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Text(
                text = "Your Metrics",
                style = MaterialTheme.typography.headlineLarge
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun MetricsContent(
    metrics: UserMetricsModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {

        // --- HERO: OVERALL PROGRESS (replace donut with strong bar)
        Text(
            text = "Overall Progress",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        GradientHorizontalBar(
            label = "Completed",
            percentage = metrics.overallProgress / 100f,
            index = 0,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricMiniBox("Topics", metrics.completedTopics, metrics.totalTopics, 1)
            MetricMiniBox("Exercises", metrics.completedExercises, metrics.totalExercises, 2)
        }

        Text(
            text = "Completion by Difficulty",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        DifficultyBars(
            easy = metrics.easyPercentage,
            medium = metrics.mediumPercentage,
            hard = metrics.hardPercentage
        )
    }
}

@Composable
fun MetricMiniBox(
    title: String,
    completed: Int,
    total: Int,
    index: Int
) {
    val (start, end) = getGradientForIndex(index)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .width(150.dp)
            .height(90.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(start, end)))
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(title, color = Color.White.copy(0.9f), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "$completed / $total",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DifficultyBars(
    easy: Float,
    medium: Float,
    hard: Float
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GradientHorizontalBar("Easy", easy, index = 0)
        GradientHorizontalBar("Medium", medium, index = 1)
        GradientHorizontalBar("Hard", hard, index = 2)
    }
}


@Composable
fun GradientHorizontalBar(
    label: String,
    percentage: Float,
    index: Int,
    modifier: Modifier = Modifier
) {
    val (startColor, endColor) = com.unicamp.dslearn.presentation.screens.exercises.getGradientForIndex(
        index
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text("${(percentage * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color(0xFFE5E7EB), shape = RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage)
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(listOf(startColor, endColor))
                    )
            )
        }
    }
}
