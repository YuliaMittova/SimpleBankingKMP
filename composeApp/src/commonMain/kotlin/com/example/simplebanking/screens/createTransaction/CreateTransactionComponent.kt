package com.example.simplebanking.screens.createTransaction

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplebanking.domain.Currency
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import simplebankingkmp.composeapp.generated.resources.Res
import simplebankingkmp.composeapp.generated.resources.back
import simplebankingkmp.composeapp.generated.resources.create_transaction_title
import simplebankingkmp.composeapp.generated.resources.email_icon_description
import simplebankingkmp.composeapp.generated.resources.enter_amount_label
import simplebankingkmp.composeapp.generated.resources.enter_amount_placeholder
import simplebankingkmp.composeapp.generated.resources.enter_currency_label
import simplebankingkmp.composeapp.generated.resources.enter_email_label
import simplebankingkmp.composeapp.generated.resources.enter_email_placeholder
import simplebankingkmp.composeapp.generated.resources.enter_transaction_details
import simplebankingkmp.composeapp.generated.resources.make_transaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateTransactionComponent(
    state: TransactionCreationUIState,
    onBackClick: () -> Unit,
    submitTransaction: (String, Double, String) -> Unit,
    onMessageShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var emailTextValue by remember { mutableStateOf("") }
    var amountTextValue by remember { mutableStateOf("") }
    val currencyList = Currency.entries.map { it.name }
    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf(currencyList[0]) }
    val canEdit = state.status != TransactionCreationStatus.TRANSACTION_INITIATED
    val shouldShowSnackBar = state.status != TransactionCreationStatus.IDLE && !state.message.isNullOrEmpty()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            (TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.create_transaction_title)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(Res.string.back)
                        )
                    }
                }
            ))
        },
        modifier = modifier.windowInsetsPadding(WindowInsets.systemBars),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        if (shouldShowSnackBar && !state.message.isNullOrEmpty()) {
            scope.launch {
                onMessageShown()
                snackbarHostState.showSnackbar(state.message, duration = SnackbarDuration.Short)
            }
        }

        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Text(
                modifier = modifier.padding(horizontal = 20.dp),
                text = stringResource(Res.string.enter_transaction_details),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                value = emailTextValue,
                onValueChange = { emailTextValue = it },
                label = { Text(stringResource(Res.string.enter_email_label)) },
                placeholder = { Text(stringResource(Res.string.enter_email_placeholder)) },
                enabled = canEdit,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = stringResource(Res.string.email_icon_description)
                    )
                },
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                value = amountTextValue,
                onValueChange = { amountTextValue = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text(stringResource(Res.string.enter_amount_label)) },
                placeholder = { Text(stringResource(Res.string.enter_amount_placeholder)) },
                enabled = canEdit,
            )

            Spacer(Modifier.height(10.dp))

            ExposedDropdownMenuBox(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedCurrency,
                    onValueChange = {},
                    label = { Text(text = stringResource(Res.string.enter_currency_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    enabled = canEdit,
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    currencyList.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedCurrency = selectionOption
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            Button(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                enabled = canEdit,
                interactionSource = remember { MutableInteractionSource() },
                shape = RoundedCornerShape(6.dp),
                onClick = {
                    focusManager.clearFocus()
                    submitTransaction(
                        emailTextValue, amountTextValue.toDouble(), selectedCurrency
                    )
                },
            ) {
                Text(
                    text = stringResource(Res.string.make_transaction),
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}
