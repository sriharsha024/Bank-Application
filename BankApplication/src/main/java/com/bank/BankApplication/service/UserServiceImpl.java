package com.bank.BankApplication.service;

import com.bank.BankApplication.config.JwtTokenProvider;
import com.bank.BankApplication.dto.*;
import com.bank.BankApplication.entity.User;
import com.bank.BankApplication.repo.UserRepo;
import com.bank.BankApplication.utils.AccountUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setPhone(userRequest.getPhone());
        newUser.setAlternatePhone(userRequest.getAlternatePhone());
        newUser.setAddress(userRequest.getAddress());
        newUser.setCity(userRequest.getCity());
        newUser.setState(userRequest.getState());
        newUser.setCountry(userRequest.getCountry());
        newUser.setAccountNumber(AccountUtils.generateAccountNumber());
        newUser.setAccountBalance(BigDecimal.ZERO);
        newUser.setRole(userRequest.getRole());
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
            savedUserFromDB.setPassword(passwordEncoder.encode(userRequest.getPassword()));
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
    @Transactional
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

        TransactionDTO transactionDTO=TransactionDTO.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(creditDebitRequest.getAmount())
                .status("SUCCESS")
                .build();
        transactionService.saveTransaction(transactionDTO);

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
    @Transactional
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

        TransactionDTO transactionDTO=TransactionDTO.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(creditDebitRequest.getAmount())
                .status("SUCCESS")
                .build();
        transactionService.saveTransaction(transactionDTO);

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

    @Transactional
    public BankResponse transfer(TransferRequest transferRequest) {
        if(!userRepo.existsByAccountNumber(transferRequest.getSourceAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBIT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        if(!userRepo.existsByAccountNumber(transferRequest.getDestinationAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_CREDIT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_CREDIT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User sourceUser=userRepo.findByAccountNumber(transferRequest.getSourceAccountNumber()).get();
        User destinationUser=userRepo.findByAccountNumber(transferRequest.getDestinationAccountNumber()).get();
        if(sourceUser.getAccountBalance().compareTo(transferRequest.getAmount()) < 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceUser.setAccountBalance(sourceUser.getAccountBalance().subtract(transferRequest.getAmount()));
        destinationUser.setAccountBalance(destinationUser.getAccountBalance().add(transferRequest.getAmount()));
        userRepo.save(sourceUser);
        userRepo.save(destinationUser);
        TransactionDTO sourceTransactionDTO=TransactionDTO.builder()
                .accountNumber(sourceUser.getAccountNumber())
                .transactionType("DEBIT")
                .amount(transferRequest.getAmount())
                .status("SUCCESS")
                .build();
        transactionService.saveTransaction(sourceTransactionDTO);
        TransactionDTO destinationTransactionDTO=TransactionDTO.builder()
                .accountNumber(destinationUser.getAccountNumber())
                .transactionType("CREDIT")
                .amount(transferRequest.getAmount())
                .status("SUCCESS")
                .build();
        transactionService.saveTransaction(destinationTransactionDTO);

        EmailDetails sourceEmailDetails = EmailDetails.builder()
                .recipient(sourceUser.getEmail())
                .subject("DEBIT ALERT")
                .messagebody("Dear " + sourceUser.getFirstName()+" "+sourceUser.getOtherName()+" "+sourceUser.getLastName() + ",\n\n" +
                        "An amount of ₹" + transferRequest.getAmount() + " has been successfully debited from your account to " +
                        destinationUser.getFirstName() + " " + destinationUser.getLastName() + ".\n" +
                        "Your updated account balance is ₹" + sourceUser.getAccountBalance() + ".\n\n" +
                        "If you did not authorize this transaction, please contact our support team immediately.\n\n" +
                        "Thank you for banking with us!\nBankApp Team.")
                .build();
        emailService.EmailAlert(sourceEmailDetails);
        System.out.println("Sending email to: " + sourceUser.getEmail());

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(destinationUser.getEmail())
                .subject("CREDIT ALERT")
                .messagebody("Dear " + destinationUser.getFirstName()+" "+destinationUser.getOtherName()+" "+destinationUser.getLastName() + ",\n\n" +
                        "An amount of ₹" + transferRequest.getAmount() + " has been successfully credited to your account from " +
                        sourceUser.getFirstName() + " " + sourceUser.getLastName() + ".\n" +
                        "Your updated account balance is ₹" + destinationUser.getAccountBalance() + ".\n\n" +
                        "Thank you for banking with us!\nBankApp Team.")
                .build();
        emailService.EmailAlert(emailDetails);
        System.out.println("Sending email to: " + destinationUser.getEmail());


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(sourceUser.getFirstName() + " " + sourceUser.getOtherName()+" "+sourceUser.getLastName())
                        .accountNumber(sourceUser.getAccountNumber())
                        .accountBalance(sourceUser.getAccountBalance())
                        .build())
                .build();


    }

    public BankResponse login(LoginDTO loginDTO){
        Authentication authentication=null;
        authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        EmailDetails loginAlert=EmailDetails.builder()
                .recipient(loginDTO.getEmail())
                .subject("LOGIN ALERT")
                .messagebody("Your account has been successfully logged in.")
                .build();
        emailService.EmailAlert(loginAlert);
        System.out.println("Sending email to: " + loginDTO.getEmail());
        return BankResponse.builder()
                .responseCode("LOGIN_SUCCESS_CODE")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();

    }
}
