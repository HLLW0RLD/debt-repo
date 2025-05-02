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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.debt.data.model.Debtor
import com.example.debt.utils.AppColors

@Composable
fun PaymentDialog(
    debtor: Debtor,
    color: Color? = null,
    onDismiss: () -> Unit,
    onPayment: (Double, Boolean) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var isAddition by remember { mutableStateOf(false) }

    AlertDialog(
        containerColor = AppColors.background,
        onDismissRequest = onDismiss,
        title = { Text(text = "Операция с долгом") },
        text = {
            Column {
                Text("Должник: ${debtor.name}")
                Text("Текущий долг: ${debtor.debtAmount}")

                Spacer(Modifier.height(16.dp))

                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = AppColors.background,
                        unfocusedContainerColor = AppColors.background,
                        focusedLabelColor = color ?: AppColors.textPrimary,
                        unfocusedLabelColor = color ?: AppColors.textSecondary,
                        cursorColor = color ?: AppColors.textPrimary,
                        unfocusedIndicatorColor = color ?: AppColors.accentPrimary,
                        focusedIndicatorColor = color ?: AppColors.accentPrimary,
                    ),
                    value = amount,
                    onValueChange = { amount = it
                        amount = it
                            .trim()
                            .replace("-", "") },
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
                            onClick = { isAddition = false },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = color ?: AppColors.accentPrimary,
                                unselectedColor = AppColors.surface
                            )
                        )
                        Text("Оплата займа",
                            modifier = Modifier
                                .clickable {
                                    isAddition = false
                                }
                        )
                    }

                    Spacer(Modifier.width(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = isAddition,
                            onClick = { isAddition = true },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = color ?: AppColors.accentPrimary,
                                unselectedColor = AppColors.surface
                            )
                        )
                        Text(
                            "Новый займ",
                            modifier = Modifier
                                .clickable {
                                    isAddition = true
                                }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = color ?: AppColors.accentPrimary,
                    disabledContainerColor = color ?: AppColors.accentPrimary,
                ),
                onClick = {
                    amount.toDoubleOrNull()?.let {
                        onPayment(it, isAddition)
                        onDismiss()
                    }
                },
                enabled = amount.toDoubleOrNull() != null
            ) {
                Text(
                    text = "Подтвердить",
                    color = AppColors.background
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Отмена",
                    color = AppColors.textPrimary
                )
            }
        }
    )
}