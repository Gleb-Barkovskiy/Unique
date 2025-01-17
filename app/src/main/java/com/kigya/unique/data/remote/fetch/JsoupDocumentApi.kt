package com.kigya.unique.data.remote.fetch

import org.jsoup.nodes.Document
import retrofit2.http.GET
import retrofit2.http.Path

interface JsoupDocumentApi {

    @GET("/{course}-kurs/{group}-gruppa/")
    suspend fun getRegularJsoupDoc(
        @Path("course") course: Int,
        @Path("group") group: Int,
    ): Document

    @GET("/{course}-kurs/vf/")
    suspend fun getMilitaryJsoupDoc(
        @Path("course") course: Int,
    ): Document
}
