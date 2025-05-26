package com.example.simplebanking

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplebanking.screens.createTransaction.CreateTransactionScreen
import com.example.simplebanking.screens.mainMenu.MainMenuScreen
import com.example.simplebanking.screens.transactionList.TransactionListScreen
import kotlinx.serialization.Serializable

@Serializable
object MainMenu

@Serializable
object CreateTransaction

@Serializable
object TransactionList

@Composable
fun App() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        Surface {
            val navController: NavHostController = rememberNavController()

            NavHost(navController = navController, startDestination = MainMenu) {
                composable<MainMenu> {
                    MainMenuScreen(
                        navigateToCreateTransaction = { navController.navigate(CreateTransaction) },
                        navigateToTransactionList = { navController.navigate(TransactionList) },
                    )
                }
                composable<CreateTransaction> {
                    CreateTransactionScreen(
                        navigateBack = { navController.popBackStack() }
                    )
                }
                composable<TransactionList> {
                    TransactionListScreen(
                        navigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
