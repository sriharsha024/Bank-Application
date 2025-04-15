package com.bank.BankApplication.service;

import com.bank.BankApplication.dto.TransactionDTO;

public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDTO);
}
