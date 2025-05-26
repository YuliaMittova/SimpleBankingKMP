package com.example.simplebanking.screens.transactionListFromSnapshot

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.simplebanking.screens.transactionList.TransactionData
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import simplebankingkmp.composeapp.generated.resources.Res
import simplebankingkmp.composeapp.generated.resources.back
import simplebankingkmp.composeapp.generated.resources.transaction_list_title

@SuppressLint("VisibleForTests")
@Composable
fun TransactionListFromSnapshotScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<TransactionListFromSnapshotViewModel>()
    val transactionList = remember { mutableStateListOf<TransactionData>() }

    LaunchedEffect(Unit) {
        viewModel.fetchTransactionsFromSnapshot().collect { transactionData ->
            transactionList.add(transactionData)
        }
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            (TopAppBar(
                title = { Text(text = stringResource(Res.string.transaction_list_title)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                    }
                }
            ))
        },
        modifier = modifier.windowInsetsPadding(WindowInsets.systemBars),
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.padding(paddingValues),
        ) {
            items(transactionList) {
                TransactionItem(it)
            }
        }
    }
}

@Composable
fun TransactionItem(
    transactionData: TransactionData,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            text = transactionData.email,
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            modifier = modifier.padding(horizontal = 10.dp),
            text = transactionData.amount.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Blue
        )

        Text(
            modifier = modifier.padding(start = 10.dp),
            text = transactionData.currency.name,
            style = MaterialTheme.typography.titleMedium,
            color = Color.DarkGray
        )

        Text(
            modifier = modifier.padding(start = 10.dp),
            text = transactionData.timestamp,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
    }
}

