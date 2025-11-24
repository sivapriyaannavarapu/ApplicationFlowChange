package com.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAutoPopulateResponse {
    private String firstName;
    private String lastName;
    private String parentName;
    private String gender;
    private Double applicationFee;
    private Double confirmationAmount;
    private Double firstConcession;
    private Double secondConcession;
    private Double thirdConcession;
}
