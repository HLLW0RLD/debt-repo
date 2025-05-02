package com.example.debt.ui.items.baseElements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.magnifier
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.debt.utils.AppColors

@Composable
fun DebtTextButton(
    text: String,
    color: Color? = null,
    enabled: Boolean = true,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = color ?: AppColors.accentPrimary,
            disabledContainerColor = color ?: AppColors.accentPrimary,
        ),
        onClick = {
            onClick()
        },
        enabled = enabled,

        border = border,
        contentPadding = contentPadding,
        modifier = Modifier.then(modifier)
    ) {
        Text(
            text = text,
            color = AppColors.background
        )
    }
}