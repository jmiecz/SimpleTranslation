package com.mieczkowski.simpletranslation.network.models

import com.fasterxml.jackson.annotation.JsonProperty

data class TranslatedText(
    @JsonProperty("translatedText")
    val text: String
)