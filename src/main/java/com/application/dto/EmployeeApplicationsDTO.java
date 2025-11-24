package com.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeApplicationsDTO {
    private int employeeId;
    private List<Integer> availableApplications;
    private int totalAvailable;

}