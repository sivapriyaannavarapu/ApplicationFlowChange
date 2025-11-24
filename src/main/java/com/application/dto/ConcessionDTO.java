package com.application.dto;

import lombok.Data;

@Data
public class ConcessionDTO {
    private Integer concessionTypeId;  
    private Double concessionAmount;
    private Integer reasonId;
}
