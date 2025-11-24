package com.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO specifically for Occupation and Sector dropdowns.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OccupationSectorDropdownDTO {
    private Integer id; // Holds either occupationId or sectorId
    private String name; // Holds either occupationName or sectorName
}