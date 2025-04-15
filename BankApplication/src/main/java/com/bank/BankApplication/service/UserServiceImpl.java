package com.bank.BankApplication.service;

import com.bank.BankApplication.dto.*;
import com.bank.BankApplication.entity.User;
import com.bank.BankApplication.repo.UserRepo;
import com.bank.BankApplication.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    public BankResponse createUser(UserRequest userRequest) {
        if(userRepo.existsByEmail(userRequest.getEmail())){
           return BankResponse.builder()
                   .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                   .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                   .accountInfo(null)
                   .build();
        }
        User newUser = new User();
        newUser.setFirstName(userRequest.getFirstName());
        newUser.setLastName(userRequest.getLastName());
        newUser.setOtherName(userRequest.getOtherName());
        newUser.setGender(userRequest.getGender());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPhone(userRequest.getPhone());
        newUser.setAlternatePhone(userRequest.getAlternatePhone());
        newUser.setAddress(userRequest.getAddress());
        newUser.setCity(userRequest.getCity());
        newUser.setState(userRequest.getState());
        newUser.setCountry(userRequest.getCountry());
        newUser.setAccountNumber(AccountUtils.generateAccountNumber());
        newUser.setAccountBalance(BigDecimal.ZERO);
        newUser.setStatus("ACTIVE");

        User savedUser = userRepo.save(newUser);

        //sendEmailAlert
        EmailDetails emailDetails =EmailDetails.builder()
                .recipient(userRequest.getEmail())
                .subject("Account Creation")
                .messagebody("Your account has been created!\nYour account has been activated!\n" +
                        " Your account name is "+savedUser.getFirstName()+" "+savedUser.getOtherName()+" "+savedUser.getLastName()+
                        "\nYour account number is "+savedUser.getAccountNumber())
                .build();

        emailService.EmailAlert(emailDetails);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstName()+" "+savedUser.getOtherName()+" "+savedUser.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }

    public User getUserByAcountNumber(String accountNumber) {
        if(userRepo.existsByAccountNumber(accountNumber)){
            User savedUser=userRepo.findByAccountNumber(accountNumber).get();
            return savedUser;
        }
        return null;
    }

    public BankResponse updateUser(String accountNumber,UserRequest userRequest) {
        if(userRepo.existsByAccountNumber(accountNumber)){
            User savedUserFromDB=userRepo.findByAccountNumber(accountNumber).get();
            savedUserFromDB.setFirstName(userRequest.getFirstName());
            savedUserFromDB.setOtherName(userRequest.getOtherName());
            savedUserFromDB.setLastName(userRequest.getLastName());
            savedUserFromDB.setGender(userRequest.getGender());
            savedUserFromDB.setEmail(userRequest.getEmail());
            savedUserFromDB.setPhone(userRequest.getPhone());
            savedUserFromDB.setAlternatePhone(userRequest.getAlternatePhone());
            savedUserFromDB.setAddress(userRequest.getAddress());
            savedUserFromDB.setCity(userRequest.getCity());
            savedUserFromDB.setState(userRequest.getState());
            savedUserFromDB.setCountry(userRequest.getCountry());
            savedUserFromDB.setStatus(userRequest.getStatus());
            User savedUser=userRepo.save(savedUserFromDB);
            EmailDetails emailDetails =EmailDetails.builder()
                    .recipient(userRequest.getEmail())
                    .subject("Account Updation")
                    .messagebody("Your account has been updated!\n" +
                            " Your account name is "+savedUser.getFirstName()+" "+savedUser.getOtherName()+" "+savedUser.getLastName()+
                            "\nYour account number is "+savedUser.getAccountNumber())
                    .build();
            emailService.EmailAlert(emailDetails);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_UPDATED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_UPDATED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(savedUser.getFirstName()+" "+savedUser.getOtherName()+" "+savedUser.getLastName())
                            .accountNumber(savedUser.getAccountNumber())
                            .accountBalance(savedUser.getAccountBalance())
                            .build())
                    .build();

        }
        return null;
    }

    @Override
    public BankResponse deleteUser(String accountNumber) {
        if(userRepo.existsByAccountNumber(accountNumber)){
            User userFromDB=userRepo.findByAccountNumber(accountNumber).get();
            userRepo.delete(userRepo.findByAccountNumber(accountNumber).get());
            EmailDetails emailDetails =EmailDetails.builder()
                    .recipient(userFromDB.getEmail())
                    .subject("Account Deletion")
                    .messagebody("Your account has been deleted!\n Your account number is "+userFromDB.getAccountNumber()+
                            "\nYour account has been deleted successfully")
                    .build();
            emailService.EmailAlert(emailDetails);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DELETED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DELETED_SUCCESS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                .accountInfo(null)
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        if(userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber())){
            User userFromDB=userRepo.findByAccountNumber(enquiryRequest.getAccountNumber()).get();
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(enquiryRequest.getAccountNumber())
                            .accountName(userFromDB.getFirstName()+" "+userFromDB.getOtherName()+" "+userFromDB.getLastName())
                            .accountBalance(userFromDB.getAccountBalance())
                            .build())
                    .build();
        }
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                .accountInfo(null)
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        if(userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber())){
            User userFromDB=userRepo.findByAccountNumber(enquiryRequest.getAccountNumber()).get();
            return userFromDB.getFirstName()+" "+userFromDB.getOtherName()+" "+userFromDB.getLastName();
        }
        return AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE;
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        if(!userRepo.existsByAccountNumber(creditDebitRequest.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit=userRepo.findByAccountNumber(creditDebitRequest.getAccountNumber()).get();
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepo.save(userToCredit);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("Amount Credited to Your Account")
                .messagebody("Dear " + userToCredit.getFirstName() + ",\n\n" +
                        "An amount of ₹" + creditDebitRequest.getAmount() + " has been successfully credited to your account.\n" +
                        "Your updated account balance is ₹" + userToCredit.getAccountBalance() + ".\n\n" +
                        "Thank you for banking with us!\nBankApp Team.")
                .build();

        emailService.EmailAlert(emailDetails);
        System.out.println("Sending email to: " + userToCredit.getEmail());


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(creditDebitRequest.getAccountNumber())
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getOtherName()+" "+userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        if(!userRepo.existsByAccountNumber(creditDebitRequest.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit=userRepo.findByAccountNumber(creditDebitRequest.getAccountNumber()).get();
        if(userToDebit.getAccountBalance().compareTo(creditDebitRequest.getAmount()) < 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
        userRepo.save(userToDebit);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userToDebit.getEmail())
                .subject("Amount Debited from Your Account")
                .messagebody("Dear " + userToDebit.getFirstName() + ",\n\n" +
                        "An amount of ₹" + creditDebitRequest.getAmount() + " has been successfully debited from your account.\n" +
                        "Your updated account balance is ₹" + userToDebit.getAccountBalance() + ".\n\n" +
                        "If you did not authorize this transaction, please contact our support team immediately.\n\n" +
                        "Thank you for banking with us!\nBankApp Team.")

                .build();

        emailService.EmailAlert(emailDetails);
        System.out.println("Sending email to: " + userToDebit.getEmail());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(creditDebitRequest.getAccountNumber())
                        .accountName(userToDebit.getFirstName()+" "+userToDebit.getOtherName()+" "+userToDebit.getLastName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
    }
}
