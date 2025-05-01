package com.example.debt.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.debt.utils.PreferenceCache
import com.example.debt.utils.ThemeMode

@Composable
fun ThemeSelectionDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismissRequest)
                .background(
                    Color.Black.copy(alpha = 0.2f)
                )
        ) {

            Surface(
                modifier = modifier
                    .padding(top = 56.dp, end = 16.dp)
                    .width(200.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Выбор темы",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )

                    Divider()

                    ThemeOption(
                        text = "Системная",
                        selected = PreferenceCache.selectedTheme == ThemeMode.SYSTEM,
                        onClick = {
                            PreferenceCache.selectedTheme = ThemeMode.SYSTEM
                            onDismissRequest()
                        }
                    )

                    ThemeOption(
                        text = "Светлая",
                        selected = PreferenceCache.selectedTheme == ThemeMode.LIGHT,
                        onClick = {
                            PreferenceCache.selectedTheme = ThemeMode.LIGHT
                            onDismissRequest()
                        }
                    )

                    ThemeOption(
                        text = "Темная",
                        selected = PreferenceCache.selectedTheme == ThemeMode.DARK,
                        onClick = {
                            PreferenceCache.selectedTheme = ThemeMode.DARK
                            onDismissRequest()
                        }
                    )

                    ThemeOption(
                        text = "Цветная",
                        selected = PreferenceCache.selectedTheme == ThemeMode.COLOR,
                        onClick = {
                            PreferenceCache.selectedTheme = ThemeMode.COLOR
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null // Клик обрабатывается на всей строке
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}