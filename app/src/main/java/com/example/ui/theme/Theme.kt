package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBg,
    surface = DarkSurface,
    onPrimary = Color(0xFF0F1A12),
    onSecondary = Color(0xFF1E1700),
    onTertiary = Color(0xFF0F1A12),
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ForestGreenMedium,
    secondary = BrightGold,
    tertiary = ForestGreenLight,
    background = WarmCream,
    surface = androidx.compose.ui.graphics.Color.White,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = Color(0xFF1E1700),
    onTertiary = androidx.compose.ui.graphics.Color.White,
    onBackground = ForestGreenDeep,
    onSurface = ForestGreenDeep,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disable dynamic color to enforce our corporate premium brand scheme
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
