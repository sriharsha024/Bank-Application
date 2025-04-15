package com.bank.BankApplication.controller;

import com.bank.BankApplication.entity.Transaction;
import com.bank.BankApplication.service.BankStatement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@Tag(
        name = "User Account Management APIs",
        description = "Operations related to user account creation, balance management, transfers, and inquiries."
)
public class TransactionController {

    @Autowired
    private BankStatement bankStatement;

    @Operation(
            summary = "Generate a user's bank statement",
            description = "Retrieves the list of transactions for a given account number between a start and end date. Also generates a PDF statement."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank statement generated successfully"),
            @ApiResponse(responseCode = "204", description = "No transactions found in the given date range"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/bankStatement")
    public ResponseEntity<List<Transaction>> getBankStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) {
        try {
            List<Transaction> transactions = bankStatement.generateStatement(accountNumber, startDate, endDate);
            if (transactions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
