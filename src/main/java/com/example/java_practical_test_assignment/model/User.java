package com.example.java_practical_test_assignment.model;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String address;

    private String phoneNumber;

}




