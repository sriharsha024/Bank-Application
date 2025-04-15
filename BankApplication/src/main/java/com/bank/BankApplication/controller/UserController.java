package com.bank.BankApplication.controller;

import com.bank.BankApplication.dto.*;
import com.bank.BankApplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs", description = "Operations related to user account creation, balance management, transfers, and inquiries.")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Create a new user account",
            description = "Registers a new user with their details and creates a bank account."
    )
    @ApiResponse(
            responseCode = "201",
            description = "User account successfully created"
    )
    @PostMapping("/create")
    public ResponseEntity<BankResponse> createUser(@RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Credit amount to user account",
            description = "Credits a specified amount to the given user's account number."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Amount successfully credited to the account"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @PostMapping("/credit")
    public ResponseEntity<BankResponse> creditAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
        if (userService.getUserByAcountNumber(creditDebitRequest.getAccountNumber()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.creditAccount(creditDebitRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Debit amount from user account",
            description = "Debits a specified amount from the given user's account number."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Amount successfully debited from the account"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @PostMapping("/debit")
    public ResponseEntity<BankResponse> debitAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
        if (userService.getUserByAcountNumber(creditDebitRequest.getAccountNumber()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.debitAccount(creditDebitRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Transfer amount between accounts",
            description = "Transfers a specified amount from one account to another."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Amount successfully transferred"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Source or destination account not found"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Source and destination account numbers cannot be the same"
    )
    @PostMapping("/transfer")
    public ResponseEntity<BankResponse> transferAccount(@RequestBody TransferRequest transferRequest) {
        if (userService.getUserByAcountNumber(transferRequest.getSourceAccountNumber()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (userService.getUserByAcountNumber(transferRequest.getDestinationAccountNumber()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (transferRequest.getDestinationAccountNumber().equals(transferRequest.getSourceAccountNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userService.transfer(transferRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Get user details by account number",
            description = "Retrieves user account information for the given account number."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User account found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getUser(@PathVariable String accountNumber) {
        if (userService.getUserByAcountNumber(accountNumber) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.getUserByAcountNumber(accountNumber), HttpStatus.OK);
    }

    @Operation(
            summary = "Check account balance",
            description = "Provides the current balance of the given account number."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Balance retrieved successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @GetMapping("/balanceEnquiry")
    public ResponseEntity<BankResponse> getBalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        if (userService.getUserByAcountNumber(enquiryRequest.getAccountNumber()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.balanceEnquiry(enquiryRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Get account holder name",
            description = "Returns the name of the account holder for the given account number."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account holder name retrieved"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @GetMapping("/nameEnquiry")
    public ResponseEntity<String> getNameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        if (userService.getUserByAcountNumber(enquiryRequest.getAccountNumber()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.nameEnquiry(enquiryRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Update user account details",
            description = "Updates the personal information of a user for the specified account number."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User account updated successfully"
    )
    @PutMapping("/update/{accountNumber}")
    public ResponseEntity<BankResponse> updateUser(@PathVariable String accountNumber, @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.updateUser(accountNumber, userRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete user account",
            description = "Deletes the user account associated with the given account number."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User account deleted successfully"
    )
    @DeleteMapping("/delete/{accountNumber}")
    public ResponseEntity<BankResponse> deleteUser(@PathVariable String accountNumber) {
        return new ResponseEntity<>(userService.deleteUser(accountNumber), HttpStatus.OK);
    }
}
