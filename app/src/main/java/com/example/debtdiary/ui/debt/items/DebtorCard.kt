package com.example.debt.app.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.debt.app.data.Debtor
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DebtorCard(
    debtor: Debtor,
    onDebtorClick: () -> Unit,
    onPaymentClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onDebtorClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = debtor.telegramNick,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Долг: ${debtor.debtAmount} ₽",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Дата займа: ${dateFormat.format(debtor.loanDate)}",
                style = MaterialTheme.typography.bodyMedium
            )

            debtor.returnDate?.let { date ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Дата возврата: ${dateFormat.format(date)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            debtor.comment?.let { comment ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Комментарий: $comment",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onPaymentClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Внести оплату")
            }
        }
    }
}