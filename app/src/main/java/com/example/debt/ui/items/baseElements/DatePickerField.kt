package com.example.debt.ui.items.baseElements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.example.debt.ui.theme.AppColors
import com.example.debt.utils.getCurrentDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerField(
    label: String,
    date: String,
    color: Color? = null,
    onDateChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        DebtOutlinedTextField(
            color = color,
            value = date,
            onValueChange = {},
            readOnly = true,
            label = label,
            trailingIcon = {
                Icon(
                    Icons.Default.DateRange, contentDescription = "Выбрать дату",
                    tint = color ?: AppColors.accentPrimary
                )
            },
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable { showDatePicker = true }
        )
    }

    if (showDatePicker) {
        ReturnDatePickerDialog(
            initialDate = date.ifEmpty { getCurrentDateTime() },
            onDateSelected = { newDate ->
                onDateChanged(newDate)
            },
            onDismiss = { showDatePicker = false },
            color = color
        )
    }
}