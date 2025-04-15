package com.bank.BankApplication.repo;

import com.bank.BankApplication.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountNumberAndTransactionDateandTimeBetween(String accountNumber, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
