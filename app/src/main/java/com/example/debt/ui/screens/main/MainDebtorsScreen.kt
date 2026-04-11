package com.example.debt.app.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.debt.R
import com.example.debt.data.model.Debt
import com.example.debt.app.ui.items.DebtorCard
import com.example.debt.app.ui.items.DebtorForm
import com.example.debt.app.utils.LogUtils.debugLog
import com.example.debt.ui.items.PaymentDialog
import com.example.debt.ui.items.DebtDialog
import com.example.debt.ui.screens.main.DebtUiState
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
    val mainDebtorViewModel: MainDebtorViewModel = koinViewModel()

    val debtors by mainDebtorViewModel.debtors.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedDebt by remember { mutableStateOf<Debt?>(null) }

    var editedDebt by remember { mutableStateOf<Debt?>(null) }

    var isMineDebtsState by remember { mutableStateOf(false) }

    var editedDebtorBGcolor by remember { mutableStateOf<Color?>(null) }

    val tabs = listOf(
        stringResource(R.string.tab_all),
        stringResource(R.string.tab_owe_me),
        stringResource(R.string.tab_i_owe)
    )
    var selectedTab by remember { mutableIntStateOf(0) }

    var showThemeDialog by remember { mutableStateOf(false) }

    val scrollThreshold = 24f
    var accumulatedScroll by remember { mutableFloatStateOf(0f) }

    var extended by remember { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source != NestedScrollSource.Drag) return Offset.Zero
                accumulatedScroll += available.y

                when {
                    accumulatedScroll > scrollThreshold &&
                            !extended -> {
                        extended = true
                        accumulatedScroll = 0f
                    }

                    accumulatedScroll < -scrollThreshold &&
                            extended -> {
                        extended = false
                        accumulatedScroll = 0f
                    }
                }

                return Offset.Zero
            }

            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity
            ): Velocity {
                accumulatedScroll = 0f
                return Velocity.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
            .fillMaxSize()
            .background(AppColors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .background(AppColors.background),
        ) {
            Spacer(Modifier.size(50.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(AppColors.background)
                    .fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(AppColors.background),
                ) {
                    IconButton(
                        onClick = {
                            // pro version
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_trade),
                            tint = AppColors.accentPrimary,
                            contentDescription = "",
                        )
                    }
                    Text(
                        text = stringResource(R.string.debt_title),
                        fontSize = 24.sp,
                        color = AppColors.textPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        painter = painterResource(R.drawable.gear),
                        tint = AppColors.textPrimary,
                        contentDescription = "",
                    )
                }
            }

            Divider(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AppColors.textPrimary)
            )

            when (val state = debtors) {

                is DebtUiState.Success -> {

                    AnimatedVisibility(
                        visible = extended,
                    ) {
                        TabRow(
                            modifier = Modifier.padding(8.dp),
                            selectedTabIndex = selectedTab,
                            indicator = {},
                            divider = {},
                            containerColor = AppColors.background,
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = {}
                                        )
                                        .background(
                                            shape = RoundedCornerShape(20.dp),
                                            color = if (selectedTab == index) {
                                                AppColors.textPrimary
                                            } else {
                                                AppColors.background
                                            }
                                        ),
                                    selected = selectedTab == index,
                                    onClick = {
                                        selectedTab = index
                                        isMineDebtsState = index == 2
                                    },
                                    text = {
                                        Text(
                                            text = title,
                                            color = if (selectedTab == index) {
                                                AppColors.background
                                            } else {
                                                AppColors.textPrimary
                                            },
                                        )
                                    },
                                )
                            }
                        }
                    }

                    val data = state.debtors

                    if (data.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    indication = null,
                                    interactionSource = null
                                ) {
                                    showBottomSheet = true
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(200.dp),
                                painter = painterResource(R.drawable.ic_trade),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(AppColors.accentPrimary)
                            )
                            Text(
                                color = AppColors.textPrimary,
                                text = stringResource(R.string.empty_debts),
                                fontSize = 24.sp,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                            )
                        }
                    } else {
                        val filteredDebtors = remember(data, selectedTab) {
                            when (selectedTab) {
                                1 -> data.filter { !it.isMine }
                                2 -> data.filter { it.isMine }
                                else -> data
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .background(AppColors.background)
                                .fillMaxSize()
                                .padding(6.dp),
                        ) {
                            item { Spacer(Modifier.size(12.dp)) }

                            items(filteredDebtors.size) { index ->
                                val debtor = filteredDebtors[index]

                                DebtorCard(
                                    isMine = debtor.isMine,
                                    debt = debtor,
                                    onDeleteDebtorClick = {
                                        selectedDebt = it
                                        showDeleteDialog = true
                                    },
                                    onPaymentClick = { debtor ->
                                        editedDebtorBGcolor = if (PreferenceCache.isColoredTheme)
                                            interfaceColorById(debtor.id)
                                        else null
                                        selectedDebt = debtor
                                        showPaymentDialog = true
                                    },
                                    onEditClick = { debtor ->
                                        editedDebtorBGcolor = if (PreferenceCache.isColoredTheme)
                                            interfaceColorById(debtor.id)
                                        else null
                                        editedDebt = debtor
                                        isMineDebtsState = debtor.isMine
                                        showBottomSheet = true

                                        debugLog(debtor.isMine)
                                    }
                                )
                            }
                            item { Spacer(Modifier.size(52.dp)) }
                        }
                    }
                }

                is DebtUiState.Loading -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()

                        Text(
                            color = AppColors.textPrimary,
                            text = stringResource(R.string.loading_debts),
                            fontSize = 24.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                        )
                    }
                }

                is DebtUiState.Error -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) {
                                mainDebtorViewModel.loadAllDebts()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(250.dp),
                            painter = painterResource(R.drawable.ic_money),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(AppColors.accentPrimary)
                        )
                        Text(
                            color = AppColors.textPrimary,
                            text = stringResource(R.string.error_loading),
                            fontSize = 24.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                        )
                    }
                }
            }
        }

        IconButton(
            modifier = Modifier
                .padding(36.dp)
                .size(56.dp)
                .align(Alignment.BottomEnd)
                .background(shape = CircleShape, color = AppColors.accentPrimary),
            onClick = {
                isMineDebtsState = selectedTab == 2
                editedDebt = null
                showBottomSheet = true
            }
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                contentDescription = "",
                tint = AppColors.background,
                painter = painterResource(R.drawable.cross_add)
            )
        }

        if (showPaymentDialog) {
            selectedDebt?.let { debtor ->
                PaymentDialog(
                    debt = debtor,
                    color = editedDebtorBGcolor,
                    onDismiss = {
                        showPaymentDialog = false
                        editedDebtorBGcolor = null
                    },
                    onPayment = { amount, isDebt ->
                        debtor.let { debtor ->
                            if (isDebt) {
                                mainDebtorViewModel.addDebt(debtor.id, amount)
                            } else {
                                mainDebtorViewModel.payDebt(debtor.id, amount)
                            }
                        }
                    }
                )
            }
        }

        if (showDeleteDialog) {
            DebtDialog(
                onDismiss = { showDeleteDialog = false },
                onCancel = { showDeleteDialog = false },
                onConfirm = {
                    mainDebtorViewModel.deleteDebtor(selectedDebt!!.id)
                    showDeleteDialog = false
                }
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    editedDebt = null
                    editedDebtorBGcolor = null
                },
                sheetState = rememberModalBottomSheetState(),
                containerColor = AppColors.background,
            ) {
                DebtorForm(
                    debt = editedDebt,
                    isMine = isMineDebtsState,
                    color = editedDebtorBGcolor ?: AppColors.accentPrimary,
                    onSaveComplete = {
                        if (editedDebt != null) {
                            mainDebtorViewModel.updateDebt(
                                id = it.id,
                                name = it.name,
                                isMine = it.isMine,
                                telegramNick = it.telegramNick,
                                debtAmount = it.debtAmount,
                                returnDate = it.returnDate,
                                comment = it.comment,
                            )
                            showBottomSheet = false
                        } else {
                            mainDebtorViewModel.createDebt(
                                name = it.name,
                                isMine = it.isMine,
                                telegramNick = it.telegramNick,
                                debtAmount = it.debtAmount,
                                returnDate = it.returnDate,
                                comment = it.comment,
                            )
                            showBottomSheet = false
                        }
                        editedDebt = null
                    }
                )
            }
        }
    }
}