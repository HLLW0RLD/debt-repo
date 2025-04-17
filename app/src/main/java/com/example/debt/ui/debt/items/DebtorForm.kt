package com.example.debt.app.ui.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.debt.app.data.Debtor
import com.example.debt.app.ui.screens.DebtorViewModel
import java.util.*

@Composable
fun DebtorForm(
    viewModel: DebtorViewModel,
    onSaveComplete: () -> Unit
) {
    var telegramNick by remember { mutableStateOf("") }
    var debtAmount by remember { mutableStateOf("") }
    var returnDate by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = telegramNick,
            onValueChange = { telegramNick = it },
            label = { Text("Ник в Telegram") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = debtAmount,
            onValueChange = { debtAmount = it },
            label = { Text("Сумма долга") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Здесь можно добавить DatePicker для выбора даты возврата

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Комментарий (необязательно)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val amount = debtAmount.toDoubleOrNull() ?: 0.0
                val debtor = Debtor(
                    telegramNick = telegramNick,
                    debtAmount = amount,
                    returnDate = returnDate,
                    comment = comment.ifEmpty { null }
                )

                viewModel.insert(debtor)
                onSaveComplete()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = telegramNick.isNotBlank() && debtAmount.toDoubleOrNull() != null
        ) {
            Text("Сохранить")
        }
    }
}