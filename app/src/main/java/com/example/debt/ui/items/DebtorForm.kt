package com.example.debt.app.ui.items

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.debt.data.model.Debtor
import com.example.debt.ui.items.DatePickerField
import com.example.debt.utils.setColorDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DebtorForm(
    debtor: Debtor? = null,
    isMine: Boolean = false,
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
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Имя должника") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Тип долга:",
                modifier = Modifier.padding(end = 16.dp)
            )

            Row(
                modifier = Modifier
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .padding(vertical = 4.dp)
            ) {
                // Вариант "Мне должны"
                Box(
                    modifier = Modifier
                        .clickable { isMineState.value = false }
                        .background(if (!isMineState.value) Color.LightGray else Color.Transparent)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Мне должны", color = if (!isMineState.value) Color.Black else Color.White)
                }

                // Вариант "Я должен"
                Box(
                    modifier = Modifier
                        .clickable { isMineState.value = true }
                        .background(if (isMineState.value) Color.LightGray else Color.Transparent)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Я должен", color = if (isMineState.value) Color.Black else Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = telegramNick.value,
            onValueChange = { telegramNick.value = it },
            label = { Text("Ник в Telegram (для перехода в приложение)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = debtAmount.value,
            onValueChange = {
                debtAmount.value = it
                    .trim()
                    .replace("-", "")
            },
            label = { Text("Сумма долга") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(
            label = "Дата возврата",
            date = returnDate.value,
            onDateChanged = { returnDate.value = it }
        )

        OutlinedTextField(
            value = comment.value,
            onValueChange = { comment.value = it },
            label = { Text("Комментарий (необязательно)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
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
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.value.isNotBlank() && debtAmount.value.toDoubleOrNull() != null
        ) {
            Text("Сохранить")
        }
    }
}