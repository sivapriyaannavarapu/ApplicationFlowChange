package com.application.dto;
 
/**
* A simple DTO to hold the raw aggregated data from UserAppSoldRepository.
* This matches the SELECT NEW in the repository.
* We use Long here because SUM() returns Long.
*/
public record GraphSoldSummaryDTO(
    long totalApplications,
    long totalSold
) {
    /**
     * A default constructor for when no data is found.
     */
    public GraphSoldSummaryDTO() {
        this(0L, 0L);
    }
}
 