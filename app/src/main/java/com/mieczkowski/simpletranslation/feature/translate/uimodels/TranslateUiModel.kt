package com.mieczkowski.simpletranslation.feature.translate.uimodels

import com.mieczkowski.simpletranslation.network.models.Language

data class TranslateUiModel(
    val sourceLanguage: Language = autoDetect,
    val targetLanguage: Language,
    val languages: List<Language>,
    val text: String = "",
    val translatedText: String = ""
)

val autoDetect = Language(
    code = "auto_detect",

    //TODO: Move this text to string resources
    name = "Auto Detect"
)