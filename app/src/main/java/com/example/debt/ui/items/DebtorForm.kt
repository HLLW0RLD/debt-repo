package com.example.debt.app.ui.items

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.debt.data.model.Debt
import com.example.debt.ui.items.baseElements.DatePickerField
import com.example.debt.ui.items.baseElements.DebtOutlinedTextField
import com.example.debt.ui.items.baseElements.DebtRadioButton
import com.example.debt.ui.items.baseElements.DebtTextButton
import com.example.debt.ui.theme.AppColors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DebtorForm(
    debt: Debt? = null,
    isMine: Boolean = false,
    color: Color? = null,
    onSaveComplete: (Debt) -> Unit
) {

    val isEdit = debt != null

    val name = remember { mutableStateOf(debt?.name ?: "") }
    val telegramNick = remember { mutableStateOf(debt?.telegramNick ?: "") }
    val debtAmount = remember { mutableStateOf(debt?.debtAmount?.toString() ?: "") }
    val returnDate = remember { mutableStateOf(debt?.returnDate ?: "") }
    val comment = remember { mutableStateOf(debt?.comment ?: "") }
    val isMineState = remember { mutableStateOf(debt?.isMine ?: isMine) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.background)
            .padding(16.dp)
    ) {
        DebtOutlinedTextField(
            color = color,
            value = name.value,
            onValueChange = { name.value = it },
            label = "Имя должника",
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            DebtRadioButton(
                selected = !isMineState.value,
                onClick = { isMineState.value = false },
                color = color,
                text = "Мне должны"
            )

            DebtRadioButton(
                selected = isMineState.value,
                onClick = { isMineState.value = true },
                color = color,
                text = "Я должен"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        DebtOutlinedTextField(
            color = color,
            value = telegramNick.value,
            onValueChange = { telegramNick.value = it },
            label = "Ник в Telegram (для перехода в приложение)",
        )

        Spacer(modifier = Modifier.height(8.dp))

        DebtOutlinedTextField(
            color = color,
            value = debtAmount.value,
            onValueChange = {
                debtAmount.value = it
                    .trim()
                    .replace("-", "")
            },
            label = "Сумма долга",
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(
            label = "Дата возврата",
            date = returnDate.value,
            color = color,
            onDateChanged = { returnDate.value = it }
        )

        DebtOutlinedTextField(
            color = color,
            value = comment.value,
            onValueChange = { comment.value = it },
            label = "Комментарий (необязательно)",
        )

        Spacer(modifier = Modifier.height(16.dp))

        DebtTextButton(
            color = color,
            modifier = Modifier.fillMaxWidth(),
            text = "Сохранить",
            enabled = name.value.isNotBlank() && debtAmount.value.toDoubleOrNull() != null,
        ) {
            if (isEdit) {
                val debt = Debt(
                    id = debt.id,
                    isMine = isMineState.value,
                    telegramNick = if (telegramNick.value.isNotEmpty()) telegramNick.value else debt.telegramNick,
                    name = if (name.value.isNotEmpty()) name.value else debt.name,
                    debtAmount = debt.debtAmount,
                    returnDate = if (returnDate.value.isNotEmpty()) returnDate.value else debt.returnDate,
                    comment = if (comment.value.isNotEmpty()) comment.value else debt.comment
                )
                onSaveComplete(debt)
            } else {
                val amount = debtAmount.value.toDoubleOrNull() ?: 0.0
                val debt = Debt(
                    telegramNick = telegramNick.value,
                    isMine = isMineState.value,
                    name = name.value,
                    debtAmount = amount,
                    returnDate = returnDate.value,
                    comment = comment.value
                )
                onSaveComplete(debt)
            }
        }
    }
}