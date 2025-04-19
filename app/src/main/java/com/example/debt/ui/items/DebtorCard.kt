package com.example.debt.app.ui.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.debt.R
import com.example.debt.data.model.Debtor
import com.example.debt.data.model.TransactionType
import com.example.debt.utils.openTelegramChat

@Composable
fun DebtorCard(
    debtor: Debtor,
    onDeleteDebtorClick: () -> Unit,
    onPaymentClick: () -> Unit
) {

    var showHistory by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (debtor.telegramNick.isNotBlank()) {
                            debtor.telegramNick.openTelegramChat()
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = debtor.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Icon(
                    contentDescription = "",
                    painter = painterResource(R.drawable.ic_delete_outline),
                    tint = Color.Red,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .clickable { onDeleteDebtorClick() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showHistory = !showHistory },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Долг: ${debtor.debtAmount} ₽",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    contentDescription = "",
                    painter = painterResource(if (showHistory) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            AnimatedVisibility(visible = showHistory) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp)
                        .padding(8.dp),
                ) {
                    items(debtor.transactions.size) { transactionInd ->
                        val transaction = debtor.transactions[debtor.transactions.size - 1 - transactionInd]
                        val operator = if (transaction.type == TransactionType.PAYMENT) "+" else "-"
                        val color = if (transaction.type == TransactionType.PAYMENT) Color.Green else Color.Red

                        Text(
                            text = "${transaction.type.v.uppercase()} - ${transaction.date}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$operator ${transaction.amount} ₽",
                            style = MaterialTheme.typography.titleLarge,
                            color = color
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Divider(Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Дата займа: ${debtor.loanDate}",
                style = MaterialTheme.typography.bodyMedium
            )

            debtor.returnDate?.let { date ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Дата возврата: ${date}",
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

            Icon(
                contentDescription = "",
                painter = painterResource(R.drawable.ic_money),
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp)
                    .background(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Yellow
                    )
                    .clickable { onPaymentClick() }
            )
        }
    }
}