package com.bank.BankApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "This DTO represents the basic details of a bank account.")
public class AccountInfo {

    @Schema(
            name = "Account Name",
            description = "The full name of the account holder.",
            example = "John Doe"
    )
    private String accountName;

    @Schema(
            name = "Account Number",
            description = "The unique identifier associated with the account.",
            example = "1234567890"
    )
    private String accountNumber;

    @Schema(
            name = "Account Balance",
            description = "The current balance available in the account.",
            example = "15000.75"
    )
    private BigDecimal accountBalance;

}
