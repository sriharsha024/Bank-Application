package com.bank.BankApplication.controller;

import com.bank.BankApplication.dto.BankResponse;
import com.bank.BankApplication.dto.CreditDebitRequest;
import com.bank.BankApplication.dto.EnquiryRequest;
import com.bank.BankApplication.dto.UserRequest;
import com.bank.BankApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<BankResponse> createUser(@RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/credit")
    public ResponseEntity<BankResponse> creditAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
        if(userService.getUserByAcountNumber(creditDebitRequest.getAccountNumber()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.creditAccount(creditDebitRequest), HttpStatus.OK);
    }

    @PostMapping("/debit")
    public ResponseEntity<BankResponse> debitAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
        if(userService.getUserByAcountNumber(creditDebitRequest.getAccountNumber()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.debitAccount(creditDebitRequest), HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getUser(@PathVariable String accountNumber) {
        if(userService.getUserByAcountNumber(accountNumber)==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.getUserByAcountNumber(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/balanceEnquiry")
    public ResponseEntity<BankResponse> getBalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        if(userService.getUserByAcountNumber(enquiryRequest.getAccountNumber())==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.balanceEnquiry(enquiryRequest), HttpStatus.OK);
    }

    @GetMapping("/nameEnquiry")
    private ResponseEntity<String> getNameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        if(userService.getUserByAcountNumber(enquiryRequest.getAccountNumber())==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.nameEnquiry(enquiryRequest), HttpStatus.OK);
    }

    @PutMapping("/update/{accountNumber}")
    public ResponseEntity<BankResponse> updateUser(@PathVariable String accountNumber,@RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.updateUser(accountNumber,userRequest),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{accountNumber}")
    public ResponseEntity<BankResponse> deleteUser(@PathVariable String accountNumber) {
        return new ResponseEntity<>(userService.deleteUser(accountNumber), HttpStatus.OK);
    }

}
