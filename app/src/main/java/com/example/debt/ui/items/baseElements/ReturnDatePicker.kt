package com.example.debt.ui.items.baseElements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.debt.utils.AppColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnDatePickerDialog(
    initialDate: String? = null,
    color: Color? = null,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val initialMillis = remember(initialDate) {
        initialDate?.let { dateString ->
            try {
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .parse(dateString)?.time
            } catch (e: Exception) {
                null
            }
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DebtTextButton(color = color, text = "OK") {
                datePickerState.selectedDateMillis?.let { millis ->
                    val selectedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        .format(Date(millis))
                    onDateSelected(selectedDate)
                }
                onDismiss()
            }
        },
        dismissButton = {
            DebtTextButton(text = "Отмена", color = color) { onDismiss }
        },
        colors = DatePickerDefaults.colors(
            containerColor = color ?: AppColors.accentPrimary,
        ),
    ) {
        DatePicker(state = datePickerState)
    }
}