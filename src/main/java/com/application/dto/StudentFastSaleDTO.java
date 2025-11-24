package com.application.dto; // Assuming your DTO package; adjust if needed

import lombok.Data;

@Data
public class StudentFastSaleDTO {
    // Essential for lookup (not visible in form, but required)
    private Long studAdmsNo;
    private String createdBy;

    // Personal Information (from image)
    private String firstName;
    private String lastName;
    private Integer genderId; // From Male/Female select
    private String dob; // DD/MM/YYYY string

    // Admission Referred By
    private Integer quotaId; // From Quota select

    // Parent Information
    private String fatherName;
    private String fatherMobileNo; // Phone Number

    // Orientation Information
    private Integer academicYearId; // AY 2025-2026 select (or fixed)
    private Integer cityId; // Select City (may be for filtering, not saved directly)
    private Integer branchId; // Select Branch (Campus)
    private Integer classId; // Joining Class select
    private Integer orientationId; // Orientation Name select
    private Integer studentTypeId; // Student Type select
    private Integer appTypeId; // Admission Type select
}