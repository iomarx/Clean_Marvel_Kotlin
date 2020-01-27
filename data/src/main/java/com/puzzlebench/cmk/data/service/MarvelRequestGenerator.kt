package com.puzzlebench.cmk.data.service

import com.puzzlebench.cmk.data.BuildConfig
import com.puzzlebench.cmk.data.service.MarvelRequestGenerator.Companion.MD5_ALGORITHM
import com.puzzlebench.cmk.data.service.MarvelRequestGenerator.Companion.PAD_CHAR
import com.puzzlebench.cmk.data.service.MarvelRequestGenerator.Companion.RADIX_VALUE
import com.puzzlebench.cmk.data.service.MarvelRequestGenerator.Companion.STRING_LENGTH
import com.puzzlebench.cmk.data.service.api.MarvelApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest


class MarvelRequestGenerator {

    private val HASH_ARG = "hash"
    private val PUBLIC_API_KEY_ARG = "apikey"
    private val TS = "ts"
    private val TS_VALUE = "1"
    private val HASH_VALUE: String = (TS_VALUE + BuildConfig.PRIVATE_API_KEY_VALUE + BuildConfig.PUBLIC_API_KEY_VALUE).md5()
    private val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val defaultRequest = chain.request()

        val defaultHttpUrl = defaultRequest.url()
        val httpUrl = defaultHttpUrl.newBuilder()
                .addQueryParameter(PUBLIC_API_KEY_ARG, BuildConfig.PUBLIC_API_KEY_VALUE)
                .addQueryParameter(HASH_ARG, HASH_VALUE)
                .addQueryParameter(TS, TS_VALUE)
                .build()

        val requestBuilder = defaultRequest.newBuilder().url(httpUrl)

        chain.proceed(requestBuilder.build())
    }

    private val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.MARVEL_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())


    private fun makeMarvelService(okHttpClient: OkHttpClient): MarvelApi {
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.MARVEL_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(MarvelApi::class.java)
    }

    fun makeMarvelService(): MarvelApi {
        val okHttpClient = httpClient.build()
        return makeMarvelService(okHttpClient)
    }

    companion object {
        const val MD5_ALGORITHM = "MD5"
        const val RADIX_VALUE = 16
        const val STRING_LENGTH = 16
        const val PAD_CHAR = '0'
    }
}

private fun String.md5(): String {
    val md = MessageDigest.getInstance(MD5_ALGORITHM)
    return BigInteger(1, md.digest(toByteArray())).toString(RADIX_VALUE).padStart(STRING_LENGTH, PAD_CHAR)
}
