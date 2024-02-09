package com.ahmetefeozenc.projedeneme

class Transaction(val userName: String, val amount: Double, val date: String,val transactionType: TransactionType) {
}
enum class TransactionType {
    SENT,
    RECEIVED
}