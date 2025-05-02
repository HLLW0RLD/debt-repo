package com.example.debt.ui.items.baseElements

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.example.debt.utils.AppColors

@Composable
fun DebtOutlinedTextField(
    value: String,
    label: String,
    color: Color? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int. MAX_VALUE,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            focusedLabelColor = color ?: AppColors.textPrimary,
            unfocusedLabelColor = color ?: AppColors.textPrimary,
            focusedBorderColor = color ?: AppColors.textPrimary,
            unfocusedBorderColor = color ?: AppColors.textPrimary,
        ),
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.then(modifier).fillMaxWidth(),

        enabled = enabled,
        readOnly = readOnly,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
    )
}