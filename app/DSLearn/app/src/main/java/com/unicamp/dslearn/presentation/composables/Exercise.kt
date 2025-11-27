package com.unicamp.dslearn.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unicamp.dslearn.core.ui.theme.DSLearnTheme

@Composable
fun Exercise(
    modifier: Modifier = Modifier,
    name: String,
    content: String,
    gradient: Pair<Color, Color> = Color(0xFFEC4899) to Color(0xFF8B5CF6),
    isCompleted: Boolean = false,
    onCardClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(gradient.first, gradient.second)
                    )
                )
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    if (isCompleted) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Navigate",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExercisePreview() {
    DSLearnTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Exercise(
                name = "Two Sum",
                content = "Given an array of integers, return indices of two numbers that add up to target",
                gradient = Color(0xFFEC4899) to Color(0xFF8B5CF6),
                isCompleted = true
            ) { }

            Exercise(
                name = "Binary Search",
                content = "Search algorithm",
                gradient = Color(0xFF3B82F6) to Color(0xFF22D3EE),
                isCompleted = false
            ) { }

            Exercise(
                name = "Merge Sort",
                content = "Divide and conquer algorithm that splits array into halves and merges them sorted",
                gradient = Color(0xFF10B981) to Color(0xFFF59E0B),
                isCompleted = false
            ) { }
        }
    }
}