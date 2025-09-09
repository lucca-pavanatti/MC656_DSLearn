package com.unicamp.dslearn.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.unicamp.dslearn.MainDispatcherRule
import com.unicamp.dslearn.core.model.CardModel
import com.unicamp.dslearn.ui.theme.DSLearnTheme
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
        val expectedCardModels = listOf(
            CardModel(
                id = 1,
                name = "Array 0",
                theory = "Um array é uma coleção de itens armazenados em locais de memória contíguos.",
                exercises = listOf()
            ),
            CardModel(
                id = 2,
                name = "Array 2",
                theory = "Um array é uma coleção de itens armazenados em locais de memória contíguos.",
                exercises = listOf()
            ),
        )

        val pagingDataFlow = MutableStateFlow(PagingData.Companion.from(expectedCardModels))
        every { viewModel.cardListState } returns pagingDataFlow
        every { viewModel.searchQueryState } returns TextFieldState("")

        composeTestRule.setContent {
            DSLearnTheme {
                HomeScreen(viewModel, onCardClick = { cardId, cardName ->
                    assertEquals(expectedCardModels[0].id, cardId)
                    assertEquals(expectedCardModels[0].name, cardName)
                })
            }
        }

        mainDispatcherRule.scope.advanceUntilIdle()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Array 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Array 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Array 0").performClick()
    }
}