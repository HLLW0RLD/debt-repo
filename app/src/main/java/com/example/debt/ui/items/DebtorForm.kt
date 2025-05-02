package com.example.debt.app.ui.items

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.debt.data.model.Debtor
import com.example.debt.ui.items.baseElements.DatePickerField
import com.example.debt.ui.items.baseElements.DebtOutlinedTextField
import com.example.debt.ui.items.baseElements.DebtRadioButton
import com.example.debt.ui.items.baseElements.DebtTextButton
import com.example.debt.utils.AppColors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DebtorForm(
    debtor: Debtor? = null,
    isMine: Boolean = false,
    color: Color? = null,
    onSaveComplete: (Debtor) -> Unit
) {

    val isEdit = debtor != null

    val name = remember { mutableStateOf(debtor?.name ?: "") }
    val telegramNick = remember { mutableStateOf(debtor?.telegramNick ?: "") }
    val debtAmount = remember { mutableStateOf(debtor?.debtAmount?.toString() ?: "") }
    val returnDate = remember { mutableStateOf(debtor?.returnDate ?: "") }
    val comment = remember { mutableStateOf(debtor?.comment ?: "") }
    val isMineState = remember { mutableStateOf(debtor?.isMine ?: isMine) }

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
            {
                if (isEdit) {
                    val debtor = Debtor(
                        id = debtor.id,
                        isMine = isMineState.value,
                        telegramNick = if (telegramNick.value.isNotEmpty()) telegramNick.value else debtor.telegramNick,
                        name = if (name.value.isNotEmpty()) name.value else debtor.name,
                        debtAmount = debtor.debtAmount,
                        returnDate = if (returnDate.value.isNotEmpty()) returnDate.value else debtor.returnDate,
                        comment = if (comment.value.isNotEmpty()) comment.value else debtor.comment
                    )
                    onSaveComplete(debtor)
                } else {
                    val amount = debtAmount.value.toDoubleOrNull() ?: 0.0
                    val debtor = Debtor(
                        telegramNick = telegramNick.value,
                        isMine = isMineState.value,
                        name = name.value,
                        debtAmount = amount,
                        returnDate = returnDate.value,
                        comment = comment.value
                    )
                    onSaveComplete(debtor)
                }
            }
        }
    }
}