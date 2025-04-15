package com.bank.BankApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "This DTO represents the response returned by the bank application after an API call.")
public class BankResponse {

    @Schema(
            name = "Response Code",
            description = "The code representing the status of the operation, e.g., success or failure.",
            example = "200"
    )
    private String responseCode;

    @Schema(
            name = "Response Message",
            description = "A detailed message providing more information about the result of the operation.",
            example = "Transaction successful"
    )
    private String responseMessage;

    @Schema(
            name = "Account Info",
            description = "The account details associated with the transaction, if applicable.",
            implementation = AccountInfo.class
    )
    private AccountInfo accountInfo;
}
