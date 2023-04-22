package com.mieczkowski.simpletranslation.repo

import com.mieczkowski.simpletranslation.network.models.AutoDetectLanguage
import com.mieczkowski.simpletranslation.network.models.Language
import com.mieczkowski.simpletranslation.network.models.TranslatedText
import com.mieczkowski.simpletranslation.network.service.TranslateService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslateRepo @Inject constructor(
    private val translateService: TranslateService
) {

    suspend fun detectLanguage(
        text: String
    ): List<AutoDetectLanguage> = translateService.detectLanguage(
        text = text
    )

    suspend fun fetchLanguages(): List<Language> = translateService.fetchLanguages()

    suspend fun translateText(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): TranslatedText = translateService.translateText(
        text = text,
        sourceLanguageCode = sourceLanguage.code,
        targetLanguageCode = targetLanguage.code
    )
}