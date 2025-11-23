package com.unicamp.dslearn.domain

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.unicamp.dslearn.core.model.TopicModel
import com.unicamp.dslearn.data.repository.topics.TopicsRepository
import com.unicamp.dslearn.domain.topics.GetTopicsUseCase
import com.unicamp.dslearn.domain.topics.GetTopicsUseCaseImpl
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

    private val topicsRepository: TopicsRepository = mockk()

    private lateinit var getTopicsUseCase: GetTopicsUseCase

    @Before
    fun setup() {
        getTopicsUseCase = GetTopicsUseCaseImpl(topicsRepository)
    }

    @Test
    fun `search by query should return correct results`() = runTest {
        val expectedTopicModels = listOf(
            TopicModel(
                id = 1,
                name = "Array 0",
                theory = "Um array é uma coleção de itens armazenados em locais de memória contíguos.",
                exercises = listOf()
            ),
            TopicModel(
                id = 2,
                name = "Array 2",
                theory = "Um array é uma coleção de itens armazenados em locais de memória contíguos.",
                exercises = listOf()
            ),
        )

        val pagingData: PagingData<TopicModel> = PagingData.from(expectedTopicModels)
        val pagingDataFlow = flow {
            emit(pagingData)
        }

        every { topicsRepository.getTopics(any()) } returns pagingDataFlow

        val searchCardUseCaseResultList = getTopicsUseCase("").asSnapshot {}

        assertEquals(2, searchCardUseCaseResultList.size)
        assertEquals(expectedTopicModels[1], searchCardUseCaseResultList[1])
    }
}