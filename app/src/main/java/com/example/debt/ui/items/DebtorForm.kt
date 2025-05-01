package com.example.debt.app.ui.items

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = color ?: AppColors.accentPrimary,
                unfocusedLabelColor = color ?: AppColors.accentPrimary,
                focusedBorderColor = color ?: AppColors.accentPrimary,
                unfocusedBorderColor = color ?: AppColors.accentPrimary,
            ),
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Имя должника") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                RadioButton(
                    selected = !isMineState.value,
                    onClick = { isMineState.value = false },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = color ?: AppColors.accentPrimary,
                        unselectedColor = AppColors.surface
                    )
                )
                Text(
                    text = "Мне должны",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            isMineState.value = false
                        },
                    color = if (!isMineState.value) AppColors.textPrimary else AppColors.divider
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                RadioButton(
                    selected = isMineState.value,
                    onClick = { isMineState.value = true },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Gray
                    )
                )
                Text(
                    text = "Я должен",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            isMineState.value = true
                        },
                    color = if (isMineState.value) Color.Black else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = color ?: AppColors.accentPrimary,
                unfocusedLabelColor = color ?: AppColors.accentPrimary,
                focusedBorderColor = color ?: AppColors.accentPrimary,
                unfocusedBorderColor = color ?: AppColors.accentPrimary,
            ),
            value = telegramNick.value,
            onValueChange = { telegramNick.value = it },
            label = { Text("Ник в Telegram (для перехода в приложение)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = color ?: AppColors.accentPrimary,
                unfocusedLabelColor = color ?: AppColors.accentPrimary,
                focusedBorderColor = color ?: AppColors.accentPrimary,
                unfocusedBorderColor = color ?: AppColors.accentPrimary,
            ),
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
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = color ?: AppColors.accentPrimary,
                unfocusedLabelColor = color ?: AppColors.accentPrimary,
                focusedBorderColor = color ?: AppColors.accentPrimary,
                unfocusedBorderColor = color ?: AppColors.accentPrimary,
            ),
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
            enabled = name.value.isNotBlank() && debtAmount.value.toDoubleOrNull() != null,
            colors = ButtonColors(
                contentColor = AppColors.textPrimary,
                disabledContentColor = AppColors.divider,
                disabledContainerColor = color ?: AppColors.accentSecondary,
                containerColor = color ?: AppColors.accentPrimary,
            )
        ) {
            Text("Сохранить")
        }
    }
}