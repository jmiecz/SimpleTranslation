package com.mieczkowski.simpletranslation.network.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Language(
    @JsonProperty("code")
    val code: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("targets")
    val supportedTranslationLanguageCodes: List<String> = listOf()
)