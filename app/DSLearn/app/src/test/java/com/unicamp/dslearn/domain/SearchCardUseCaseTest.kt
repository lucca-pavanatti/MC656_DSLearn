package com.unicamp.dslearn.domain

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.unicamp.dslearn.core.model.CardModel
import com.unicamp.dslearn.data.repository.SearchRepository
import com.unicamp.dslearn.domain.cardsearch.SearchCardUseCase
import com.unicamp.dslearn.domain.cardsearch.SearchCardUseCaseImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class SearchCardUseCaseTest {

    private val searchRepository: SearchRepository = mockk()

    private lateinit var searchCardUseCase: SearchCardUseCase

    @Before
    fun setup() {
        searchCardUseCase = SearchCardUseCaseImpl(searchRepository)
    }

    @Test
    fun `search by query should return correct results`() = runTest {
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

        val pagingData: PagingData<CardModel> = PagingData.from(expectedCardModels)
        val pagingDataFlow = flow {
            emit(pagingData)
        }

        every { searchRepository.searchByQuery(any()) } returns pagingDataFlow

        val searchCardUseCaseResultList = searchCardUseCase("").asSnapshot {}

        assertEquals(2, searchCardUseCaseResultList.size)
        assertEquals(expectedCardModels[1], searchCardUseCaseResultList[1])
    }
}