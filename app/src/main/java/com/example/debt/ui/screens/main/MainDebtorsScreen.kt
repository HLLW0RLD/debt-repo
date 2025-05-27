package com.example.debt.app.ui.screens

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.core.view.WindowCompat
import com.example.debt.R
import com.example.debt.data.model.Debtor
import com.example.debt.app.ui.items.DebtorCard
import com.example.debt.app.ui.items.DebtorForm
import com.example.debt.ui.items.PaymentDialog
import com.example.debt.ui.items.SimpleDebtDialog
import com.example.debt.ui.screens.main.MainDebtorViewModel
import com.example.debt.ui.theme.AppColors
import com.example.debt.utils.PreferenceCache
import com.example.debt.ui.theme.interfaceColorById
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUserScreen(
    onSettingsClick: () -> Unit
) {
    val viewModel: MainDebtorViewModel = koinViewModel()

    val debtors by viewModel.debtors.collectAsState(initial = emptyList())

    var showBottomSheet by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedDebtor by remember { mutableStateOf<Debtor?>(null) }

    var editedDebtor by remember { mutableStateOf<Debtor?>(null) }

    var isMineDebtsState by remember { mutableStateOf(false) }

    var editedDebtorBGcolor by remember { mutableStateOf<Color?>(null) }

    val tabs = listOf("все", "мне должны", "я должен")
    var selectedTab by remember { mutableIntStateOf(0) }

    var showThemeDialog by remember { mutableStateOf(false) }

    val filteredDebtors = remember(debtors, selectedTab) {
        when (selectedTab) {
            1 -> debtors.filter { !it.isMine }
            2 -> debtors.filter { it.isMine }
            else -> debtors
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Spacer(Modifier.size(50.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .background(AppColors.background)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                Text(
                    text = "debt",
                    fontSize = 48.sp,
                    color = AppColors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { showThemeDialog = true }
                )
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_shelves_horizontal),
                        tint = AppColors.textPrimary,
                        contentDescription = "",
                    )
                }
            }
            Divider(Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
            )

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = AppColors.background,
                contentColor = AppColors.textPrimary,
                indicator = { }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            isMineDebtsState = index == 2
                        },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) {
                                    AppColors.textPrimary
                                } else {
                                    AppColors.divider
                                },
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .background(AppColors.background)
                    .fillMaxSize()
                    .padding(6.dp,),
            ) {
                item { Spacer(Modifier.size(12.dp)) }
                items(filteredDebtors.size) { index ->
                    DebtorCard(
                        isMine = filteredDebtors[index].isMine,
                        debtor = filteredDebtors[index],
                        onDeleteDebtorClick = {
                            selectedDebtor = it
                            showDeleteDialog = true
                        },
                        onPaymentClick = { debtor ->
                            editedDebtorBGcolor = if (PreferenceCache.isColoredTheme) interfaceColorById(debtor.id) else null
                            selectedDebtor = debtor
                            showPaymentDialog = true
                        },
                        onEditClick = { debtor ->
                            editedDebtorBGcolor = if (PreferenceCache.isColoredTheme) interfaceColorById(debtor.id) else null
                            editedDebtor = debtor
                            isMineDebtsState = debtor.isMine
                            showBottomSheet = true
                        }
                    )
                }
                item { Spacer(Modifier.size(52.dp)) }
            }
        }

        IconButton(
            modifier = Modifier
                .padding(12.dp)
                .size(56.dp)
                .align(Alignment.BottomEnd)
                .background(shape = CircleShape, color = Color.White),
            onClick = {
                isMineDebtsState = selectedTab == 2
                editedDebtor = null
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
            selectedDebtor?.let { debtor ->
                PaymentDialog(
                    debtor = debtor,
                    color = editedDebtorBGcolor,
                    onDismiss = {
                        showPaymentDialog = false
                        editedDebtorBGcolor = null
                                },
                    onPayment = { amount, isDebt ->
                        debtor.let { debtor ->
                            if (isDebt) {
                                viewModel.addDebt(debtor.id, amount)
                            } else {
                                viewModel.payDebt(debtor.id, amount)
                            }
                        }
                    }
                )
            }
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
                onDismissRequest = {
                    showBottomSheet = false
                    editedDebtor = null
                    editedDebtorBGcolor = null
                },
                sheetState = rememberModalBottomSheetState(),
                containerColor = AppColors.background,
            ) {
               DebtorForm(
                   debtor = editedDebtor,
                   isMine = isMineDebtsState,
                   color = editedDebtorBGcolor,
                   onSaveComplete = {
                       if (editedDebtor != null) {
                           viewModel.updateDebt(it)
                           showBottomSheet = false
                       } else {
                           viewModel.insertDebtor(it)
                           showBottomSheet = false
                       }
                       editedDebtor = null
                   }
               )
            }
        }
    }
}