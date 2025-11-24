//package com.application.service;
//
//import java.time.LocalDate;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.application.dto.StudentFastSaleDTO;
//import com.application.entity.Campus;
//import com.application.entity.Distribution;
//import com.application.entity.Employee;
//import com.application.entity.ParentDetails;
//import com.application.entity.Status;
//import com.application.entity.StudentAcademicDetails;
//import com.application.entity.StudentClass;
//import com.application.entity.StudentOrientationDetails;
//import com.application.entity.StudentPersonalDetails;
//import com.application.entity.StudentRelation;
//import com.application.entity.StudyType;
//import com.application.repository.AcademicYearRepository;
//import com.application.repository.AdmissionTypeRepository;
//import com.application.repository.CampusRepository;
//import com.application.repository.DistributionRepository;
//import com.application.repository.GenderRepository;
//import com.application.repository.ParentDetailsRepository;
//import com.application.repository.QuotaRepository;
//import com.application.repository.StatusRepository;
//import com.application.repository.StudentAcademicDetailsRepository;
//import com.application.repository.StudentClassRepository;
//import com.application.repository.StudentOrientationDetailsRepository;
//import com.application.repository.StudentPersonalDetailsRepository;
//import com.application.repository.StudentRelationRepository;
//import com.application.repository.StudentTypeRepository;
//
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//
//@Service
//public class ApplicationFastSale {
//	
//	@Autowired private DistributionRepository distributionRepository;
//	@Autowired private AcademicYearRepository academicYearRepository;
//	@Autowired private QuotaRepository quotaRepository;
//	@Autowired private GenderRepository genderRepository;
//	@Autowired private AdmissionTypeRepository admissionTypeRepository;
//	@Autowired private StudentTypeRepository studentTypeRepository;
//	@Autowired private StudentClassRepository classRepository;
//	@Autowired private CampusRepository campusRepository;
//	@Autowired private StatusRepository statusRepository;
//	@Autowired private StudentAcademicDetailsRepository studentAcademicDetailsRepository;
//	@Autowired private StudentPersonalDetailsRepository personalDetailsRepository;
//	@Autowired private StudentOrientationDetailsRepository orientationDetailsRepository;
//	@Autowired private StudentRelationRepository relationRepository;
//	@Autowired private ParentDetailsRepository parentDetailsRepository;
//	
//	
//	@Transactional
//    public StudentAcademicDetails createFastSaleAdmission(StudentFastSaleDTO formData) {
//        // ==============================================================
//        // PART 1: VALIDATE AND LOOKUP PRO (essential, same as existing)
//        // ==============================================================
//        Long admissionNumberNumeric = formData.getStudAdmsNo();
//        if (admissionNumberNumeric == null) {
//            throw new IllegalArgumentException("Admission Number must be provided.");
//        }
//        Distribution distribution = distributionRepository.findProDistributionForAdmissionNumber(admissionNumberNumeric)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "No PRO has been assigned for Admission Number: " + admissionNumberNumeric
//                ));
//        Employee pro = distribution.getIssuedToEmployee();
//        if (pro == null) {
//            throw new EntityNotFoundException("A PRO has not been linked to the distribution for Admission Number: " + admissionNumberNumeric);
//        }
//
//        // Parse DOB (DD/MM/YYYY to LocalDate)
//        LocalDate dobLocal = null;
//        if (formData.getDob() != null && !formData.getDob().isEmpty()) {
//            try {
//                String[] parts = formData.getDob().split("/");
//                if (parts.length == 3) {
//                    dobLocal = LocalDate.of(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
//                }
//            } catch (Exception e) {
//                throw new IllegalArgumentException("Invalid DOB format: " + formData.getDob() + ". Use DD/MM/YYYY.");
//            }
//        }
//
//        // --- 1. Save Academic Details (only image fields + essentials) ---
//        StudentAcademicDetails academicDetails = new StudentAcademicDetails();
//        academicDetails.setIs_active(1); // Active
//
//        // Set from DTO
//        if (formData.getAcademicYearId() != null) {
//            academicYearRepository.findById(formData.getAcademicYearId()).ifPresent(academicDetails::setAcademicYear);
//        }
//        academicDetails.setStudAdmsNo(formData.getStudAdmsNo());
//        academicDetails.setFirst_name(formData.getFirstName());
//        academicDetails.setLast_name(formData.getLastName());
//        academicDetails.setAdms_date(LocalDate.now());
//        academicDetails.setApp_sale_date(LocalDate.now());
//
//        // Quota (Admission referred by)
//        if (formData.getQuotaId() != null) {
//            quotaRepository.findById(formData.getQuotaId()).ifPresent(academicDetails::setQuota);
//        }
//
//        // Gender
//        if (formData.getGenderId() != null) {
//            genderRepository.findById(formData.getGenderId()).ifPresent(academicDetails::setGender);
//        }
//
//        // Admission Type
//        if (formData.getAppTypeId() != null) {
//            admissionNumberNumeric.findById(formData.getAppTypeId()).ifPresent(academicDetails::setAdmissionType);
//        }
//
//        // Student Type
//        if (formData.getStudentTypeId() != null) {
//            studentTypeRepository.findById(formData.getStudentTypeId()).ifPresent(academicDetails::setStudentType);
//        }
//
//        // Class and Campus/Branch
//        StudentClass studentClass = classRepository.findById(formData.getClassId())
//                .orElseThrow(() -> new EntityNotFoundException("Invalid Class ID: " + formData.getClassId()));
//        academicDetails.setStudentClass(studentClass);
//
//        Campus campus = campusRepository.findById(formData.getBranchId())
//                .orElseThrow(() -> new EntityNotFoundException("Invalid Branch ID: " + formData.getBranchId()));
//        academicDetails.setCampus(campus);
//
//        // Employee/PRO (from lookup)
//        academicDetails.setCreated_by(formData.getCreatedBy());
//        academicDetails.setEmployee(pro);
//
//        // Defaults
//        StudyType defaultStudyType = studentTypeRepository.findById(1)
//                .orElseThrow(() -> new EntityNotFoundException("Default StudyType (ID: 1) not found"));
//        academicDetails.setStudyType(defaultStudyType);
//
//        Status defaultStatus = statusRepository.findById(2)
//                .orElseThrow(() -> new EntityNotFoundException("Default Status (ID: 2) not found"));
//        academicDetails.setStatus(defaultStatus);
//
//        StudentAcademicDetails savedAcademicDetails = studentAcademicDetailsRepository.save(academicDetails);
//
//        // --- 2. Save Personal Details (minimal: DOB, defaults) ---
//        StudentPersonalDetails personalDetails = new StudentPersonalDetails();
//        personalDetails.setStudentAcademicDetails(savedAcademicDetails);
//        personalDetails.setDob(dobLocal);
//        personalDetails.setCreated_by(formData.getCreatedBy());
//        // Defaults for required fields (as in existing code)
//        personalDetails.setCaste_id(3);
//        personalDetails.setReligion_id(3);
//        personalDetailsRepository.save(personalDetails);
//
//        // --- 3. Save Student Orientation Details ---
//        StudentOrientationDetails orientationDetails = new StudentOrientationDetails();
//        orientationDetails.setStudentAcademicDetails(savedAcademicDetails);
//        if (formData.getOrientationId() != null) {
//            orientationDetailsRepository.findById(formData.getOrientationId()).ifPresent(orientationDetails::setOrientation);
//        }
//        orientationDetailsRepository.save(orientationDetails);
//
//        // --- 4. Save Parent Details (Father only, if provided) ---
//        if (formData.getFatherName() != null && !formData.getFatherName().isEmpty() ||
//            formData.getFatherMobileNo() != null && !formData.getFatherMobileNo().isEmpty()) {
//            StudentRelation fatherRelation = relationRepository.findById(1)
//                    .orElseThrow(() -> new EntityNotFoundException("StudentRelation 'Father' (ID: 1) not found"));
//            ParentDetails parent = new ParentDetails();
//            parent.setStudentAcademicDetails(savedAcademicDetails);
//            parent.setName(formData.getFatherName());
//            parent.setMobileNo(formData.getFatherMobileNo());
//            parent.setCreated_by(formData.getCreatedBy());
//            parent.setStudentRelation(fatherRelation);
//            // Defaults
//            parent.setOccupation("Not Provided");
//            parent.setEmail("not provided");
//            parentDetailsRepository.save(parent);
//        }
//
//        // No address or payment for fast sale
//
//        // TODO: Handle student image upload separately (e.g., via file param in controller)
//
//        return savedAcademicDetails;
//    }
//}
