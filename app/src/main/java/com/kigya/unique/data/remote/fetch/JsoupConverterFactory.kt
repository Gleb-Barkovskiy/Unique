package com.kigya.unique.data.remote.fetch

import com.kigya.unique.data.remote.fetch.JsoupConverterFactory.Const.CHARSET_NAME
import com.kigya.unique.data.remote.fetch.JsoupConverterFactory.Const.TIMEOUT
import com.kigya.unique.data.remote.fetch.JsoupConverterFactory.Const.XML_APPLICATION_TYPE
import com.kigya.unique.data.remote.fetch.JsoupConverterFactory.Const.XML_TEXT_TYPE
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

object JsoupConverterFactory : Converter.Factory() {

    val httpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *>? = when (type) {
        Document::class.java -> JsoupConverter(retrofit.baseUrl().toString())
        else -> null
    }

    private class JsoupConverter(val baseUri: String) : Converter<ResponseBody, Document?> {

        override fun convert(value: ResponseBody): Document? {
            val charset = value.contentType()?.charset() ?: Charset.forName(CHARSET_NAME)
            val parser = when (value.contentType().toString()) {
                XML_APPLICATION_TYPE, XML_TEXT_TYPE -> Parser.xmlParser()
                else -> Parser.htmlParser()
            }
            return Jsoup.parse(value.byteStream(), charset.name(), baseUri, parser)
        }
    }

    object Const {
        const val TIMEOUT = 30L
        const val CHARSET_NAME = "UTF-8"
        const val XML_APPLICATION_TYPE = "application/xml"
        const val XML_TEXT_TYPE = "text/xml"
    }
}
