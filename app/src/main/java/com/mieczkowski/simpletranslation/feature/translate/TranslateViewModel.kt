package com.mieczkowski.simpletranslation.feature.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mieczkowski.simpletranslation.feature.translate.uimodels.TranslateUiModel
import com.mieczkowski.simpletranslation.feature.translate.uimodels.autoDetect
import com.mieczkowski.simpletranslation.network.models.Language
import com.mieczkowski.simpletranslation.repo.TranslateRepo
import com.mieczkowski.simpletranslation.uikit.UiState
import com.mieczkowski.simpletranslation.uikit.toDataLoaded
import com.mieczkowski.simpletranslation.uikit.toError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val translateRepo: TranslateRepo
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<TranslateUiModel>> =
        MutableStateFlow(UiState.Loading())
    val uiState = _uiState.asStateFlow()

    init {
        fetchLanguages()
    }

    private fun fetchLanguages() {
        viewModelScope.launch {
            runCatching { translateRepo.fetchLanguages() }
                .onSuccess {
                    val translateUiModel = (_uiState.value as? UiState.DataLoaded)?.data?.copy(
                        languages = it
                    ) ?: TranslateUiModel(
                        languages = it,
                        targetLanguage = it.first()
                    )

                    _uiState.value = translateUiModel.toDataLoaded()
                }
                .onFailure { _uiState.value = it.toError() }
        }
    }

    fun onTextChange(text: String) {
        (_uiState.value as? UiState.DataLoaded)?.data?.let {
            _uiState.value = it.copy(
                text = text,
            ).toDataLoaded()
        }
    }

    fun onSearch() {
        val translateUiModel = (_uiState.value as? UiState.DataLoaded)?.data ?: return

        //TODO: add auto detect support
        if (translateUiModel.sourceLanguage == autoDetect) return

        viewModelScope.launch {
            runCatching {
                translateRepo.translateText(
                    text = translateUiModel.text,
                    sourceLanguage = translateUiModel.sourceLanguage,
                    targetLanguage = translateUiModel.targetLanguage
                )
            }
                .onSuccess {
                    _uiState.value = translateUiModel.copy(
                        translatedText = it.text
                    ).toDataLoaded()
                }
                .onFailure { _uiState.value = it.toError() }
        }
    }

    fun onSourceLanguageChange(language: Language) {
        (_uiState.value as? UiState.DataLoaded)?.data?.let {
            _uiState.value = it.copy(
                sourceLanguage = language,
            ).toDataLoaded()
        }
    }

    fun onTargetLanguageChange(language: Language) {
        (_uiState.value as? UiState.DataLoaded)?.data?.let {
            _uiState.value = it.copy(
                targetLanguage = language
            ).toDataLoaded()
        }
    }

    fun onSwapLanguageClick() {
        (_uiState.value as? UiState.DataLoaded)?.data?.let {
            _uiState.value = it.copy(
                sourceLanguage = it.targetLanguage,
                targetLanguage = it.sourceLanguage
            ).toDataLoaded()
        }
    }
}