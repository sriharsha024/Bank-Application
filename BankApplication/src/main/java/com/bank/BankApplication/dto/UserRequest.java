package com.bank.BankApplication.dto;

import com.bank.BankApplication.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "This DTO represents a request to create or update a user's personal information.")
public class UserRequest {

    @Schema(
            name = "First Name",
            description = "The user's first name.",
            example = "John"
    )
    private String firstName;

    @Schema(
            name = "Last Name",
            description = "The user's last name.",
            example = "Doe"
    )
    private String lastName;

    @Schema(
            name = "Other Name",
            description = "Any other name the user may have.",
            example = "JD"
    )
    private String otherName;

    @Schema(
            name = "Gender",
            description = "The user's gender.",
            example = "Male"
    )
    private String gender;

    @Schema(
            name = "Email",
            description = "The user's email address.",
            example = "john.doe@example.com"
    )
    private String email;

    @Schema(
            name = "Password",
            description = "The user's password.",
            example = "johndoe@546"
    )
    private String password;


    @Schema(
            name = "Phone",
            description = "The user's primary phone number.",
            example = "+1234567890"
    )
    private String phone;

    @Schema(
            name = "Alternate Phone",
            description = "An alternate phone number for the user.",
            example = "+0987654321"
    )
    private String alternatePhone;

    @Schema(
            name = "Address",
            description = "The user's address.",
            example = "1234 Elm Street, Springfield"
    )
    private String address;

    @Schema(
            name = "City",
            description = "The user's city of residence.",
            example = "Springfield"
    )
    private String city;

    @Schema(
            name = "State",
            description = "The user's state of residence.",
            example = "Illinois"
    )
    private String state;

    @Schema(
            name = "Country",
            description = "The user's country of residence.",
            example = "USA"
    )
    private String country;

    @Schema(
            name = "Status",
            description = "The current status of the user's account (e.g., active, inactive).",
            example = "active"
    )
    private String status;

    @Schema(
            name = "Role",
            description = "The current role  (e.g., user, admin).",
            example = "user"
    )
    private Role role;

}
