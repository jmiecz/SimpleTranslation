package com.mieczkowski.simpletranslation.network.service

import com.mieczkowski.simpletranslation.BuildConfig
import com.mieczkowski.simpletranslation.network.models.AutoDetectLanguage
import com.mieczkowski.simpletranslation.network.models.Language
import com.mieczkowski.simpletranslation.network.models.TranslatedText
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * TODO:
 * Make the [api_key] apply to all the api calls vs manually passing it in for each function
 */
interface TranslateService {

    @FormUrlEncoded
    @POST("detect")
    suspend fun detectLanguage(
        @Field("q") text: String,
        @Field("api_key") apiKey: String = BuildConfig.API_KEY
    ): List<AutoDetectLanguage>

    @GET("languages")
    suspend fun fetchLanguages() : List<Language>

    @FormUrlEncoded
    @POST("translate")
    suspend fun translateText(
        @Field("q") text: String,
        @Field("source") sourceLanguageCode: String,
        @Field("target") targetLanguageCode: String,
        @Field("format") format: String = "text",
        @Field("api_key") apiKey: String = BuildConfig.API_KEY
    ): TranslatedText
}