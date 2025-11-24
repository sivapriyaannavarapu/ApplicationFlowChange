// RangeResponseDTO.java - wrapper used by the first API
package com.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RangeResponseDTO {
    // mergedRanges: when >1, frontend should show dropdown (list of available blocks)
    private List<AppDistributionDTO> mergedRanges;

    // selectedRange: when mergedRanges size == 1, this contains the full AppRangeDTO with nextStart
    private AppRangeDTO selectedRange;
}
