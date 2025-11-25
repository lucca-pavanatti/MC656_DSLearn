package com.unicamp.dslearn.presentation.screens.exercisedetail

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unicamp.dslearn.core.model.Difficulty
import com.unicamp.dslearn.core.model.ExerciseModel
import com.unicamp.dslearn.core.ui.theme.DSLearnTheme
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    viewModel: ExerciseDetailsViewModel = koinViewModel(),
    exercise: ExerciseModel,
    gradient: Pair<Color, Color> = Color(0xFFEC4899) to Color(0xFF8B5CF6),
    onBackClick: () -> Unit,
    onOpenUrl: (Context, String) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 140.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(gradient.first, gradient.second)
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = exercise.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            DifficultyBadge(difficulty = exercise.difficulty)
                        }
                    }
                }

                item {
                    InfoSection(
                        title = "Related Topics",
                        content = {
                            val topics = exercise.relatedTopics.split(",").map { it.trim() }
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(topics.size) { index ->
                                    TopicChip(topic = topics[index])
                                }
                            }
                        }
                    )
                }

                item {
                    InfoSection(
                        title = "Companies",
                        content = {
                            val companies = exercise.companies.split(",").map { it.trim() }
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                companies.forEach { company ->
                                    CompanyChip(company = company)
                                }
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onOpenUrl(context, exercise.url) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Open in LeetCode",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.setExerciseAsCompleted(exercise.id)
                        onBackClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = if (exercise.completed) {
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981),
                            contentColor = Color.White
                        )
                    } else {
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    },
                    border = if (!exercise.completed) {
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    } else null
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (exercise.completed) Icons.Default.Check else Icons.Default.CheckCircle,
                            contentDescription = null
                        )
                        Text(
                            text = if (exercise.completed) "Completed" else "Mark as Complete",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DifficultyBadge(difficulty: Difficulty) {
    val (backgroundColor, textColor) = when (difficulty) {
        Difficulty.EASY -> Color(0xFF10B981) to Color.White
        Difficulty.MEDIUM -> Color(0xFFF59E0B) to Color.White
        Difficulty.HARD -> Color(0xFFEF4444) to Color.White
        else -> Color.Gray to Color.White
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = difficulty.name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicChip(topic: String) {
    SuggestionChip(
        onClick = { },
        label = {
            Text(
                text = topic,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            labelColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyChip(company: String) {
    AssistChip(
        onClick = { },
        label = {
            Text(
                text = company,
                style = MaterialTheme.typography.bodySmall
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = AssistChipDefaults.assistChipBorder(
            enabled = true,
            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ExerciseDetailScreenPreview() {
    DSLearnTheme {
        ExerciseDetailScreen(
            exercise = ExerciseModel(
                id = 1,
                title = "Two Sum",
                url = "https://leetcode.com/problems/two-sum",
                difficulty = Difficulty.EASY,
                relatedTopics = "Array,Hash Table",
                companies = "Amazon,Google,Apple,Adobe,Microsoft,Bloomberg,Facebook,Oracle,Uber,Expedia,Twitter,Nagarro,SAP,Yahoo,Cisco",
                completed = false
            ),
            gradient = Color(0xFFEC4899) to Color(0xFF8B5CF6),
            onBackClick = { },
            onOpenUrl = { _, _ -> },
        )
    }
}