package com.application.dto;
import lombok.Data;

@Data
public class FormSubmissionDTO {
    private int userId;
    private int academicYearId;
    private int cityId;
    private int zoneId;
    private int campusId;
    private int issuedToId;
    private int dgmEmployeeId;
    private Integer selectedBalanceTrackId;
    private String applicationNoFrom;
    private String applicationNoTo;
    private int range;
    
    // Added Field
    private float amount; 
}