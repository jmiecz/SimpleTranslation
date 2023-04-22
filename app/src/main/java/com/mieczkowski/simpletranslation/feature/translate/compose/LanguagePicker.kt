package com.mieczkowski.simpletranslation.feature.translate.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mieczkowski.simpletranslation.R
import com.mieczkowski.simpletranslation.network.models.Language
import com.mieczkowski.simpletranslation.ui.theme.SimpleTranslationTheme
import com.mieczkowski.simpletranslation.uikit.simpletranslationTypography

@Composable
fun LanguagePicker(
    currentLanguage: Language,
    languages: List<Language>,
    onLanguagePick: (Language) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_a_language),
                    style = simpletranslationTypography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Column(
                    modifier = Modifier
                        .height(400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    languages.forEachIndexed { index, language ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable { onLanguagePick(language) },
                        ) {
                            Text(
                                text = language.name,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )

                            if (language == currentLanguage) Icon(
                                painter = painterResource(id = R.drawable.baseline_check_24),
                                contentDescription = stringResource(R.string.language_selected),
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                        if (index != languages.lastIndex) Divider()
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}

@Preview
@Composable
private fun SampleLanguagePicker() {
    SimpleTranslationTheme {
        LanguagePicker(
            currentLanguage = Language(code = "en", name = "English"),
            languages = listOf(
                Language(code = "en", name = "English"),
                Language(code = "es", name = "Spanish")
            ),
            onLanguagePick = {},
            onDismiss = {}
        )
    }
}