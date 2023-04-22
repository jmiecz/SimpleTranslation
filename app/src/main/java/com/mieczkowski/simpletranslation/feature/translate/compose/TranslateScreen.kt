package com.mieczkowski.simpletranslation.feature.translate.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mieczkowski.simpletranslation.R
import com.mieczkowski.simpletranslation.feature.translate.TranslateViewModel
import com.mieczkowski.simpletranslation.feature.translate.uimodels.TranslateUiModel
import com.mieczkowski.simpletranslation.feature.translate.uimodels.autoDetect
import com.mieczkowski.simpletranslation.network.models.Language
import com.mieczkowski.simpletranslation.ui.theme.SimpleTranslationTheme
import com.mieczkowski.simpletranslation.uikit.UiState
import com.mieczkowski.simpletranslation.uikit.simpletranslationTypography
import com.mieczkowski.simpletranslation.uikit.toDataLoaded
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun TranslateScreen(
    navigator: DestinationsNavigator,
    translateViewModel: TranslateViewModel = hiltViewModel()
) {
    TranslateScreen(
        uiState = translateViewModel.uiState.collectAsState().value,
        onTextChange = { translateViewModel.onTextChange(it) },
        onSearch = { translateViewModel.onSearch() },
        onSourceLanguageChange = { translateViewModel.onSourceLanguageChange(it) },
        onTargetLanguageChange = { translateViewModel.onTargetLanguageChange(it) },
        onSwapLanguageClick = { translateViewModel.onSwapLanguageClick() }
    )
}

@Composable
fun TranslateScreen(
    uiState: UiState<TranslateUiModel>,
    onTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSourceLanguageChange: (Language) -> Unit,
    onTargetLanguageChange: (Language) -> Unit,
    onSwapLanguageClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is UiState.DataLoaded -> LoadedState(
                translateUiModel = uiState.data,
                onTextChange = onTextChange,
                onSearch = onSearch,
                onSourceLanguageChange = onSourceLanguageChange,
                onTargetLanguageChange = onTargetLanguageChange,
                onSwapLanguageClick = onSwapLanguageClick
            )

            is UiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            else -> Text(text = "Unknown Error")
        }
    }
}

@Composable
private fun LoadedState(
    translateUiModel: TranslateUiModel,
    onTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSourceLanguageChange: (Language) -> Unit,
    onTargetLanguageChange: (Language) -> Unit,
    onSwapLanguageClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.6F)
        ) {
            val showTranslated = translateUiModel.translatedText.isNotBlank()
            val weight = if (showTranslated) .5F else 1F

            TextBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight),
                language = translateUiModel.sourceLanguage,
                text = translateUiModel.text,
                onTextChange = onTextChange,
                onSearch = onSearch,
                showLabel = showTranslated
            )

            if (showTranslated) {
                Spacer(modifier = Modifier.size(16.dp))
                TextBlock(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight),
                    language = translateUiModel.targetLanguage,
                    text = translateUiModel.translatedText,
                    onTextChange = null,
                    onSearch = onSearch,
                    showLabel = true
                )
            }
        }

        TranslateActionSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.4F),
            translateUiModel = translateUiModel,
            onSourceLanguageChange = onSourceLanguageChange,
            onTargetLanguageChange = onTargetLanguageChange,
            onSwapLanguageClick = onSwapLanguageClick
        )
    }
}

@Composable
private fun TextBlock(
    modifier: Modifier = Modifier,
    language: Language,
    text: String,
    showLabel: Boolean,
    onTextChange: ((String) -> Unit)? = null,
    onSearch: () -> Unit
) {
    val textStyle = simpletranslationTypography.headlineMedium
    val requester = FocusRequester()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
    ) {
        if (showLabel) Text(
            text = language.name,
            style = simpletranslationTypography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        TextField(
            value = text,
            readOnly = onTextChange == null,
            onValueChange = { onTextChange?.invoke(it) },
            placeholder = {
                Text(
                    text = stringResource(R.string.enter_text), style = textStyle
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(requester),
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onSearch()
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

private val sampleTranslateUiModel: TranslateUiModel by lazy {
    TranslateUiModel(
        sourceLanguage = autoDetect, targetLanguage = Language(
            code = "en", name = "English"
        ), languages = listOf(
            Language(code = "en", name = "English"),
            Language(code = "es", name = "Spanish")
        )
    )
}

@Preview
@Composable
fun SampleTranslateScreen() {
    var translateUiModel: TranslateUiModel by remember {
        mutableStateOf(sampleTranslateUiModel)
    }

    SimpleTranslationTheme {
        TranslateScreen(
            uiState = translateUiModel.toDataLoaded(),
            onTextChange = {
                translateUiModel = translateUiModel.copy(
                    text = it
                )
            },
            onSearch = {},
            onSourceLanguageChange = {},
            onTargetLanguageChange = { },
            onSwapLanguageClick = {
                translateUiModel = translateUiModel.copy(
                    sourceLanguage = translateUiModel.targetLanguage,
                    targetLanguage = translateUiModel.sourceLanguage
                )
            }
        )
    }
}

@Preview
@Composable
fun SampleTranslateScreenWithTranslation() {
    SimpleTranslationTheme {
        TranslateScreen(
            uiState = sampleTranslateUiModel.copy(
                sourceLanguage = Language(code = "en", name = "English"),
                targetLanguage = Language(code = "es", name = "Spanish"),
                text = "Hello",
                translatedText = "Hola"
            ).toDataLoaded(),
            onTextChange = {},
            onSearch = {},
            onSourceLanguageChange = {},
            onTargetLanguageChange = { },
            onSwapLanguageClick = {}
        )
    }
}