package com.application.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ApplicationConfirmationDto {

    private String admissionNo;
    private String firstName;
    private String lastName;
    private String parentName;//autopopulate
    private Integer gender;
//    private Integer status;
//    private Double applicationFee;autopopulate

    private List<ConcessionDTO> concessions;

//    private Integer joinYearId;autopopulate
    private Integer streamId;
    private Integer programId;
    private Integer examProgramId;
    private Integer orientationId;
    private Integer batchId;
    private Integer sectionId;
    private Date app_conf_date;
    private Integer foodType;
//    private Integer bloodGroup;
    
    private List<LanguageDTO> languages; 
}

