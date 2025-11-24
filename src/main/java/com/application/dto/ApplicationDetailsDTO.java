package com.application.dto;
 
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date; // For dob
 
@Data
@NoArgsConstructor
public class ApplicationDetailsDTO {
 
    // Personal Information
    private Long proReceiptNo;
    private String firstName;
    private String lastName;
    private Integer genderId;
    private String genderName; // For display
    private String apaarNo;
    private Date dob; // Date of Birth
    private Integer admissionReferredById; // Assuming ID is stored, adjust if name
    private String admissionReferredByName; // For display
    private Integer quotaId;
    private String quotaName; // For display
    private Long aadharCardNo;
 
    // Parent Information
    private ParentSummaryDTO parentInfo;
 
    // Orientation Information
    private Integer academicYearId;
    private String academicYearValue; // e.g., "2025-2026"
    private Integer branchId; // Campus ID
    private String branchName; // Campus Name
    private Integer studentTypeId;
    private String studentTypeName; // For display
    private Integer joiningClassId;
    private String joiningClassName; // For display
    private Integer orientationId;
    private String orientationName; // For display
    private Integer branchTypeId; // Campus School Type ID
    private String branchTypeName; // For display
    private Integer admissionTypeId;
    private String admissionTypeName; // For display
 
    // Address Information
    private AddressDetailsNewDTO addressDetails;
 
}