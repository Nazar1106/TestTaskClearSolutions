package com.example.java_practical_test_assignment.exception;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import java.time.LocalDate;

@Data
@JsonRootName("error")
public class ErrorObjects {

    private long httpStatus;
    private String detail;
    private LocalDate time;

}
