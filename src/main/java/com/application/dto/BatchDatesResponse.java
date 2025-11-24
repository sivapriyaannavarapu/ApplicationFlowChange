package com.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchDatesResponse {
    private LocalDate courseBatchStartDate;
    private LocalDate courseBatchEndDate;
}
