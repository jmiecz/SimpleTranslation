package com.mieczkowski.simpletranslation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mieczkowski.simpletranslation.feature.translate.compose.NavGraphs
import com.mieczkowski.simpletranslation.ui.theme.SimpleTranslationTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleTranslationTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}