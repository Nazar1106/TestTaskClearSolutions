package com.example.java_practical_test_assignment.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
public class CreateUserDTO {


    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

   // @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date should be in the past")
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;



}
