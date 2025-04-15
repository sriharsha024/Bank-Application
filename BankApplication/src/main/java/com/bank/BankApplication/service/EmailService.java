package com.bank.BankApplication.service;

import com.bank.BankApplication.dto.EmailDetails;

public interface EmailService {

    void EmailAlert(EmailDetails emailDetails);
    void sendEmailWithAttachment(EmailDetails emailDetails);
}
