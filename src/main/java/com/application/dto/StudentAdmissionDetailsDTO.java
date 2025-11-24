package com.application.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAdmissionDetailsDTO {

    // --- Personal Information (StudentAcademicDetails, StudentPersonalDetails, Employee) ---
    private Integer proReceiptNo; // From academicDetails.pro_receipt_no
    private String firstName;
    private String lastName;
    private String gender; // From academicDetails.gender.name
    private String admissionReferredBy; // From academicDetails.employee.name (PRO)
    private String quota; // From academicDetails.quota.name
    private String apaarNo;
    private Long aadharCardNo; // From personalDetails.stud_aadhaar_no
    private Date dob; // From personalDetails.dob
    // Note: Assuming 'PRO Receipt No' full text is either not stored or is the combo of studAdmsNo + pro_receipt_no

    // --- Parent Information (ParentDetails) ---
    private String fatherName; // From parentDetails.name (where relationId=1)
    private Long fatherMobileNo; // From parentDetails.mobileNo

    // --- Orientation Information (StudentAcademicDetails, StudentOrientationDetails) ---
    private String academicYear; // From academicDetails.academicYear.name
    private String branch; // From academicDetails.campus.name
    private String studentType; // From academicDetails.studentType.name
    private String joiningClass; // From academicDetails.studentClass.name
    private String orientationName; // From orientationDetails.orientation.name
    private String campusCity; // From academicDetails.campus.city.name (assuming Campus has a City link)
    private String branchType; // From academicDetails.campusSchoolType.name
    private String admissionType; // From academicDetails.admissionType.name

    // --- Address Information (StudentAddress) ---
    private String doorNo;
    private String street;
    private String landmark;
    private String area;
    private Integer pincode; // From address.postalCode
    private String district; // From address.district.name
    private String mandal; // From address.mandal.name
    private String addressCity; // From address.city.name
    // Note: The fields in the image for address seem to have duplicated data.
    // We'll map them based on the field names in your save method.

    // All necessary Getters and Setters...
}
