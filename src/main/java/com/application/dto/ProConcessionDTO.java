package com.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for PRO-specific concession details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProConcessionDTO {
    private Integer concessionAmount;
    private String reason;
    private Integer proEmployeeId;
    private int created_by;
}