package com.example.seminarku.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SeminarColorScheme = lightColorScheme(
    primary = SeminarBlue,
    secondary = SeminarNavy,
    tertiary = SeminarAmber,
    background = SeminarBackground,
    surface = androidx.compose.ui.graphics.Color.White,
    error = SeminarRed,
    onBackground = SeminarInk,
    onSurface = SeminarInk
)

@Composable
fun SeminarKuTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = SeminarColorScheme, typography = Typography, content = content)
}
