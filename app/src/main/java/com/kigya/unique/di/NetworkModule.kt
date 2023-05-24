package com.kigya.unique.di

import com.kigya.unique.data.remote.fetch.LessonProvider
import com.kigya.unique.di.NetworkModule.Const.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .build())
        .build()

    @Provides
    @Singleton
    fun provideLessonProvider(retrofit: Retrofit): LessonProvider =
        retrofit.create(LessonProvider::class.java)

    object Const {
        const val BASE_URL = "https://bsu-schedule-server.onrender.com"
    }
}
