package com.kigya.unique.di

import com.kigya.unique.data.remote.JsoupConverterFactory
import com.kigya.unique.data.remote.JsoupDocumentApi
import com.kigya.unique.utils.constants.NetworkConst
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(JsoupConverterFactory)
        .client(JsoupConverterFactory.httpClient)
        .baseUrl(NetworkConst.BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideJsoupDocumentApi(retrofit: Retrofit): JsoupDocumentApi =
        retrofit.create(JsoupDocumentApi::class.java)

}