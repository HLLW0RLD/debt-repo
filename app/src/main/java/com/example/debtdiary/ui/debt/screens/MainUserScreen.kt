package com.example.debt.app.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.debt.app.data.Debtor
import com.example.debt.app.ui.items.DebtorCard
import com.example.debtdiary.utils.getCurrentDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainUserScreen() {
    val context = LocalContext.current
    val viewModel: DebtorViewModel = koinViewModel()

    val debtors by viewModel.debtors.collectAsState(initial = emptyList())

    LazyColumn {
        items(debtors.size) { debtor ->
            DebtorCard(
                debtor = debtors[debtor],
                onDebtorClick = { /* обработка клика */ },
                onPaymentClick = { /* обработка оплаты */ }
            )
        }
    }

    // Пример добавления нового должника
    val newDebtor = Debtor(
        telegramNick = "user123",
        debtAmount = 1000.0,
        returnDate = getCurrentDate(),
        comment = "Займ на ремонт"
    )

    Button(onClick = { viewModel.insert(newDebtor) }) {
        Text("Добавить должника")
    }
}