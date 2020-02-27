package com.ang.acb.baking.data.repository


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ang.acb.baking.data.database.RecipeDao
import com.ang.acb.baking.data.database.RecipeDetails
import com.ang.acb.baking.data.database.RecipesDatabase
import com.ang.acb.baking.data.network.*
import com.ang.acb.baking.util.*

import org.junit.Test
import org.junit.Before
import org.junit.Rule

import org.mockito.ArgumentMatchers
import org.mockito.Mockito

/**
 * Tests the [RecipeRepository].
 */
class RecipeRepositoryTest {

    // Subject under test
    private lateinit var repository: RecipeRepository

    private val dao = Mockito.mock(RecipeDao::class.java)
    private val service = Mockito.mock(ApiService::class.java)

    // Executes each task synchronously using Architecture Components.
    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db = Mockito.mock(RecipesDatabase::class.java)
        Mockito.`when`(db.recipeDao).thenReturn(dao)
        Mockito.`when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = RecipeRepository(
            appExecutors = InstantAppExecutors(),
            database = db,
            recipeDao = dao,
            apiService = service)
    }


    @Test
    fun loadMovieDetailsFromNetwork() {
        val dbData = MutableLiveData<List<RecipeDetails>>()
        Mockito.`when`(dao.getDetailedRecipes()).thenReturn(dbData)

        val networkRecipes: List<NetworkRecipe> = PojosTestUtil.createNetworkRecipes(5)
        val callLiveData = ApiUtil.successCall(networkRecipes)
        Mockito.`when`(service.getAllRecipes()).thenReturn(callLiveData)

        val liveRecipes: LiveData<Resource<List<RecipeDetails>>> = repository.loadAllRecipes()
        Mockito.verify(dao).getDetailedRecipes()
        Mockito.verifyNoMoreInteractions(service)

        val observer: Observer<Resource<List<RecipeDetails>>> = mock()
        liveRecipes.observeForever(observer)
        Mockito.verifyNoMoreInteractions(service)
        Mockito.verify(observer).onChanged(Resource.loading(null))

        val updatedDbData = MutableLiveData<List<RecipeDetails>>()
        Mockito.`when`(dao.getDetailedRecipes()).thenReturn(updatedDbData)

        dbData.value = emptyList() // should fetch fresh data from network
        Mockito.verify(service).getAllRecipes()

        val recipes = networkRecipes.map {
            it.asRecipeDetails()
        }
        updatedDbData.value = recipes
        Mockito.verify(observer).onChanged(Resource.success(recipes))
    }
}