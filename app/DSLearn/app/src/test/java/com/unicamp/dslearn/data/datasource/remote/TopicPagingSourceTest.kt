package com.unicamp.dslearn.data.datasource.remote

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.unicamp.dslearn.data.datasource.remote.api.TopicsApi
import com.unicamp.dslearn.data.datasource.remote.dto.TopicItemResponseDTO
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class TopicPagingSourceTest {

    private val mockWebServer: MockWebServer by lazy { MockWebServer() }

    private lateinit var testPager: TestPager<Int, TopicItemResponseDTO>

    @Before
    fun setup() {
        mockWebServer.start()

        val json = Json {
            ignoreUnknownKeys = true
        }

        val searchApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(OkHttpClient())
            .build()
            .create(TopicsApi::class.java)

        val pagingSource = TopicsPagingSource(
            searchApi, ""
        )

        testPager = TestPager(
            config = PagingConfig(
                pageSize = TopicsPagingSource.TOPICS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSource = pagingSource
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `pagingSource load returns page when first load is successful`() = runTest {
        val jsonSuccessResponse = File("src/test/res/raw/get_topic_success.json").readText()
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(jsonSuccessResponse)

        mockWebServer.enqueue(response)

        val expectedTopicResponseDTO = TopicItemResponseDTO(
            name = "Array",
            contentMarkdown = "# Arrays contentMarkdown",
        )

        val result = testPager.refresh() as PagingSource.LoadResult.Page

        Assert.assertEquals(10, result.data.size)
        Assert.assertEquals(expectedTopicResponseDTO, result.data[0])
    }

    @Test
    fun `pagingSource load more returns correct page when second load is successful`() = runTest {
        val jsonFirstPageSuccessResponse =
            File("src/test/res/raw/get_topic_success.json").readText()
        val firstPageResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(jsonFirstPageSuccessResponse)
        mockWebServer.enqueue(firstPageResponse)

        val expectedFirstPageSize = 10
        val firstPageLoadResult = testPager.refresh() as PagingSource.LoadResult.Page

        Assert.assertEquals(expectedFirstPageSize, firstPageLoadResult.data.size)

        val jsonSecondPageSuccessResponse =
            File("src/test/res/raw/get_topic_success_2.json").readText()
        val secondPageResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(jsonSecondPageSuccessResponse)
        mockWebServer.enqueue(secondPageResponse)

        val expectedTopicResponseDTO = TopicItemResponseDTO(
            name = "Dynamic Programming",
            contentMarkdown = "# Programação Dinâmica contentMarkdown",
        )

        val expectedSecondPageSize = 10
        val secondPageLoadResult = testPager.append() as PagingSource.LoadResult.Page

        Assert.assertEquals(expectedSecondPageSize, secondPageLoadResult.data.size)
        Assert.assertEquals(expectedTopicResponseDTO, secondPageLoadResult.data[0])
    }

    @Test
    fun `pagingSource load returns exception when first load fails`() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)

        mockWebServer.enqueue(response)

        val result = testPager.refresh() as PagingSource.LoadResult.Error
        Assert.assertEquals(Exception::class.java, result.throwable.javaClass)
        Assert.assertThrows(Exception::class.java) {
            throw result.throwable
        }
    }
}