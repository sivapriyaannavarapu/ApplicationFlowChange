package com.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the entire student concession section, containing common details
 * and a list of individual concession entries.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentConcessionDetailsDTO {
    // Common details for the whole concession group
    private Integer concessionIssuedBy;
    private Integer concessionAuthorisedBy;
    private String description;
    private Integer concessionReasonId;
    private int created_by;

    // A list of the actual concessions being applied
    private List<ConcessionEntryDTO> concessions; 
}