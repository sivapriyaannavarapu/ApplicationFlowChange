package com.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRangeInfoDTO {
    // The next number available for distribution (MAX(end)+1 or appFrom)
    private Integer nextAvailableNumber; 
    
    // The overall start of the range assigned to the state (from StateApp)
    private Integer overallAppFrom;      
    
    // The overall end of the range assigned to the state (from StateApp)
    private Integer overallAppTo;        
}