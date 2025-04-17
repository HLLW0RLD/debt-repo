package com.example.debt.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.debt.R
import com.example.debt.data.model.Debtor
import com.example.debt.app.ui.items.DebtorCard
import com.example.debt.app.ui.items.DebtorForm
import com.example.debt.ui.items.PaymentDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUserScreen() {
    val context = LocalContext.current
    val viewModel: DebtorViewModel = koinViewModel()

    val debtors by viewModel.debtors.collectAsState(initial = emptyList())

    var showBottomSheet by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var selectedDebtor by remember { mutableStateOf<Debtor?>(null) }

    Box(
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Spacer(Modifier.size(10.dp))
            Text(
                text = "debt",
                fontSize = 48.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(Modifier.size(10.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(debtors.size) { debtor ->
                    DebtorCard(
                        debtor = debtors[debtor],
                        onDebtorClick = {
                            viewModel.deleteDebtor(debtors[debtor].id)
                        },
                        onPaymentClick = {
                            selectedDebtor = debtors[debtor]
                            showPaymentDialog = true
                        }
                    )
                }
            }
        }

        IconButton(
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.BottomEnd)
                .background(shape = CircleShape, color = Color.White),
            onClick = {
                showBottomSheet = true
            }
        ) {
            Icon(
                modifier = Modifier.size(56.dp),
                contentDescription = "",
                tint = Color.Black,
                painter = painterResource(R.drawable.baseline_add_circle_24)
            )
        }

        if (showPaymentDialog) {
            PaymentDialog(
                debtor = selectedDebtor,
                onDismiss = { showPaymentDialog = false },
                onPayment = { amount, isAddition ->
                    selectedDebtor?.let { debtor ->
                        if (isAddition) {
                            viewModel.addDebt(debtor.id, amount)
                        } else {
                            viewModel.payDebt(debtor.id, amount)
                        }
                    }
                }
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState()
            ) {
               DebtorForm(
                   onSaveComplete = {
                       viewModel.insert(it)
                       showBottomSheet = false
                   }
               )
            }
        }
    }
}