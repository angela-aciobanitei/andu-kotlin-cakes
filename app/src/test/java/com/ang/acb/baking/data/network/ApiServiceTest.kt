package com.ang.acb.baking.data.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ang.acb.baking.data.database.Recipe
import com.ang.acb.baking.data.database.RecipeDetails
import com.ang.acb.baking.util.LiveDataTestUtil.getValue
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Test for the [ApiService] class.
 */
@RunWith(JUnit4::class)
class ApiServiceTest {

    // Subject under test
    private lateinit var apiService: ApiService

    // A scriptable web server for testing HTTP clients. Callers supply canned
    // responses and the server replays them upon request in sequence.
    private lateinit var mockWebServer: MockWebServer

    // Executes each task synchronously using Architecture Components.
    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createService() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        mockWebServer = MockWebServer()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getRecipeList() {
        enqueueResponse("baking.json")
        val response =
            (getValue(apiService.getAllRecipes()) as ApiSuccessResponse).body

        // Verify endpoint
        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("/baking.json"))

        val recipes: List<RecipeDetails> = response.map {
            it.asRecipeDetails()
        }
        assertThat(recipes, notNullValue())
        assertThat(recipes.size, `is`(4))

        val firstRecipe = recipes[0]
        assertThat(firstRecipe.recipe?.id, `is`(1))
        assertThat(firstRecipe.recipe?.name, `is`("Nutella Pie"))
        assertThat(firstRecipe.recipe?.servings, `is`(8))
        assertThat(firstRecipe.recipe?.image, `is`(""))

        val secondRecipe = recipes[1]
        assertThat(secondRecipe.recipe?.id, `is`(2))
        assertThat(secondRecipe.recipe?.name, `is`("Brownies"))
        assertThat(secondRecipe.recipe?.servings, `is`(8))
        assertThat(secondRecipe.recipe?.image, `is`(""))

        val thirdRecipe = recipes[2]
        assertThat(thirdRecipe.recipe?.id, `is`(3))
        assertThat(thirdRecipe.recipe?.name, `is`("Yellow Cake"))
        assertThat(thirdRecipe.recipe?.servings, `is`(8))
        assertThat(thirdRecipe.recipe?.image, `is`(""))

        val fourthRecipe = recipes[3]
        assertThat(fourthRecipe.recipe?.id, `is`(4))
        assertThat(fourthRecipe.recipe?.name, `is`("Cheesecake"))
        assertThat(fourthRecipe.recipe?.servings, `is`(8))
        assertThat(fourthRecipe.recipe?.image, `is`(""))
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")
        val source = inputStream?.source()?.buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        if (source != null) {
            mockWebServer.enqueue(
                mockResponse.setBody(source.readString(Charsets.UTF_8))
            )
        }
    }
}