package com.example.luma_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    var selectedTransaction by remember { mutableStateOf<TransactionItemModel?>(null) }

                    if (selectedTransaction != null) {
                        TransactionInfoScreen(
                            transaction = selectedTransaction!!,
                            onBackClick = { selectedTransaction = null }
                        )
                    } else {
                        TransactionsScreen(
                            onTransactionClick = { tx -> selectedTransaction = tx }
                        )
                    }
                }
            }
        }
    }
}
