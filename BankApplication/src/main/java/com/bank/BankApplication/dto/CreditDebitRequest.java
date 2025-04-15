package com.bank.BankApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "This DTO represents the request to credit or debit an amount from a user's account.")
public class CreditDebitRequest {

    @Schema(
            name = "Account Number",
            description = "The unique account number of the user from which the amount will be debited or credited.",
            example = "1234567890"
    )
    private String accountNumber;

    @Schema(
            name = "Amount",
            description = "The amount to be debited or credited to the account.",
            example = "1000.50"
    )
    private BigDecimal amount;
}
