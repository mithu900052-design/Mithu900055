package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.model.MonexaLanguage
import com.example.data.repository.MonexaDeviceRepository
import com.example.ui.screens.MonexaHomeScreen
import com.example.ui.theme.MonexaMonitorTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: MonexaDeviceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = MonexaDeviceRepository(applicationContext)

        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }
            var currentLanguage by remember { mutableStateOf(MonexaLanguage.ENGLISH) }

            MonexaMonitorTheme(darkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MonexaHomeScreen(
                        repository = repository,
                        currentLanguage = currentLanguage,
                        isDarkTheme = isDarkTheme,
                        onLanguageToggle = {
                            currentLanguage = if (currentLanguage == MonexaLanguage.ENGLISH) {
                                MonexaLanguage.BENGALI
                            } else {
                                MonexaLanguage.ENGLISH
                            }
                        },
                        onThemeToggle = {
                            isDarkTheme = !isDarkTheme
                        }
                    )
                }
            }
        }
    }
}
