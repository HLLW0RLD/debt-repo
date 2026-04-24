package com.example.debt.ui.items

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.debt.R
import com.example.debt.app.ui.screens.getFilterText
import com.example.debt.ui.screens.main.FilterType
import com.example.debt.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFilterDropdown(
    currentFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val filterOptions = listOf(
        FilterType.ALL,
        FilterType.ACTIVE,
        FilterType.PAID,
    )

    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(AppColors.surface)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { isExpanded = true }
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getFilterText(currentFilter),
                color = AppColors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(R.drawable.ic_double_arrow_down),
                contentDescription = "",
                modifier = Modifier
                    .size(18.dp)
                    .rotate(if (isExpanded) 180f else 0f),
                tint = AppColors.accentPrimary
            )
        }

        if (isExpanded) {
            Popup(
                alignment = Alignment.TopEnd,
                onDismissRequest = { isExpanded = false }
            ) {
                Card(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AppColors.surface)
                )
                {
                    Column {
                        filterOptions.forEach { filter ->
                            val isSelected = filter == currentFilter

                            Row(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        onFilterSelected(filter)
                                        isExpanded = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = getFilterText(filter),
                                    color = if (isSelected) AppColors.accentPrimary else AppColors.textPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        painter = painterResource(R.drawable.ic_money),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = AppColors.accentPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}