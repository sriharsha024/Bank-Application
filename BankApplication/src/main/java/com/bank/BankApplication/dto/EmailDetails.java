package com.bank.BankApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "This DTO represents the details of an email to be sent, including recipient, subject, message, and an optional attachment.")
public class EmailDetails {

    @Schema(
            name = "Recipient",
            description = "The email address of the recipient.",
            example = "user@example.com"
    )
    private String recipient;

    @Schema(
            name = "Subject",
            description = "The subject of the email.",
            example = "Account Update Notification"
    )
    private String subject;

    @Schema(
            name = "Message Body",
            description = "The body content of the email message.",
            example = "Dear User, Your account has been updated. Please check your balance."
    )
    private String messagebody;

    @Schema(
            name = "Attachment",
            description = "An optional file attachment for the email. If none, this field can be left null.",
            example = "statement.pdf"
    )
    private String attachment;
}
