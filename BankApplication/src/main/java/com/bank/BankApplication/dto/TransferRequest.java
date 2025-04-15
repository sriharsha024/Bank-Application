package com.bank.BankApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "This DTO represents a request to transfer an amount from one account to another.")
public class TransferRequest {

    @Schema(
            name = "Source Account Number",
            description = "The account number from which the funds will be transferred.",
            example = "1234567890"
    )
    private String sourceAccountNumber;

    @Schema(
            name = "Destination Account Number",
            description = "The account number to which the funds will be transferred.",
            example = "9876543210"
    )
    private String destinationAccountNumber;

    @Schema(
            name = "Amount",
            description = "The amount of money to be transferred.",
            example = "5000.00"
    )
    private BigDecimal amount;
}
