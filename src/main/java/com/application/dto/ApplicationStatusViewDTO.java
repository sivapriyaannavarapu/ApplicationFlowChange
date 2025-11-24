package com.application.dto;
 
import java.time.LocalDateTime;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusViewDTO {
 
    private int applicationNo;
    private String pro;
    private String campus;
    private String dgm;
    private String zone;
    private LocalDateTime date;
    private String status;
}