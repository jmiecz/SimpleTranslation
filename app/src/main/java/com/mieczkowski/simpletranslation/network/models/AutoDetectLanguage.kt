package com.mieczkowski.simpletranslation.network.models

import com.fasterxml.jackson.annotation.JsonProperty

data class AutoDetectLanguage(
    @JsonProperty("confidence")
    val confidence: Double,

    @JsonProperty("language")
    val code: String
)