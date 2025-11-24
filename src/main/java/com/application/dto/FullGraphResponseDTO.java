package com.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullGraphResponseDTO {
    private String title;
    private String permissionKey;
    private List<GraphDataDTO> graphData;
    private List<GraphBarDTO> graphBarData;
    private List<YearPercentDTO> yearlyPercent;
}
