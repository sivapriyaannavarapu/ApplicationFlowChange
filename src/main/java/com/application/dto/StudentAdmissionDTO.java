//
//package com.application.dto;
//
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.List;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class StudentAdmissionDTO {
//
//    // --- Academic & Personal Info ---
//    private String studAdmsNo;
//    private String studentName;
//    private String surname;
//    private String htNo;
//    private String apaarNo;
//    private LocalDate dateOfJoin;
//    private Integer createdBy;
//    private Long aadharCardNo;
//    private Date dob;
//    private Integer religionId;
//    private Integer casteId;
//    private Integer schoolTypeId;
//
//    // --- Previous School & Orientation Info ---
//    private String schoolName;
//    private Integer preSchoolStateId;
//    private Integer preSchoolDistrictId;
//    private Integer preschoolTypeId;
//    private String admissionReferredBy;
//    private String scoreAppNo;
//    private int marks;
//    private Date orientationDate;
//    private Date appSaleDate;
//    private Float orientationFee;
//
//    // --- Core Admission IDs ---
//    private Integer genderId;
//    private Integer appTypeId;
//    private Integer studentTypeId;
//    private Integer studyTypeId;
//    private Integer orientationId; // Corrected from "Orientationid"
//    private Integer sectionId;
//    private Integer quotaId;
//    private Integer statusId;
//    private Integer classId;
//    private Integer campusId;
////    private Integer proId;
//    private Integer orientationBatchId;
//    private Integer bloodGroupId;
//    // --- Parent Info ---
//    private List<ParentDetailsDTO> parents;
//
//    // --- Nested DTOs ---
//    private AddressDetailsDTO addressDetails;
//    private List<SiblingDTO> siblings;
//    private StudentConcessionDetailsDTO studentConcessionDetails;
//    private ProConcessionDTO proConcessionDetails;
//    private PaymentDetailsDTO paymentDetails;
//}


package com.application.dto;
 
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
 
/**
* Main DTO for the combined Student Admission (Step 1) and Payment (Step 2) forms.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAdmissionDTO {
 
    // --- Personal Information (Step 1) ---
    private String firstName;
    private String lastName;
    private Integer genderId;
    private String apaarNo;
    private Date dob;
    private Long aadharCardNo;
    private Integer quotaId;      // From "Quota/Admission Referred By"
    private Long proReceiptNo;
    private String admissionReferedBy;
    private Date appSaleDate;
    
 
    // --- Parent Information (Step 1) ---
    private String fatherName;
    private Long fatherMobileNo;
 
    // --- Orientation Information (Step 1) ---
    private Integer academicYearId;
    private Integer branchId;       // Replaces campusId
    private Integer studentTypeId;
    private Integer classId;        // From "Joining Class"
    private Integer orientationId;  // From "Orientation Name"
    private Integer appTypeId;      // From "Admission Type"
 
    // --- Address Information (Step 1) ---
    private AddressDetailsDTO addressDetails;
    // --- Hidden/System Fields ---
    private Long studAdmsNo;    // This is your "Application No"
    private Integer proId;        // The Employee ID from the "Employee" dropdown
    private Integer createdBy;    // This 'createdBy' is for the Student record
    // --- Payment Details (Step 2) ---
    /**
     * This field will contain all the data from the Step 2 Payment Popup.
     */
    private PaymentDetailsDTO paymentDetails; 
}