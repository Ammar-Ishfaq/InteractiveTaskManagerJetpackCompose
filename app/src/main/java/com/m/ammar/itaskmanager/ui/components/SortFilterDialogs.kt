package com.m.ammar.itaskmanager.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.m.ammar.itaskmanager.R
import com.m.ammar.itaskmanager.data.local.enums.FilterOption
import com.m.ammar.itaskmanager.data.local.enums.SortOption

/**
 * A composable that displays two action buttons for filtering and sorting tasks.
 * Tapping on the buttons opens dialog boxes to select the desired sort or filter option.
 *
 * @param sortOption Current sort option applied to the tasks.
 * @param filterOption Current filter option applied to the tasks.
 * @param onSortChange Lambda to handle changes in the sort option.
 * @param onFilterChange Lambda to handle changes in the filter option.
 */
@Composable
fun FilterSortActionButtons(
    sortOption: SortOption,
    filterOption: FilterOption,
    onSortChange: (SortOption) -> Unit,
    onFilterChange: (FilterOption) -> Unit
) {
    // State to control visibility of the sort and filter dialogs
    var showSortDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        // Filter button that opens filter dialog
        FilterChip(
            selected = filterOption != FilterOption.ALL,
            onClick = { showFilterDialog = true },
            label = {
                Text(text = "Filter", style = MaterialTheme.typography.labelMedium)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.filter),
                    contentDescription = "Filter",
                    modifier = Modifier.size(18.dp)
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        // Sort button that opens sort dialog
        FilterChip(
            selected = sortOption != SortOption.DUE_DATE,
            onClick = { showSortDialog = true },
            label = {
                Text(text = "Sort", style = MaterialTheme.typography.labelMedium)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.sort),
                    contentDescription = "Sort",
                    modifier = Modifier.size(18.dp)
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }

    // Sort dialog
    if (showSortDialog) {
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text("Sort Tasks") },
            text = {
                Column {
                    SortOption.entries.forEach { option ->
                        RadioButtonItem(
                            text = option.name,
                            selected = option == sortOption,
                            onSelect = {
                                onSortChange(option)
                                showSortDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSortDialog = false }) {
                    Text("DONE")
                }
            }
        )
    }

    // Filter dialog
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Tasks") },
            text = {
                Column {
                    FilterOption.entries.forEach { option ->
                        RadioButtonItem(
                            text = option.name,
                            selected = option == filterOption,
                            onSelect = {
                                onFilterChange(option)
                                showFilterDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("DONE")
                }
            }
        )
    }
}

/**
 * A composable that displays a radio button item with text.
 * Used in the filter and sort dialogs.
 *
 * @param text The text label for the radio button.
 * @param selected Whether the radio button is selected.
 * @param onSelect Lambda to handle selection change.
 */
@Composable
private fun RadioButtonItem(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}
