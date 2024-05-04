package com.example.java_practical_test_assignment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "First name is required")
    @NotNull(message = "Last name can't be null")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @NotNull(message = "Last name can't be null")
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Last name is required")
    @Past(message = "Birth date should be in the past")
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;


}




