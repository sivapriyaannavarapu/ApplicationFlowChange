package com.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearPercentDTO {
    private String year;
    private Double issuedPercent;
    private Double soldPercent;
}

