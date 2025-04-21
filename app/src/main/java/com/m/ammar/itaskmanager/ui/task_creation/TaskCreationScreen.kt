package com.m.ammar.itaskmanager.ui.task_creation

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import com.m.ammar.itaskmanager.data.local.model.Priority
import com.m.ammar.itaskmanager.ui.components.StyledErrorSnackbarHost
import kotlinx.coroutines.launch
import java.util.*

/**
 * TaskCreationScreen is a composable that provides a UI for creating a new task.
 * It allows users to input a title, description, select a priority, and pick a due date.
 *
 * @param onSaveClick A lambda function invoked when the user clicks the save button.
 *      It receives the task title, description (optional), priority, and due date in milliseconds.
 * @param onBack A lambda function invoked when the user clicks the back button in the top bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreationScreen(
    onSaveClick: (title: String, description: String?, priority: Priority, dueDateMillis: Long) -> Unit,
    onBack: () -> Unit
) {
    // Local context and state for date picker, title, description, etc.
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterTitle = FocusRequester()
    val focusRequesterDescription = FocusRequester()

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var selectedPriority by remember { mutableStateOf(Priority.LOW) }
    var dueDateMillis by remember { mutableStateOf(0L) }
    var dueDateText by remember { mutableStateOf("") }
    var isDateValid by remember { mutableStateOf(true) }

    val showDatePicker = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // DatePicker Dialog logic
    if (showDatePicker.value) {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day, 0, 0, 0)
                dueDateMillis = calendar.timeInMillis
                dueDateText = "$day/${month + 1}/$year"
                isDateValid = true
                showDatePicker.value = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Scaffold for the UI layout with top bar, content, and snackbar for errors
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Task") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
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
                    if (dueDateMillis == 0L) {
                        isDateValid = false
                        showDatePicker.value = true
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please select a due date")
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
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusRequesterDescription.requestFocus()
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesterDescription),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
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

            if (!isDateValid) {
                Text("Please select a valid due date", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/**
 * Preview of the TaskCreationScreen with default lambdas for onSaveClick and onBack.
 */
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TaskCreationScreenPreview() {
    TaskCreationScreen(
        onSaveClick = { title, description, priority, dueDate -> },
        onBack = {}
    )
}
