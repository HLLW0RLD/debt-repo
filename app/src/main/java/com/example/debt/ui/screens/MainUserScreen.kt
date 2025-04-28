package com.example.debt.app.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import com.example.debt.R
import com.example.debt.data.model.Debtor
import com.example.debt.app.ui.items.DebtorCard
import com.example.debt.app.ui.items.DebtorForm
import com.example.debt.ui.items.PaymentDialog
import com.example.debt.ui.items.SimpleDebtDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUserScreen() {
    val viewModel: DebtorViewModel = koinViewModel()

    val debtors by viewModel.debtors.collectAsState(initial = emptyList())

    var showBottomSheet by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedDebtor by remember { mutableStateOf<Debtor?>(null) }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        Column {
            Spacer(Modifier.size(45.dp))
//            Divider(Modifier
//                .fillMaxWidth()
//                .height(2.dp)
//                .background(Color.Black)
//            )
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(Modifier.size(12.dp))
                Text(
                    text = "debt",
                    fontSize = 48.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }
            Divider(Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp,),
            ) {
                items(debtors.size) { debtor ->
                    DebtorCard(
                        debtor = debtors[debtor],
                        onDeleteDebtorClick = {
                            selectedDebtor = debtors[debtor]
                            showDeleteDialog = true
                        },
                        onPaymentClick = {
                            selectedDebtor = debtors[debtor]
                            showPaymentDialog = true
                        }
                    )
                }
                item {
                    Spacer(Modifier.size(24.dp))
                }
            }
        }

        IconButton(
            modifier = Modifier
                .padding(12.dp)
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

        if (showDeleteDialog) {
            SimpleDebtDialog(
                onDismiss = { showDeleteDialog = false },
                onCancel = { showDeleteDialog = false },
                onConfirm = {
                    viewModel.deleteDebtor(selectedDebtor!!.id)
                    showDeleteDialog = false
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