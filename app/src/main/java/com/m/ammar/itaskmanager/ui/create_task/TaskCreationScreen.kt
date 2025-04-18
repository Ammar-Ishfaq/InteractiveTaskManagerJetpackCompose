package com.m.ammar.itaskmanager.ui.create_task

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.m.ammar.itaskmanager.data.local.model.Priority
import com.m.ammar.itaskmanager.ui.components.StyledErrorSnackbarHost
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreationScreen(
    onSaveClick: (title: String, description: String?, priority: Priority, dueDateMillis: Long) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var selectedPriority by remember { mutableStateOf(Priority.LOW) }
    var dueDateMillis by remember { mutableStateOf(0L) }
    var dueDateText by remember { mutableStateOf("") }

    val showDatePicker = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    if (showDatePicker.value) {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day, 0, 0, 0)
                dueDateMillis = calendar.timeInMillis
                dueDateText = "$day/${month + 1}/$year"
                showDatePicker.value = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create Task") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.text.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please enter a title")
                        }
                        return@FloatingActionButton
                    }
                    onSaveClick(
                        title.text,
                        description.text.takeIf { it.isNotBlank() },
                        selectedPriority,
                        dueDateMillis
                    )
                }
            ) {
                Text("Save")
            }
        },
        snackbarHost = {
            StyledErrorSnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title*") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Priority:")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Priority.entries.forEach { priority ->
                    FilterChip(
                        selected = selectedPriority == priority,
                        onClick = { selectedPriority = priority },
                        label = { Text(priority.name) }
                    )
                }
            }

            OutlinedButton(
                onClick = { showDatePicker.value = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (dueDateText.isNotEmpty()) "Due Date: $dueDateText" else "Pick Due Date")
            }
        }
    }
}
