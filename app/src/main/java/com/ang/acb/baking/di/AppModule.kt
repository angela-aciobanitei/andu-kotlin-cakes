package com.ang.acb.baking.di

import android.app.Application
import androidx.room.Room
import com.ang.acb.baking.BuildConfig
import com.ang.acb.baking.data.database.RecipeDao
import com.ang.acb.baking.data.database.RecipesDatabase
import com.ang.acb.baking.data.network.ApiService
import com.ang.acb.baking.data.network.LiveDataCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * See: https://github.com/codepath/android_guides/wiki/Dependency-Injection-with-Dagger-2
 */
@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor() : HttpLoggingInterceptor {
        // Retrofit completely relies on OkHttp for any network operation.
        // Since logging isn’t integrated by default anymore in Retrofit 2,
        // we'll use a logging interceptor for OkHttp.
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        // Build the OkHttpClient with the logging interceptor.
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Singleton
    @Provides
    fun provideMoshi() : Moshi {
        // Build the Moshi object that Retrofit will be using, making sure
        // to add the Kotlin adapter for full Kotlin compatibility.
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient : OkHttpClient) : ApiService {
        // Build the Retrofit object using a Moshi converter for parsing JSON, and
        // a [LiveDataCallAdapter] that converts the Retrofit Call into a LiveData of ApiResponse.
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideDatabase(app: Application): RecipesDatabase {
        return Room
            .databaseBuilder(app, RecipesDatabase::class.java, "baking")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRecipeDao(database: RecipesDatabase): RecipeDao {
        return database.recipeDao
    }
}