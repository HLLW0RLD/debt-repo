package com.example.debt.app.ui.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.debt.R
import com.example.debt.data.model.Debtor
import com.example.debt.data.model.TransactionType
import com.example.debt.utils.darkBGColors
import com.example.debt.utils.openTelegramChat
import com.example.debt.utils.lightBGColors
import com.example.debt.utils.setColorDate
import kotlin.math.abs

const val PLUS = "+"
const val MINUS = "-"

@Composable
fun DebtorCard(
    isMine: Boolean = false,
    debtor: Debtor,
    onPaymentClick: (Debtor) -> Unit,
    onEditClick: (Debtor) -> Unit,
    onDeleteDebtorClick: (Debtor) -> Unit
) {

    val scrollState = rememberLazyListState()
    var showHistory by remember { mutableStateOf(false) }
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val cardColor = remember(debtor.id) {
        val index = abs(debtor.id.hashCode()) % (lightBGColors.size + darkBGColors.size)
        if (isSystemInDarkTheme) darkBGColors[index] else lightBGColors[index]
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onEditClick(debtor) },
                    onTap = { /* Обычный клик -- TODO  */ },
                    onDoubleTap = { /* Двойной клик -- TODO для открытия тг */ },
                    onPress = { /* Начало нажатия (еще не отпустили палец) -- TODO просмотр инф. / оплата */ },
                )
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        contentDescription = "",
                        painter = if (isMine) painterResource(R.drawable.ic_graph_down) else painterResource(R.drawable.ic_graph_up),
                        tint = if (isMine) Color.Red else Color.Green
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = debtor.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Icon(
                    contentDescription = "",
                    painter = painterResource(R.drawable.ic_delete_outline),
                    tint = Color.Red,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .clickable { onDeleteDebtorClick(debtor) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
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
                        val operator = if (transaction.type == TransactionType.PAYMENT) PLUS else MINUS
                        val color = if (transaction.type == TransactionType.PAYMENT) Color.Green else Color.Red
                        var trueOperator = ""
                        var trueColor = Color.White

                        if (isMine) {
                            trueOperator = if (operator == PLUS) MINUS else PLUS
                            trueColor = if (color == Color.Red) Color.Green else Color.Red
                        } else {
                            trueOperator = operator
                            trueColor = color
                        }

                        Text(
                            text = "${transaction.type.v.uppercase()} - ${transaction.date}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$trueOperator ${transaction.amount} ₽",
                            style = MaterialTheme.typography.titleLarge,
                            color = trueColor
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

            if (debtor.returnDate.setColorDate() != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Дата возврата: ${debtor.returnDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = debtor.returnDate.setColorDate() ?: Color.White
                )
            }

            if (debtor.comment.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Комментарий: ${debtor.comment}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    contentDescription = "",
                    painter = painterResource(R.drawable.ic_money),
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .background(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.Yellow
                        )
                        .clickable { onPaymentClick(debtor) }
                )

                if (debtor.telegramNick.isNotBlank()) {
                    Image(
                        contentDescription = "",
                        painter = painterResource(R.drawable.ic_telegram),
                        modifier = Modifier
                            .size(48.dp)
                            .padding(4.dp)
                            .background(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White
                            )
                            .clickable { debtor.telegramNick.openTelegramChat() }
                    )
                }
            }
        }
    }
}