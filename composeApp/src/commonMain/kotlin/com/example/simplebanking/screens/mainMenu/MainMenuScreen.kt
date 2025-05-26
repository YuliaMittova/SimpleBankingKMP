package com.example.simplebanking.screens.mainMenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import simplebankingkmp.composeapp.generated.resources.Res
import simplebankingkmp.composeapp.generated.resources.create_transaction_main_menu
import simplebankingkmp.composeapp.generated.resources.transaction_list_main_menu

@Composable
fun MainMenuScreen(
    navigateToCreateTransaction: () -> Unit,
    navigateToTransactionList: () -> Unit,
) {
    MainMenuComponent(navigateToCreateTransaction, navigateToTransactionList)
}

@Composable
private fun MainMenuComponent(
    navigateToCreateTransaction: () -> Unit,
    navigateToTransactionList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            Modifier.padding(20.dp)
        ) {
            Text(
                modifier = modifier.clickable { navigateToCreateTransaction() },
                text = stringResource(Res.string.create_transaction_main_menu),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = modifier.height(12.dp))

            Text(
                modifier = modifier.clickable { navigateToTransactionList() },
                text = stringResource(Res.string.transaction_list_main_menu),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
