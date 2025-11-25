package com.unicamp.dslearn.data.repository


import androidx.paging.testing.asSnapshot
import com.unicamp.dslearn.core.model.TopicModel
import com.unicamp.dslearn.core.model.Difficult
import com.unicamp.dslearn.core.model.ExerciseModel
import com.unicamp.dslearn.data.datasource.remote.TopicsPagingSource.Companion.TOPICS_PAGE_SIZE
import com.unicamp.dslearn.data.datasource.remote.api.TopicsApi
import com.unicamp.dslearn.data.repository.topics.TopicsRepository
import com.unicamp.dslearn.data.repository.topics.TopicsRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.EventListener
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import java.net.HttpURLConnection
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SearchRepositoryTest {

    private val mockWebServer: MockWebServer by lazy { MockWebServer() }

    private lateinit var repository: TopicsRepository

    @Before
    fun setup() {
        mockWebServer.start()

        val json = Json {
            ignoreUnknownKeys = true
        }

        val searchApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(
                OkHttpClient.Builder()
                    .eventListenerFactory { EventListener.NONE }
                    .build())
            .build()
            .create(TopicsApi::class.java)

        repository = TopicsRepositoryImpl(searchApi)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `search by query should return correct results`() = runTest {
        val jsonSuccessResponse =
            File("src/test/res/raw/search_success.json").readText()
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(jsonSuccessResponse)

        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return response
            }
        }

        val expectedTopicModel =
            TopicModel(
                id = 1,
                name = "Array 0",
                theory = "Um array é uma coleção de itens armazenados em locais de memória contíguos.",
                exercises = listOf(
                    ExerciseModel(
                        id = 101,
                        title = "Two Sum",
                        difficult = Difficult.EASY
                    ),
                    ExerciseModel(
                        id = 102,
                        title = "Rotate Array",
                        difficult = Difficult.MEDIUM
                    ),
                )
            )

        val searchByQueryResultList = repository.getTopics("").asSnapshot {
            scrollTo(TOPICS_PAGE_SIZE * 2)
        }

        assertEquals(8, searchByQueryResultList.size)
        assertEquals(expectedTopicModel, searchByQueryResultList[0])
    }
}