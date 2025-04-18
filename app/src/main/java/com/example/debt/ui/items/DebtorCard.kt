package com.example.debt.app.ui.items

import android.R
import android.app.ProgressDialog.show
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.debt.data.model.Debtor
import com.example.debt.utils.openTelegramChat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DebtorCard(
    debtor: Debtor,
    onDebtorClick: () -> Unit,
    onPaymentClick: () -> Unit
) {

    var showHistory by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onPaymentClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = debtor.telegramNick,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    debtor.telegramNick.openTelegramChat()
                }
            )

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
                    painter = painterResource(if (showHistory) R.drawable.arrow_down_float else R.drawable.arrow_up_float)
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
                        val transaction =
                            debtor.transactions[debtor.transactions.size - 1 - transactionInd]
                        Text(
                            text = "${transaction.type.v.uppercase()} - ${debtor.loanDate}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${transaction.amount} ₽",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
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

            TextButton(
                onClick = onDebtorClick,
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Red
                    )
            ) {
                Text(
                    text = "удалить",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}