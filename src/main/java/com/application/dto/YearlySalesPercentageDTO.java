package com.application.dto;
 
import lombok.Data;
 
@Data
public class YearlySalesPercentageDTO {
    private int year;
    private double percentage;
    private long totalSold;
    private long totalApplications;
 
    // Main 4-argument constructor
    public YearlySalesPercentageDTO(int year, double percentage, long totalSold, long totalApplications) {
        this.year = year;
        this.percentage = percentage;
        this.totalSold = totalSold;
        this.totalApplications = totalApplications;
    }
 
    /**
     * THIS IS THE NEW CONSTRUCTOR THAT FIXES THE ERROR.
     * It matches the 'SELECT NEW' in the UserAppSoldRepository.
     * Hibernate's SUM() function returns Long, so we use Long here.
     */
    public YearlySalesPercentageDTO(Long totalApplications, Long totalSold) {
        this.year = 0; // Will be set later in the service
        this.percentage = 0.0; // Will be calculated later in the service
        this.totalApplications = (totalApplications != null) ? totalApplications : 0L;
        this.totalSold = (totalSold != null) ? totalSold : 0L;
    }
 
    // Default no-args constructor
    public YearlySalesPercentageDTO() {
        this.year = 0;
        this.percentage = 0.0;
        this.totalSold = 0L;
        this.totalApplications = 0L;
    }
}
 