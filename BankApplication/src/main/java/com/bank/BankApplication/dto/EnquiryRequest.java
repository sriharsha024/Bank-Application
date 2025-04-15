package com.bank.BankApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "This DTO represents an enquiry request, typically used to request information about an account.")
public class EnquiryRequest {

    @Schema(
            name = "Account Number",
            description = "The account number for which the enquiry is being made.",
            example = "1234567890"
    )
    private String accountNumber;
}
