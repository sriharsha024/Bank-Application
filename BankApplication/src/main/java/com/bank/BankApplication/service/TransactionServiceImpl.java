package com.bank.BankApplication.service;

import com.bank.BankApplication.dto.TransactionDTO;
import com.bank.BankApplication.entity.Transaction;
import com.bank.BankApplication.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction =Transaction.builder()
                .transactionId(transactionDTO.getTransactionId())
                .transactionType(transactionDTO.getTransactionType())
                .amount(transactionDTO.getAmount())
                .accountNumber(transactionDTO.getAccountNumber())
                .status(transactionDTO.getStatus())
                .transactionDateandTime(transactionDTO.getTransactionDateandTime())
                .build();
        transactionRepo.save(transaction);
        System.out.println("Transaction saved successfully");


    }
}
