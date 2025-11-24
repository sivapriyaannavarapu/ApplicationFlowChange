package com.application.dto;
 
import lombok.Data;
 
@Data
public class DgmToCampusFormDTO {
    private int userId;
    private int academicYearId;
    private int districtId;
    private int cityId;
    private int campusId;
    private int issuedToId;
    private int proEmployeeId;
    private int selectedBalanceTrackId;
    private String applicationNoFrom;
    private String applicationNoTo;
    private int range;
}
 