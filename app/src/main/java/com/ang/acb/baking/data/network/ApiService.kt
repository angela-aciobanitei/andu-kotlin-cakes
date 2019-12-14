package com.ang.acb.baking.data.network

import androidx.lifecycle.LiveData
import retrofit2.http.GET

// Defines the REST API access points for Retrofit.
interface ApiService {
    @GET("baking.json")
    fun getAllRecipes(): LiveData<ApiResponse<List<NetworkRecipe>>>
}