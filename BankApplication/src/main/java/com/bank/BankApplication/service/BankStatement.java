package com.bank.BankApplication.service;

import com.bank.BankApplication.entity.Transaction;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.util.List;

public interface BankStatement {
    public List<Transaction> generateStatement( String accountNumber ,String startDate, String endDate ) throws FileNotFoundException, DocumentException;

}
