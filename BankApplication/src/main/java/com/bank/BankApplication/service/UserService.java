package com.bank.BankApplication.service;

import com.bank.BankApplication.dto.BankResponse;
import com.bank.BankApplication.dto.CreditDebitRequest;
import com.bank.BankApplication.dto.EnquiryRequest;
import com.bank.BankApplication.dto.UserRequest;
import com.bank.BankApplication.entity.User;

public interface UserService {
    BankResponse createUser(UserRequest userRequest);
    User getUserByAcountNumber(String accountNumber);
    BankResponse updateUser(String accountNumber,UserRequest userRequest);
    BankResponse deleteUser(String accountNumber);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);

}
