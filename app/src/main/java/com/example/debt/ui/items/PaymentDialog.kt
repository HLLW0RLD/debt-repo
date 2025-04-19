package com.example.debt.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.debt.data.model.Debtor

@Composable
fun PaymentDialog(
    debtor: Debtor?,
    onDismiss: () -> Unit,
    onPayment: (Double, Boolean) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var isAddition by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Операция с долгом") },
        text = {
            Column {
                Text("Должник: ${debtor?.name.orEmpty()}")
                Text("Текущий долг: ${debtor?.debtAmount ?: 0}")

                Spacer(Modifier.height(16.dp))

                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Сумма") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(Modifier.width(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = !isAddition,
                            onClick = { isAddition = false }
                        )
                        Text("Оплата займа", modifier = Modifier.clickable { isAddition = false })
                    }

                    Spacer(Modifier.width(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = isAddition,
                            onClick = { isAddition = true }
                        )
                        Text("Новый займ", modifier = Modifier.clickable { isAddition = true })
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    amount.toDoubleOrNull()?.let {
                        onPayment(it, isAddition)
                        onDismiss()
                    }
                },
                enabled = amount.toDoubleOrNull() != null
            ) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}