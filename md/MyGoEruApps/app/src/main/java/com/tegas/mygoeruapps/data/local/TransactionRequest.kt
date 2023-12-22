package com.tegas.mygoeruapps.data.local

import java.util.Currency

data class TransactionRequest(
    val transaction_details: TransactionDetails
    // Add other attributes as needed
)

data class TransactionDetails(
    val order_id: String,
    val gross_amount: Int,
    val currency: String
    // Add other attributes as needed
)