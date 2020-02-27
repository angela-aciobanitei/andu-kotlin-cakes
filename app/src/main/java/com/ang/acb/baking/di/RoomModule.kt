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
 * A Dagger module that provides the Room database for this app, and the [MovieDao].
 */
@Module(includes = [ViewModelModule::class])
class RoomModule {
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