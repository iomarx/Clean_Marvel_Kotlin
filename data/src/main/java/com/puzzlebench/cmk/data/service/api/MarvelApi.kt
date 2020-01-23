package com.puzzlebench.cmk.data.service.api

import com.puzzlebench.cmk.data.service.response.CharacterResponse
import com.puzzlebench.cmk.data.service.response.DataBaseResponse
import com.puzzlebench.cmk.data.service.response.MarvelBaseResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MarvelApi {
    @GET("/v1/public/characters")
    fun getCharacter(): Single<MarvelBaseResponse<DataBaseResponse<CharacterResponse>>>

    @GET("/v1/public/characters/{characterId}")
    fun getCharacterDetail(@Path("characterId") characterId: Int): Single<MarvelBaseResponse<DataBaseResponse<CharacterResponse>>>
}