package com.mieczkowski.simpletranslation.feature.translate.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mieczkowski.simpletranslation.R
import com.mieczkowski.simpletranslation.feature.translate.uimodels.TranslateUiModel
import com.mieczkowski.simpletranslation.feature.translate.uimodels.autoDetect
import com.mieczkowski.simpletranslation.network.models.Language
import com.mieczkowski.simpletranslation.ui.theme.SimpleTranslationTheme
import com.mieczkowski.simpletranslation.uikit.simpletranslationTypography

enum class LanguageSelect {
    Source, Target, Hide
}

/**
 * TODO:
 * Expand this section to include voice options
 */
@Composable
fun TranslateActionSection(
    modifier: Modifier = Modifier,
    translateUiModel: TranslateUiModel,
    onSourceLanguageChange: (Language) -> Unit,
    onTargetLanguageChange: (Language) -> Unit,
    onSwapLanguageClick: () -> Unit
) {
    val languageButtonWidth = 150.dp

    var languageSelect: LanguageSelect by remember {
        mutableStateOf(LanguageSelect.Hide)
    }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageButton(
                text = translateUiModel.sourceLanguage.name,
                onClick = { languageSelect = LanguageSelect.Source },
                modifier = modifier.width(languageButtonWidth)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Icon(
                painter = painterResource(
                    id = if (translateUiModel.sourceLanguage == autoDetect) R.drawable.baseline_arrow_forward_24 else R.drawable.baseline_swap_horiz_24
                ),
                contentDescription = stringResource(R.string.swap_languages),
                modifier = Modifier
                    .clickable { if (translateUiModel.sourceLanguage != autoDetect) onSwapLanguageClick() }
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            LanguageButton(
                text = translateUiModel.targetLanguage.name,
                onClick = { languageSelect = LanguageSelect.Target },
                modifier = modifier.width(languageButtonWidth)
            )
        }
    }

    val currentLanguage = when (languageSelect) {
        LanguageSelect.Source -> translateUiModel.sourceLanguage
        LanguageSelect.Target -> translateUiModel.targetLanguage
        LanguageSelect.Hide -> null
    }

    if (currentLanguage != null) LanguagePicker(
        currentLanguage = currentLanguage,
        languages = translateUiModel.languages.toMutableList().apply {
            if (languageSelect == LanguageSelect.Source) add(0, autoDetect)
        },
        onLanguagePick = {
            when (languageSelect) {
                LanguageSelect.Source -> onSourceLanguageChange(it)
                LanguageSelect.Target -> onTargetLanguageChange(it)
                LanguageSelect.Hide -> {}
            }

            languageSelect = LanguageSelect.Hide
        },
        onDismiss = { languageSelect = LanguageSelect.Hide }
    )

}

@Composable
private fun LanguageButton(
    modifier: Modifier = Modifier, text: String, onClick: () -> Unit
) {
    Button(modifier = modifier, onClick = onClick, content = {
        Text(
            text = text, style = simpletranslationTypography.bodyLarge
        )
    })
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
private fun SampleTranslateActionSection() {
    SimpleTranslationTheme {
        TranslateActionSection(
            translateUiModel = sampleTranslateUiModel,
            onSourceLanguageChange = {},
            onTargetLanguageChange = {},
            onSwapLanguageClick = {}
        )
    }
}