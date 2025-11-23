package com.unicamp.dslearn.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.unicamp.dslearn.R
import com.unicamp.dslearn.core.model.TopicModel
import com.unicamp.dslearn.core.ui.theme.DSLearnTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onTopicClick: (String, String, Boolean) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val topicPagingItems: LazyPagingItems<TopicModel> =
        viewModel.topicListState.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .navigationBarsPadding(),
        topBar = { HomeTopBar(scrollBehavior) }) { paddingValues ->

        HomeScreen(modifier = Modifier.padding(paddingValues), topicPagingItems, onTopicClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    topicPagingItems: LazyPagingItems<TopicModel>,
    onTopicClick: (String, String, Boolean) -> Unit
) {
    DuolingoTopicList(modifier, topicPagingItems, onTopicClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    MediumTopAppBar(
        modifier = Modifier
            .padding(start = 24.dp),
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
fun DuolingoTopicList(
    modifier: Modifier = Modifier,
    topicPagingItems: LazyPagingItems<TopicModel>,
    onTopicClick: (String, String, Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = topicPagingItems.itemCount,
            ) { index ->
                val topic = topicPagingItems[index]
                topic?.let {
                    TopicCard(
                        topic = topic,
                        isLeft = index % 2 == 0,
                        gradient = getGradientForIndex(index),
                        onToggleComplete = {
                            onTopicClick(topic.name, topic.content, topic.completed)
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun TopicCard(
    topic: TopicModel,
    isLeft: Boolean,
    gradient: Pair<Color, Color>,
    onToggleComplete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .align(Alignment.Center)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF8B5CF6).copy(alpha = 0.3f),
                            Color(0xFF3B82F6).copy(alpha = 0.3f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = if (isLeft) Arrangement.Start else Arrangement.End
        ) {
            Card(
                modifier = Modifier
                    .width(180.dp)
                    .height(100.dp)
                    .clickable(enabled = topic.unlocked) { onToggleComplete() },
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = if (topic.unlocked) {
                                    listOf(gradient.first, gradient.second)
                                } else {
                                    listOf(Color(0xFF4B5563), Color(0xFF6B7280))
                                }
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (!topic.unlocked) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Text(
                            text = topic.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        if (topic.completed) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(
                    when {
                        topic.completed -> Color(0xFF4ADE80)
                        topic.unlocked -> Color(0xFF60A5FA)
                        else -> Color(0xFF6B7280)
                    }
                )
        )
    }
}

fun getGradientForIndex(index: Int): Pair<Color, Color> {
    val spectrum = listOf(
        Color(0xFFEC4899),
        Color(0xFF8B5CF6),
        Color(0xFF3B82F6),
        Color(0xFF22D3EE),
        Color(0xFF10B981),
        Color(0xFFF59E0B)
    )

    val position = (index * 0.382f) % spectrum.size
    val first = position.toInt() % spectrum.size
    val second = (first + 1) % spectrum.size

    return spectrum[first] to spectrum[second]
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DSLearnTheme {}
}