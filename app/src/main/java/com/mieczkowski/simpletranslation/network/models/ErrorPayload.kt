package com.mieczkowski.simpletranslation.network.models

import com.fasterxml.jackson.annotation.JsonProperty

class ErrorPayload(
    @JsonProperty("error")
    val errorText: String
)