package com.example.java_practical_test_assignment.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonRootName("data")
public class UserAllUpdateDTO {

    @NotNull(message = "Email name must be updated")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "First name must be updated")
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name must be updated")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Birth date must be updated")
    @Past(message = "Birth date should be in the past")
    private LocalDate birthDate;

    @NotNull(message = "Address must be updated")
    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Phone number must be updated")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

}
