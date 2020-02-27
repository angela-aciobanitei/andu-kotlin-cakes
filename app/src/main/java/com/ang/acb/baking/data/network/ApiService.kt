package com.ang.acb.baking.data.network

import androidx.lifecycle.LiveData
import retrofit2.http.GET

/**
 * Defines the REST API access points for Retrofit.
 */
interface ApiService {

    /**
     * Get a list of recipes from the network using this base URL
     * "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/".
     */
    @GET("baking.json")
    fun getAllRecipes(): LiveData<ApiResponse<List<NetworkRecipe>>>
}