package com.unicamp.dslearn.presentation.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.unicamp.dslearn.MainDispatcherRule
import com.unicamp.dslearn.core.model.TopicModel
import com.unicamp.dslearn.core.ui.theme.DSLearnTheme
import com.unicamp.dslearn.presentation.screens.home.HomeScreen
import com.unicamp.dslearn.presentation.screens.home.HomeViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val viewModel: HomeViewModel = mockk()

    @Test
    fun `when card is displayed, assert on card click calls callback function`() = runTest {
        val expectedTopicModels = listOf(
            TopicModel(
                name = "Array 0",
                content = "Um array é uma coleção de itens armazenados em locais de memória contíguos.",
                completed = false,
                unlocked = false
            ),
            TopicModel(
                name = "Array 2",
                content = "Um array é uma coleção de itens armazenados em locais de memória contíguos.",
                completed = false,
                unlocked = false

            ),
        )

        val pagingDataFlow = MutableStateFlow(PagingData.Companion.from(expectedTopicModels))
        every { viewModel.topicListState } returns pagingDataFlow

        composeTestRule.setContent {
            DSLearnTheme {
                HomeScreen(viewModel, onTopicClick = { name, content, completed ->
                    assertEquals(expectedTopicModels[0].name, name)
                    assertEquals(expectedTopicModels[0].content, content)
                }
                )
            }
        }

        mainDispatcherRule.scope.advanceUntilIdle()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Array 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Array 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Array 0").performClick()
    }
}