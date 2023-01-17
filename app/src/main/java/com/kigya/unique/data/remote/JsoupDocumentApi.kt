package com.kigya.unique.data.remote

import org.jsoup.nodes.Document
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface JsoupDocumentApi {

    @GET("/{course}-kurs/{group}-gruppa")
    suspend fun getRegularJsoupDoc(
        @Path("course") course: Int,
        @Path("group") group: Int
    ): Response<Document>

    @GET("/{course}-kurs/vf")
    suspend fun getMilitaryJsoupDoc(
        @Path("course") course: Int
    ): Response<Document>
}