package com.bank.BankApplication.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE="200";
    public static final String ACCOUNT_EXISTS_MESSAGE = "Account already exists";
    public static final String ACCOUNT_CREATION_SUCCESS_CODE="201";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created successfully";
    public static final String ACCOUNT_UPDATED_SUCCESS_CODE="202";
    public static final String ACCOUNT_UPDATED_SUCCESS_MESSAGE = "Account updated successfully";
    public static final String ACCOUNT_DELETED_SUCCESS_CODE="203";
    public static final String ACCOUNT_DELETED_SUCCESS_MESSAGE = "Account deleted successfully";
    public static final String ACCOUNT_NOT_FOUND_CODE="204";
    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";
    public static final String ACCOUNT_FOUND_CODE="205";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account found";
    public static final String ACCOUNT_DEBIT_NOT_FOUND_CODE="206";
    public static final String ACCOUNT_DEBIT_NOT_FOUND_MESSAGE = "Debiters Account not found";
    public static final String ACCOUNT_CREDIT_NOT_FOUND_CODE="207";
    public static final String ACCOUNT_CREDIT_NOT_FOUND_MESSAGE = "Crediter Account not found";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE="101";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE ="Amount credited successfully";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE="102";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Amount debited successfully";
    public static final String ACCOUNT_INSUFFICIENT_BALANCE_CODE="103";
    public static final String ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE = "Insuffcient Account Balance";
    public static final String ACCOUNT_TRANSFER_SUCCESS_CODE ="104" ;
    public static final String ACCOUNT_TRANSFER_SUCCESS_MESSAGE ="Amount transferred successfully";


    public static String generateAccountNumber() {
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;
        int random = (int) Math.floor(Math.random() * (max - min + 1) + min);

        return currentYear.toString() + Integer.toString(random);
    }
}
