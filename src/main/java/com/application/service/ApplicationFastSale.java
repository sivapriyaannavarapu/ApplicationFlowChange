package com.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.dto.AddressDetailsDTO;
import com.application.dto.ApplicationDetailsDTO;
import com.application.dto.ApplicationFastDetailsGet;
import com.application.dto.ConcessionConfirmationDTO;
import com.application.dto.ParentSummaryDTO;
import com.application.dto.PaymentDetailsDTO;
import com.application.dto.SiblingDTO;
import com.application.dto.StudentApplicationSaleColegeDTO;
import com.application.dto.StudentApplicationSingleDTO;
import com.application.dto.StudentApplicationUpdateDTO;
import com.application.dto.StudentCollegeConfirmationDto;
import com.application.dto.StudentFastSaleDTO;
import com.application.entity.AcademicYear;
import com.application.entity.Campus;
import com.application.entity.CmpsOrientationBatchFeeView;
import com.application.entity.Distribution;
import com.application.entity.Employee;
import com.application.entity.ParentDetails;
import com.application.entity.PaymentDetails;
import com.application.entity.Sibling;
import com.application.entity.Status;
import com.application.entity.StudentAcademicDetails;
import com.application.entity.StudentAddress;
import com.application.entity.StudentApplicationTransaction;
import com.application.entity.StudentClass;
import com.application.entity.StudentConcessionType;
import com.application.entity.StudentOrientationDetails;
import com.application.entity.StudentPersonalDetails;
import com.application.entity.StudentRelation;
import com.application.entity.StudyType;
import com.application.repository.AcademicYearRepository;
import com.application.repository.AdmissionTypeRepository;
import com.application.repository.BloodGroupRepository;
import com.application.repository.CampusRepository;
import com.application.repository.CasteRepository;
import com.application.repository.CityRepository;
import com.application.repository.CmpsOrientationBatchFeeViewRepository;
import com.application.repository.ConcessionReasonRepository;
import com.application.repository.ConcessionTypeRepository;
import com.application.repository.DistributionRepository;
import com.application.repository.DistrictRepository;
import com.application.repository.EmployeeRepository;
import com.application.repository.FoodTypeRepository;
import com.application.repository.GenderRepository;
import com.application.repository.MandalRepository;
import com.application.repository.OccupationRepository;
import com.application.repository.OrgBankBranchRepository;
import com.application.repository.OrgBankRepository;
import com.application.repository.OrientationRepository;
import com.application.repository.ParentDetailsRepository;
import com.application.repository.PaymentDetailsRepository;
import com.application.repository.PaymentModeRepository;
import com.application.repository.QuotaRepository;
import com.application.repository.ReligionRepository;
import com.application.repository.SectorRepository;
import com.application.repository.SiblingRepository;
import com.application.repository.StateRepository;
import com.application.repository.StatusRepository;
import com.application.repository.StudentAcademicDetailsRepository;
import com.application.repository.StudentAddressRepository;
import com.application.repository.StudentApplicationTransactionRepository;
import com.application.repository.StudentClassRepository;
import com.application.repository.StudentConcessionTypeRepository;
import com.application.repository.StudentOrientationDetailsRepository;
import com.application.repository.StudentPersonalDetailsRepository;
import com.application.repository.StudentRelationRepository;
import com.application.repository.StudentTypeRepository;
import com.application.repository.StudyTypeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ApplicationFastSale {

	private final ReligionRepository religionRepository;

	@Autowired
	private DistributionRepository distributionRepository;
	@Autowired
	private AcademicYearRepository academicYearRepository;
	@Autowired
	private QuotaRepository quotaRepository;
	@Autowired
	private GenderRepository genderRepository;
	@Autowired
	private AdmissionTypeRepository admissionTypeRepository;
	@Autowired
	private StudentTypeRepository studentTypeRepository;
	@Autowired
	private StudentClassRepository classRepository;
	@Autowired
	private CampusRepository campusRepository;
	@Autowired
	private StatusRepository statusRepository;
	@Autowired
	private StudentAcademicDetailsRepository studentAcademicDetailsRepository;
	@Autowired
	private StudentPersonalDetailsRepository personalDetailsRepository;
	@Autowired
	private StudentOrientationDetailsRepository orientationDetailsRepository;
	@Autowired
	private StudentRelationRepository relationRepository;
	@Autowired
	private ParentDetailsRepository parentDetailsRepository;
	@Autowired
	private StudyTypeRepository studyTypeRepository;
	@Autowired
	private OrientationRepository orientationRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private SiblingRepository siblingRepository;
	@Autowired
	private ConcessionTypeRepository concessionTypeRepository;
	@Autowired
	private ConcessionReasonRepository concessionReasonRepository;
	@Autowired
	private StudentConcessionTypeRepository concessionRepository;
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private DistrictRepository districtRepository;
	@Autowired
	private MandalRepository mandalRepository;
	@Autowired
	private StudentAddressRepository studentAddressRepository;
	@Autowired
	private PaymentModeRepository paymentModeRepository;
	@Autowired
	private PaymentDetailsRepository paymentDetailsRepository;
	@Autowired
	private OrgBankBranchRepository orgBankBranchRepository;
	@Autowired
	private OrgBankRepository orgBankRepository;
	@Autowired
	private StudentApplicationTransactionRepository studentApplicationTransactionRepository;
	@Autowired
	private SectorRepository sectorRepository;
	@Autowired
	private OccupationRepository occupationRepository;
	@Autowired
	private FoodTypeRepository foodTypeRepository;
	@Autowired
	private BloodGroupRepository bloodGroupRepository;
	@Autowired
	private CasteRepository casteRepository;
	@Autowired
	private CmpsOrientationBatchFeeViewRepository cmpsOrientationBatchFeeViewRepository;

	ApplicationFastSale(EmployeeRepository employeeRepository, ReligionRepository religionRepository) {
		this.employeeRepository = employeeRepository;
		this.religionRepository = religionRepository;
	}

	// Application Fast SALE - college
	@Transactional
	public StudentAcademicDetails createFastSaleAdmission(StudentFastSaleDTO formData) {

		Long admissionNumberNumeric = formData.getStudAdmsNo();
		if (admissionNumberNumeric == null) {
			throw new IllegalArgumentException("Admission Number must be provided.");
		}
		Distribution distribution = distributionRepository.findProDistributionForAdmissionNumber(admissionNumberNumeric)
				.orElseThrow(() -> new EntityNotFoundException(
						"No PRO has been assigned for Admission Number: " + admissionNumberNumeric));
		Employee pro = distribution.getIssuedToEmployee();
		if (pro == null) {
			throw new EntityNotFoundException(
					"A PRO has not been linked to the distribution for Admission Number: " + admissionNumberNumeric);
		}

		// --- 1. Save Academic Details (only image fields + essentials) ---
		StudentAcademicDetails academicDetails = new StudentAcademicDetails();
		academicDetails.setIs_active(1); // Active

		// Set from DTO
		if (formData.getAcademicYearId() != null) {
			academicYearRepository.findById(formData.getAcademicYearId()).ifPresent(academicDetails::setAcademicYear);
		}
		academicDetails.setStudAdmsNo(formData.getStudAdmsNo());
		academicDetails.setFirst_name(formData.getFirstName());
		academicDetails.setLast_name(formData.getLastName());
		academicDetails.setAdms_date(LocalDate.now());
		academicDetails.setApaar_no(formData.getApaarNo());
		// academicDetails.setApp_sale_date(LocalDate.now());

		if (formData.getProId() != null) {
			// Convert the Integer Employee ID from the DTO to a String
			String referredByEmployeeId = String.valueOf(formData.getProId());

			// Set the String value in the admission_referred_by column
			academicDetails.setAdmission_referred_by(referredByEmployeeId);
		}

		// Quota (Admission referred by)
		if (formData.getQuotaId() != null) {
			quotaRepository.findById(formData.getQuotaId()).ifPresent(academicDetails::setQuota);
		}

		// Gender
		if (formData.getGenderId() != null) {
			genderRepository.findById(formData.getGenderId()).ifPresent(academicDetails::setGender);
		}

		if (formData.getAppTypeId() != null)
			admissionTypeRepository.findById(formData.getAppTypeId()).ifPresent(academicDetails::setAdmissionType);

		// Student Type
		if (formData.getStudentTypeId() != null) {
			studentTypeRepository.findById(formData.getStudentTypeId()).ifPresent(academicDetails::setStudentType);
		}

		// Class and Campus/Branch
		StudentClass studentClass = classRepository.findById(formData.getClassId())
				.orElseThrow(() -> new EntityNotFoundException("Invalid Class ID: " + formData.getClassId()));
		academicDetails.setStudentClass(studentClass);

		Campus campus = campusRepository.findById(formData.getBranchId())
				.orElseThrow(() -> new EntityNotFoundException("Invalid Branch ID: " + formData.getBranchId()));
		academicDetails.setCampus(campus);

		// Employee/PRO (from lookup)
		academicDetails.setCreated_by(formData.getCreatedBy());
		academicDetails.setEmployee(pro);

		// Defaults
		StudyType defaultStudyType = studyTypeRepository.findById(1)
				.orElseThrow(() -> new EntityNotFoundException("Default StudyType (ID: 1) not found"));
		academicDetails.setStudyType(defaultStudyType);

		Status defaultStatus = statusRepository.findById(9)
				.orElseThrow(() -> new EntityNotFoundException("Default Status (ID: 2) not found"));
		academicDetails.setStatus(defaultStatus);

		StudentAcademicDetails savedAcademicDetails = studentAcademicDetailsRepository.save(academicDetails);

		// --- 2. Save Personal Details (minimal: DOB, defaults) ---
		StudentPersonalDetails personalDetails = new StudentPersonalDetails();
		personalDetails.setStudentAcademicDetails(savedAcademicDetails);
		personalDetails.setDob(formData.getDob());
		personalDetails.setCreated_by(formData.getCreatedBy());
		personalDetails.setStud_aadhaar_no(formData.getAadharCardNo());
		personalDetailsRepository.save(personalDetails);

		// --- 3. Save Student Orientation Details ---
		StudentOrientationDetails orientationDetails = new StudentOrientationDetails();
		orientationDetails.setStudentAcademicDetails(savedAcademicDetails);
		if (formData.getOrientationId() != null) {
			orientationRepository.findById(formData.getOrientationId()).ifPresent(orientationDetails::setOrientation);
		}
		orientationDetailsRepository.save(orientationDetails);

		// --- 4. Save Parent Details (Father only, if provided) ---
		if (formData.getFatherName() != null || formData.getFatherMobileNo() != null) {

			StudentRelation fatherRelation = relationRepository.findById(1) // Assuming 1 = Father
					.orElseThrow(() -> new EntityNotFoundException("StudentRelation 'Father' (ID: 1) not found"));

			ParentDetails parent = new ParentDetails();
			parent.setStudentAcademicDetails(savedAcademicDetails);
			parent.setName(formData.getFatherName());
			parent.setMobileNo(formData.getFatherMobileNo());
			parent.setCreated_by(formData.getCreatedBy());
			parent.setStudentRelation(fatherRelation);
			// Defaults
			parent.setOccupation("Not Provided");
			parent.setEmail("not provided");
			parentDetailsRepository.save(parent);
		}

		if (formData.getAddressDetails() != null) {
			AddressDetailsDTO addressDTO = formData.getAddressDetails();
			StudentAddress address = new StudentAddress();
			address.setStudentAcademicDetails(savedAcademicDetails);
			address.setHouse_no(addressDTO.getDoorNo());
			address.setStreet(addressDTO.getStreet());
			address.setLandmark(addressDTO.getLandmark());
			address.setArea(addressDTO.getArea());
			if (addressDTO.getPincode() != null)
				address.setPostalCode(addressDTO.getPincode());
			if (addressDTO.getStateId() != null)
				stateRepository.findById(addressDTO.getStateId()).ifPresent(address::setState);
			if (addressDTO.getCityId() != null)
				cityRepository.findById(addressDTO.getCityId()).ifPresent(address::setCity);
			if (addressDTO.getMandalId() != null)
				mandalRepository.findById(addressDTO.getMandalId()).ifPresent(address::setMandal);
			if (addressDTO.getDistrictId() != null)
				districtRepository.findById(addressDTO.getDistrictId()).ifPresent(address::setDistrict);

			// --- FIX 1: Use the 'createdBy' from the AddressDetailsDTO ---
			address.setCreated_by(addressDTO.getCreatedBy());

			studentAddressRepository.save(address);
		}

		PaymentDetailsDTO paymentDTO = formData.getPaymentDetails();

		if (paymentDTO != null && paymentDTO.getAmount() != null) {

			PaymentDetails paymentDetails = new PaymentDetails();

			paymentDetails.setStudentAcademicDetails(savedAcademicDetails);
			paymentDetails.setApplication_fee_pay_date(paymentDTO.getPaymentDate());
			paymentDetails.setPre_print_receipt_no(paymentDTO.getPrePrintedReceiptNo());
			paymentDetails.setRemarks(paymentDTO.getRemarks());
			paymentDetails.setCreated_by(paymentDTO.getCreatedBy());
			paymentDetails.setApp_fee(paymentDTO.getAmount());
			paymentDetails.setPaid_amount(paymentDTO.getAmount());
			paymentDetails.setAcedemicYear(savedAcademicDetails.getAcademicYear());
			paymentDetails.setStudentClass(savedAcademicDetails.getStudentClass());
			paymentDetails.setStatus(defaultStatus);

			if (paymentDTO.getPaymentModeId() != null) {
				paymentModeRepository.findById(paymentDTO.getPaymentModeId()).ifPresent(paymentDetails::setPaymenMode);
			}

			PaymentDetails savedPaymentDetails = paymentDetailsRepository.save(paymentDetails);

			Integer paymentModeId = paymentDTO.getPaymentModeId();
			final int DD_PAYMENT_ID = 2;
			final int CHEQUE_PAYMENT_ID = 3;

			if (paymentModeId != null && (paymentModeId == DD_PAYMENT_ID || paymentModeId == CHEQUE_PAYMENT_ID)) {

				StudentApplicationTransaction transaction = new StudentApplicationTransaction();
				transaction.setPaymentDetails(savedPaymentDetails);
				transaction.setPaymentMode(savedPaymentDetails.getPaymenMode());
				transaction.setNumber(paymentDTO.getTransactionNumber());
				transaction.setDate(paymentDTO.getTransactionDate());
				transaction.setApplication_fee_pay_date(paymentDTO.getPaymentDate());
				transaction.setCreated_by(paymentDTO.getCreatedBy());
				transaction.setStatus("Pending");

				// DD / Cheque extra fields
				transaction.setIfsc_code(paymentDTO.getIfscCode());
				if (paymentDTO.getOrganisationId() != null)
					transaction.setOrg_id(paymentDTO.getOrganisationId());
				if (paymentDTO.getBankId() != null)
					orgBankRepository.findById(paymentDTO.getBankId()).ifPresent(transaction::setOrgBank);
				if (paymentDTO.getBranchId() != null)
					orgBankBranchRepository.findById(paymentDTO.getBranchId()).ifPresent(transaction::setOrgBankBranch);
				if (paymentDTO.getCityId() != null)
					cityRepository.findById(paymentDTO.getCityId()).ifPresent(transaction::setCity);

				studentApplicationTransactionRepository.save(transaction);
			}
		}

		return savedAcademicDetails;
	}

	// Application FastSale Get
	public ApplicationFastDetailsGet getFastSaleDetailsByAdmissionNo(Long studAdmsNo) {
		// 1. Fetch the main academic record
		StudentAcademicDetails student = studentAcademicDetailsRepository.findByStudAdmsNo(studAdmsNo)
				.orElseThrow(() -> new EntityNotFoundException("Student not found with Admission No: " + studAdmsNo));

		// 2. Fetch related records (handle possibility of them being null/multiple)
		Optional<StudentPersonalDetails> personalOpt = personalDetailsRepository.findByStudentAcademicDetails(student);
		Optional<StudentOrientationDetails> orientationOpt = orientationDetailsRepository
				.findByStudentAcademicDetails(student);
		// Assuming Father has relationTypeId = 1
		Optional<ParentDetails> fatherOpt = parentDetailsRepository
				.findByStudentAcademicDetailsAndStudentRelationRelationId(student, 1);

		// Fetch Address (Assuming one address per student for fast sale)
		Optional<StudentAddress> addressOpt = studentAddressRepository.findByStudentAcademicDetails(student);

		// Fetch Payment (Assuming one payment for application fee for fast sale)
		// You might need a more specific query if a student has multiple payments.
		Optional<PaymentDetails> paymentOpt = paymentDetailsRepository.findByStudentAcademicDetails(student);

		// Fetch Transaction (only for DD/Cheque payment modes: ID 2 or 3)
		Optional<StudentApplicationTransaction> transactionOpt = Optional.empty();
		if (paymentOpt.isPresent()) {
			transactionOpt = studentApplicationTransactionRepository.findByPaymentDetails(paymentOpt.get());
		}

		// 3. Create the main DTO
		ApplicationFastDetailsGet detailsDTO = new ApplicationFastDetailsGet();
		// Set Admission Number
		

		// 4. Map Academic Details (all fast sale fields)
		detailsDTO.setFirstName(student.getFirst_name());
		detailsDTO.setLastName(student.getLast_name());
		detailsDTO.setApaarNo(student.getApaar_no()); // <<< MISSING FIELD ADDED

		if (student.getAdmission_referred_by() != null) {
			// Since it's stored as a String (Employee ID), pass it directly
			try {
				detailsDTO.setProId(Integer.valueOf(student.getAdmission_referred_by())); // <<< MISSING FIELD ADDED
			} catch (NumberFormatException e) {
				// Handle if the stored value is not a valid Integer ID
			}
		}

		// Map Lookups from Academic Details
		if (student.getGender() != null) {
			detailsDTO.setGenderId(student.getGender().getGender_id());
			detailsDTO.setGenderName(student.getGender().getGenderName());
		}

		if (student.getQuota() != null) {
			detailsDTO.setQuotaId(student.getQuota().getQuota_id());
			detailsDTO.setQuotaName(student.getQuota().getQuota_name());
		}

		if (student.getAdmissionType() != null) {
			detailsDTO.setAppTypeId(student.getAdmissionType().getAdms_type_id()); // <<< MISSING FIELD ADDED
			detailsDTO.setAdmissionTypeName(student.getAdmissionType().getAdms_type_name());
		}

		if (student.getAcademicYear() != null) {
			detailsDTO.setAcademicYearId(student.getAcademicYear().getAcdcYearId());
			detailsDTO.setAcademicYearValue(student.getAcademicYear().getAcademicYear());
		}

		if (student.getCampus() != null) {
			detailsDTO.setBranchId(student.getCampus().getCampusId());
			detailsDTO.setBranchName(student.getCampus().getCampusName());
		}

		if (student.getStudentType() != null) {
			detailsDTO.setStudentTypeId(student.getStudentType().getStud_type_id());
			detailsDTO.setStudentTypeName(student.getStudentType().getStud_type());
		}

		if (student.getStudentClass() != null) {
			detailsDTO.setJoiningClassId(student.getStudentClass().getClassId());
			detailsDTO.setJoiningClassName(student.getStudentClass().getClassName());
		}

		// 5. Map Personal Details (DOB and Aadhaar)
		personalOpt.ifPresent(personal -> {
			detailsDTO.setDob(personal.getDob());
			detailsDTO.setAadharCardNo(personal.getStud_aadhaar_no()); // <<< MISSING FIELD ADDED
		});

		// 6. Map Orientation Details
		Integer cmpsId = student.getCampus() != null ? student.getCampus().getCampusId() : null;
	    Integer classId = student.getStudentClass() != null ? student.getStudentClass().getClassId() : null;
	    
	    // --- 🔑 FIX: Use Optional.map to extract and set the DTO fields 🔑 ---
	    Integer orientationId = orientationOpt
	        .flatMap(orientation -> Optional.ofNullable(orientation.getOrientation()))
	        .map(orientation -> {
	            // Set DTO fields inside the safe map operation
	            detailsDTO.setOrientationId(orientation.getOrientationId());
	            detailsDTO.setOrientationName(orientation.getOrientation_name());
	            
	            // Return the ID for assignment to the external variable
	            return orientation.getOrientationId(); 
	        })
	        .orElse(null);
		
		
		if (orientationId != null && cmpsId != null && classId != null) {
	        Optional<CmpsOrientationBatchFeeView> orientationFeeDetails = cmpsOrientationBatchFeeViewRepository
	                .findSingleBestBatchDetails(orientationId, cmpsId, classId);

	        orientationFeeDetails.ifPresent(feeView -> {
	            detailsDTO.setOrientationStartDate(feeView.getOrientationStartDate()); 
	            detailsDTO.setOrientationEndDate(feeView.getOrientationEndDate());
	            detailsDTO.setOrientationFee(feeView.getOrientationFee()); // Assuming DTO fields exist
	        });
	    }

		// 7. Map Parent Details (Father only)
		fatherOpt.ifPresent(father -> {
			detailsDTO.setParentInfo(new ParentSummaryDTO(father.getName(), father.getMobileNo()));
		});

		// 8. Map Address Details
		addressOpt.ifPresent(address -> {
			AddressDetailsDTO addressDTO = new AddressDetailsDTO();
			addressDTO.setDoorNo(address.getHouse_no());
			addressDTO.setStreet(address.getStreet());
			addressDTO.setLandmark(address.getLandmark());
			addressDTO.setArea(address.getArea());
			addressDTO.setPincode(address.getPostalCode());
			addressDTO.setCreatedBy(address.getCreated_by());

			if (address.getState() != null)
				addressDTO.setStateId(address.getState().getStateId());
			if (address.getCity() != null)
				addressDTO.setCityId(address.getCity().getCityId());
			if (address.getMandal() != null)
				addressDTO.setMandalId(address.getMandal().getMandal_id());
			if (address.getDistrict() != null)
				addressDTO.setDistrictId(address.getDistrict().getDistrictId());

			detailsDTO.setAddressDetails(addressDTO); // <<< MISSING FIELD ADDED
		});
		// Skip ProReceiptNo (not posted in fast sale)

		return detailsDTO;
	}

	// ApplicationSale - Colleges
@Transactional
public StudentAcademicDetails createApplicationSale(StudentApplicationSaleColegeDTO formData) {

    Long admissionNumberNumeric = formData.getStudAdmsNo();
    if (admissionNumberNumeric == null) {
        throw new IllegalArgumentException("Admission Number must be provided.");
    }

    // ==============================================================
    // PART 1: VALIDATE PRO and LOOKUP/FETCH Academic Record
    // ==============================================================
    Distribution distribution = distributionRepository.findProDistributionForAdmissionNumber(admissionNumberNumeric)
        .orElseThrow(() -> new EntityNotFoundException(
            "No PRO has been assigned for Admission Number: " + admissionNumberNumeric));
    Employee pro = distribution.getIssuedToEmployee();
    if (pro == null) {
        throw new EntityNotFoundException(
            "A PRO has not been linked to the distribution for Admission Number: " + admissionNumberNumeric);
    }

    // --- CRITICAL FIX: Fetch existing Academic Details or create a new one (UPSERT) ---
    StudentAcademicDetails academicDetails = studentAcademicDetailsRepository
        .findByStudAdmsNo(admissionNumberNumeric)
        .orElseGet(StudentAcademicDetails::new);

    // --- 1. Map/Update Academic Details (Conditional Update) ---
    academicDetails.setIs_active(1); 
    academicDetails.setStudAdmsNo(formData.getStudAdmsNo());

    // Conditional Updates for Core Identity (To prevent overwriting Fast Sale data with null)
    if (formData.getFirstName() != null) academicDetails.setFirst_name(formData.getFirstName());
    if (formData.getLastName() != null) academicDetails.setLast_name(formData.getLastName());
    if (formData.getApaarNo() != null) academicDetails.setApaar_no(formData.getApaarNo());
    if (formData.getGenderId() != null) genderRepository.findById(formData.getGenderId()).ifPresent(academicDetails::setGender);
    if (formData.getQuotaId() != null) quotaRepository.findById(formData.getQuotaId()).ifPresent(academicDetails::setQuota);
    if (formData.getAppTypeId() != null) admissionTypeRepository.findById(formData.getAppTypeId()).ifPresent(academicDetails::setAdmissionType);
    if (formData.getAcademicYearId() != null) academicYearRepository.findById(formData.getAcademicYearId()).ifPresent(academicDetails::setAcademicYear);
    if (formData.getStudentTypeId() != null) studentTypeRepository.findById(formData.getStudentTypeId()).ifPresent(academicDetails::setStudentType);

    // Fields that must be set/updated regardless of prior state
    academicDetails.setAdms_date(LocalDate.now());
    academicDetails.setApp_sale_date(formData.getAppSaleDate());
    
    if (formData.getProId() != null) {
        academicDetails.setAdmission_referred_by(String.valueOf(formData.getProId()));
    }
    if (formData.getProReceiptNo() != null) {
        academicDetails.setPro_receipt_no(formData.getProReceiptNo().intValue());
    }

    // New/Detailed Academic Fields
    academicDetails.setHt_no(formData.getHallTicketNumber());
    academicDetails.setScore_app_no(formData.getScoreAppNo());
    if (formData.getScoreMarks() != null) {
        academicDetails.setScore_marks(formData.getScoreMarks());
    }

    // Previous School Details
    academicDetails.setPre_school_name(formData.getSchoolName());
    if (formData.getSchoolStateId() != null && formData.getSchoolStateId() > 0) stateRepository.findById(formData.getSchoolStateId()).ifPresent(academicDetails::setState); 
    if (formData.getSchoolDistrictId() != null && formData.getSchoolDistrictId() > 0) districtRepository.findById(formData.getSchoolDistrictId()).ifPresent(academicDetails::setDistrict); 

    if (formData.getClassId() != null) {
        StudentClass studentClass = classRepository.findById(formData.getClassId())
            .orElseThrow(() -> new EntityNotFoundException("Invalid Class ID: " + formData.getClassId()));
        academicDetails.setStudentClass(studentClass);
    }


    if (formData.getBranchId() != null) {
        Campus campus = campusRepository.findById(formData.getBranchId())
            .orElseThrow(() -> new EntityNotFoundException("Invalid Branch ID: " + formData.getBranchId()));
        academicDetails.setCampus(campus);
    }


    academicDetails.setCreated_by(formData.getCreatedBy());
    academicDetails.setEmployee(pro);

    // Defaults/Status Update
    StudyType defaultStudyType = studyTypeRepository.findById(1)
        .orElseThrow(() -> new EntityNotFoundException("Default StudyType (ID: 1) not found"));
    academicDetails.setStudyType(defaultStudyType);

    Status defaultStatus = statusRepository.findById(2)
        .orElseThrow(() -> new EntityNotFoundException("Default Status (ID: 2) not found"));
    academicDetails.setStatus(defaultStatus);

    StudentAcademicDetails savedAcademicDetails = studentAcademicDetailsRepository.save(academicDetails);

    // --- 2. Save/Update Personal Details ---
    StudentPersonalDetails personalDetails = personalDetailsRepository
        .findByStudentAcademicDetails(savedAcademicDetails)
        .orElseGet(StudentPersonalDetails::new);

    personalDetails.setStudentAcademicDetails(savedAcademicDetails);
    personalDetails.setCreated_by(formData.getCreatedBy());

    if (formData.getAadharCardNo() != null) personalDetails.setStud_aadhaar_no(formData.getAadharCardNo());
    if (formData.getDob() != null) personalDetails.setDob(formData.getDob());

    if (formData.getCasteId() != null) casteRepository.findById(formData.getCasteId()).ifPresent(personalDetails::setCaste);
    if (formData.getReligionId() != null) religionRepository.findById(formData.getReligionId()).ifPresent(personalDetails::setReligion);
    if (formData.getBloodGroupId() != null) bloodGroupRepository.findById(formData.getBloodGroupId()).ifPresent(personalDetails::setBloodGroup);
    if (formData.getFoodTypeId() != null) foodTypeRepository.findById(formData.getFoodTypeId()).ifPresent(personalDetails::setFoodType);

    personalDetailsRepository.save(personalDetails);

    // --- 3. Save/Update Student Orientation Details ---
    StudentOrientationDetails orientationDetails = orientationDetailsRepository
        .findByStudentAcademicDetails(savedAcademicDetails)
        .orElseGet(StudentOrientationDetails::new);

    orientationDetails.setStudentAcademicDetails(savedAcademicDetails);
    if (formData.getOrientationId() != null)
        orientationRepository.findById(formData.getOrientationId()).ifPresent(orientationDetails::setOrientation);
    orientationDetailsRepository.save(orientationDetails);

    // --- 4. Save/Update Parent Details (Father and Mother) ---
    StudentRelation fatherRelation = relationRepository.findById(1)
        .orElseThrow(() -> new EntityNotFoundException("StudentRelation 'Father' (ID: 1) not found"));

    // FATHER UPSERT
    if (formData.getFatherName() != null || formData.getFatherMobileNo() != null) {
        ParentDetails father = parentDetailsRepository
            .findByStudentAcademicDetailsAndStudentRelationRelationId(savedAcademicDetails, 1)
            .orElseGet(ParentDetails::new);

        father.setStudentAcademicDetails(savedAcademicDetails);
        father.setStudentRelation(fatherRelation);
        father.setCreated_by(formData.getCreatedBy());
        
        if (formData.getFatherName() != null) father.setName(formData.getFatherName());
        if (formData.getFatherMobileNo() != null) father.setMobileNo(formData.getFatherMobileNo());
        
        father.setEmail(formData.getFatherEmail());
        if (formData.getFatherSectorId() != null) sectorRepository.findById(formData.getFatherSectorId()).ifPresent(father::setSector);
        if (formData.getFatherOccupationId() != null) occupationRepository.findById(formData.getFatherOccupationId())
            .ifPresent(occupation -> father.setOccupation(occupation.getOccupation_name()));
        parentDetailsRepository.save(father);
    }

    // MOTHER UPSERT
    if (formData.getMotherName() != null || formData.getMotherMobileNo() != null) {
        StudentRelation motherRelation = relationRepository.findById(2)
            .orElseThrow(() -> new EntityNotFoundException("StudentRelation 'Mother' (ID: 2) not found"));
        
        ParentDetails mother = parentDetailsRepository
            .findByStudentAcademicDetailsAndStudentRelationRelationId(savedAcademicDetails, 2)
            .orElseGet(ParentDetails::new);

        mother.setStudentAcademicDetails(savedAcademicDetails);
        mother.setStudentRelation(motherRelation);
        mother.setCreated_by(formData.getCreatedBy());
        
        mother.setName(formData.getMotherName());
        mother.setMobileNo(formData.getMotherMobileNo());
        mother.setEmail(formData.getMotherEmail());
        if (formData.getMotherSectorId() != null) sectorRepository.findById(formData.getMotherSectorId()).ifPresent(mother::setSector);
        if (formData.getMotherOccupationId() != null) occupationRepository.findById(formData.getMotherOccupationId())
            .ifPresent(occupation -> mother.setOccupation(occupation.getOccupation_name()));
        parentDetailsRepository.save(mother);
    }

    // --- 5. Save/Update Address Details ---
    if (formData.getAddressDetails() != null) {
        AddressDetailsDTO addressDTO = formData.getAddressDetails();
        
        StudentAddress address = studentAddressRepository
            .findByStudentAcademicDetails(savedAcademicDetails)
            .orElseGet(StudentAddress::new);

        address.setStudentAcademicDetails(savedAcademicDetails);
        address.setHouse_no(addressDTO.getDoorNo());
        address.setStreet(addressDTO.getStreet());
        address.setLandmark(addressDTO.getLandmark());
        address.setArea(addressDTO.getArea());
        if (addressDTO.getPincode() != null) address.setPostalCode(addressDTO.getPincode());
        
        // CRITICAL FIX: Ensure ID is present and non-zero before calling findById
        if (addressDTO.getStateId() != null && addressDTO.getStateId() > 0)
            stateRepository.findById(addressDTO.getStateId()).ifPresent(address::setState);
        if (addressDTO.getCityId() != null && addressDTO.getCityId() > 0)
            cityRepository.findById(addressDTO.getCityId()).ifPresent(address::setCity);
        if (addressDTO.getMandalId() != null && addressDTO.getMandalId() > 0)
            mandalRepository.findById(addressDTO.getMandalId()).ifPresent(address::setMandal);
        if (addressDTO.getDistrictId() != null && addressDTO.getDistrictId() > 0) // <--- FIX HERE
            districtRepository.findById(addressDTO.getDistrictId()).ifPresent(address::setDistrict);

        address.setCreated_by(addressDTO.getCreatedBy());
        studentAddressRepository.save(address);
    }
    
    // --- 6. Save/Update Siblings (UPSERT LOGIC) ---
    if (formData.getSiblings() != null && !formData.getSiblings().isEmpty()) {
        Map<String, Sibling> existingSiblingsMap = siblingRepository
            .findByStudentAcademicDetails(savedAcademicDetails).stream()
            .filter(s -> s.getSibling_name() != null)
            .collect(Collectors.toMap(Sibling::getSibling_name, Function.identity(), (first, second) -> first)); 

        for (SiblingDTO siblingDto : formData.getSiblings()) {
            Sibling sibling = existingSiblingsMap.get(siblingDto.getFullName());
            if (sibling == null) {
                sibling = new Sibling();
                sibling.setStudentAcademicDetails(savedAcademicDetails);
                sibling.setCreated_by(siblingDto.getCreatedBy());
                sibling.setSibling_name(siblingDto.getFullName());
            }
            sibling.setSibling_school(siblingDto.getSchoolName());
            if (siblingDto.getRelationTypeId() != null) relationRepository.findById(siblingDto.getRelationTypeId()).ifPresent(sibling::setStudentRelation);
            if (siblingDto.getClassId() != null) classRepository.findById(siblingDto.getClassId()).ifPresent(sibling::setStudentClass);
            if (siblingDto.getGenderId() != null) genderRepository.findById(siblingDto.getGenderId()).ifPresent(sibling::setGender);
            siblingRepository.save(sibling);
        }
    }

    // --- 7. Save/Update Concession Details (UPSERT LOGIC) ---
    if (formData.getConcessions() != null && !formData.getConcessions().isEmpty()) {
        Map<Integer, StudentConcessionType> existingConcessionsMap = concessionRepository
            .findByStudAdmsId(savedAcademicDetails.getStud_adms_id()).stream()
            .filter(c -> c.getConcessionType() != null).collect(Collectors.toMap(
                c -> c.getConcessionType().getConcTypeId(), Function.identity(), (first, second) -> first)); 

        AcademicYear currentYear = academicYearRepository
            .findById(savedAcademicDetails.getAcademicYear().getAcdcYearId())
            .orElseThrow(() -> new EntityNotFoundException("Academic Year not found"));

        for (ConcessionConfirmationDTO concDto : formData.getConcessions()) {
            StudentConcessionType concession = existingConcessionsMap.get(concDto.getConcessionTypeId());
            if (concession == null) {
                concession = new StudentConcessionType();
                concession.setStudAdmsId(savedAcademicDetails.getStud_adms_id());
                concession.setAcademicYear(currentYear);
                concession.setCreated_by(concDto.getCreatedBy());
                concession.setCreated_Date(LocalDateTime.now());
                if (concDto.getConcessionTypeId() != null) concessionTypeRepository.findById(concDto.getConcessionTypeId()).ifPresent(concession::setConcessionType);
            }
            concession.setConc_amount(concDto.getConcessionAmount());
            concession.setComments(concDto.getComments());
            if (concDto.getReasonId() != null) concessionReasonRepository.findById(concDto.getReasonId()).ifPresent(concession::setConcessionReason);
            concession.setConc_referred_by(concDto.getConcReferedBy());
            if (concDto.getGivenById() != null) concession.setConc_issued_by(concDto.getGivenById());
            if (concDto.getAuthorizedById() != null) concession.setConc_authorised_by(concDto.getAuthorizedById());
            concessionRepository.save(concession);
        }
    }

    // --- 8. Save NEW Payment and Transaction Details ---
    PaymentDetailsDTO paymentDTO = formData.getPaymentDetails();
    if (paymentDTO != null && paymentDTO.getAmount() != null) {
        
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setStudentAcademicDetails(savedAcademicDetails);
        paymentDetails.setApplication_fee_pay_date(paymentDTO.getPaymentDate());
        paymentDetails.setPre_print_receipt_no(paymentDTO.getPrePrintedReceiptNo());
        paymentDetails.setRemarks(paymentDTO.getRemarks());
        paymentDetails.setCreated_by(paymentDTO.getCreatedBy());
        paymentDetails.setApp_fee(paymentDTO.getAmount());
        paymentDetails.setPaid_amount(paymentDTO.getAmount());
        paymentDetails.setAcedemicYear(savedAcademicDetails.getAcademicYear());
        paymentDetails.setStudentClass(savedAcademicDetails.getStudentClass());
        paymentDetails.setStatus(defaultStatus);
        if (paymentDTO.getPaymentModeId() != null) paymentModeRepository.findById(paymentDTO.getPaymentModeId()).ifPresent(paymentDetails::setPaymenMode);
        
        PaymentDetails savedPaymentDetails = paymentDetailsRepository.save(paymentDetails);

        // Transaction (Only for DD/Cheque)
        Integer paymentModeId = paymentDTO.getPaymentModeId();
        final int DD_PAYMENT_ID = 2;
        final int CHEQUE_PAYMENT_ID = 3;

        if (paymentModeId != null && (paymentModeId == DD_PAYMENT_ID || CHEQUE_PAYMENT_ID == paymentModeId)) {
            StudentApplicationTransaction transaction = new StudentApplicationTransaction(); 
            transaction.setPaymentDetails(savedPaymentDetails);
            transaction.setPaymentMode(savedPaymentDetails.getPaymenMode());
            transaction.setNumber(paymentDTO.getTransactionNumber());
            transaction.setDate(paymentDTO.getTransactionDate());
            transaction.setApplication_fee_pay_date(paymentDTO.getPaymentDate());
            transaction.setCreated_by(paymentDTO.getCreatedBy());
            transaction.setStatus("Pending");
            
            // Transaction location details
            transaction.setIfsc_code(paymentDTO.getIfscCode());
            if (paymentDTO.getOrganisationId() != null) transaction.setOrg_id(paymentDTO.getOrganisationId());
            if (paymentDTO.getBankId() != null) orgBankRepository.findById(paymentDTO.getBankId()).ifPresent(transaction::setOrgBank);
            if (paymentDTO.getBranchId() != null) orgBankBranchRepository.findById(paymentDTO.getBranchId()).ifPresent(transaction::setOrgBankBranch);
            if (paymentDTO.getCityId() != null) cityRepository.findById(paymentDTO.getCityId()).ifPresent(transaction::setCity);

            studentApplicationTransactionRepository.save(transaction);
        }
    }

    return savedAcademicDetails;
}

	// application-sale - colleges - get

	@Transactional
public StudentApplicationSingleDTO getSingleApplicationDetails(Long studAdmsId) {

    // 1. Fetch Academic Entity
    StudentAcademicDetails academic = studentAcademicDetailsRepository.findByStudAdmsNo(studAdmsId)
            .orElseThrow(() -> new EntityNotFoundException("ID not found"));

    StudentApplicationSingleDTO dto = new StudentApplicationSingleDTO();

    // --- Academic Basic ---
    dto.setStudAdmsId(academic.getStud_adms_id());
    dto.setStudAdmsNo(academic.getStudAdmsNo());
    dto.setFirstName(academic.getFirst_name());
    dto.setLastName(academic.getLast_name());
    dto.setApaarNo(academic.getApaar_no());
    dto.setAppSaleDate(academic.getApp_sale_date());
    dto.setHallTicketNo(academic.getHt_no());
    dto.setScoreMarks(academic.getScore_marks());
    dto.setPreSchoolName(academic.getPre_school_name());

    // --- Academic Lookups ---
    if (academic.getAcademicYear() != null) {
        dto.setAcademicYearId(academic.getAcademicYear().getAcdcYearId());
        dto.setAcademicYearName(academic.getAcademicYear().getAcademicYear());
    }
    if (academic.getStudentClass() != null) {
        dto.setClassId(academic.getStudentClass().getClassId());
        dto.setClassName(academic.getStudentClass().getClassName());
    }
    if (academic.getCampus() != null) {
        dto.setBranchId(academic.getCampus().getCampusId());
        dto.setBranchName(academic.getCampus().getCampusName());
    }
    if (academic.getEmployee() != null) {
        dto.setProId(academic.getEmployee().getEmp_id());
        dto.setProName(academic.getEmployee().getFirst_name() + " " + academic.getEmployee().getLast_name());
    }
    if (academic.getQuota() != null) {
        dto.setQuotaId(academic.getQuota().getQuota_id());
        dto.setQuotaName(academic.getQuota().getQuota_name());
    }
    if (academic.getGender() != null) {
        dto.setGenderId(academic.getGender().getGender_id());
        dto.setGenderName(academic.getGender().getGenderName());
    }
    if (academic.getAdmissionType() != null) {
        dto.setAdmissionTypeId(academic.getAdmissionType().getAdms_type_id());
        dto.setAdmissionTypeName(academic.getAdmissionType().getAdms_type_name());
    }
    if (academic.getStudentType() != null) {
        dto.setStudentTypeId(academic.getStudentType().getStud_type_id());
        dto.setStudentTypeName(academic.getStudentType().getStud_type());
    }

    // Pre-School State & District
    if (academic.getState() != null) {
        dto.setPreSchoolStateId(academic.getState().getStateId());
        dto.setPreSchoolStateName(academic.getState().getStateName());
    }
    if (academic.getDistrict() != null) {
        dto.setPreSchoolDistrictId(academic.getDistrict().getDistrictId());
        dto.setPreSchoolDistrictName(academic.getDistrict().getDistrictName());
    }

    // --- Personal Details ---
    StudentPersonalDetails personal = personalDetailsRepository
            .findByStudentAcademicDetails(academic).orElse(null);

    if (personal != null) {
        dto.setAadharNo(personal.getStud_aadhaar_no());
        dto.setDob(personal.getDob());

        if (personal.getCaste() != null) {
            dto.setCasteId(personal.getCaste().getCaste_id());
            dto.setCasteName(personal.getCaste().getCaste_type());
        }
        if (personal.getReligion() != null) {
            dto.setReligionId(personal.getReligion().getReligion_id());
            dto.setReligionName(personal.getReligion().getReligion_type());
        }
        if (personal.getBloodGroup() != null) {
            dto.setBloodGroupId(personal.getBloodGroup().getBlood_group_id());
            dto.setBloodGroupName(personal.getBloodGroup().getBlood_group_name());
        }
        if (personal.getFoodType() != null) {
            dto.setFoodTypeId(personal.getFoodType().getFood_type_id());
            dto.setFoodTypeName(personal.getFoodType().getFood_type());
        }
    }

    // --- Orientation Details ---
    StudentOrientationDetails orientation = orientationDetailsRepository
            .findByStudentAcademicDetails(academic).orElse(null);

    if (orientation != null && orientation.getOrientation() != null) {

        dto.setOrientationId(orientation.getOrientation().getOrientationId());
        dto.setOrientationName(orientation.getOrientation().getOrientation_name());

        Integer orientationId = orientation.getOrientation().getOrientationId();
        Integer cmpsId = academic.getCampus() != null ? academic.getCampus().getCampusId() : null;
        Integer classId = academic.getStudentClass() != null ? academic.getStudentClass().getClassId() : null;

        if (orientationId != null && cmpsId != null && classId != null) {
            cmpsOrientationBatchFeeViewRepository
                .findSingleBestBatchDetails(orientationId, cmpsId, classId)
                .ifPresent(feeView -> {
                    dto.setOrientationStartDate(feeView.getOrientationStartDate());
                    dto.setOrientationEndDate(feeView.getOrientationEndDate());
                    dto.setOrientationFee(feeView.getOrientationFee());
                });
        }
    }

    // --- Parents ---
    List<ParentDetails> parents = parentDetailsRepository.findByStudentAcademicDetails(academic);

    for (ParentDetails p : parents) {

        if (p.getStudentRelation().getRelationId() == 1) { // Father
            dto.setFatherName(p.getName());
            dto.setFatherMobile(p.getMobileNo());
            dto.setFatherEmail(p.getEmail());
            dto.setFatherOccupationName(p.getOccupation());


            dto.setFatherOccupationName(p.getOccupation());

            if (p.getSector() != null) {
                dto.setFatherSectorId(p.getSector().getOccupation_sector_id());
                dto.setFatherSectorName(p.getSector().getSector_name());
            }

        } else if (p.getStudentRelation().getRelationId() == 2) { // Mother
            dto.setMotherName(p.getName());
            dto.setMotherMobile(p.getMobileNo());
            dto.setMotherEmail(p.getEmail());
            dto.setMotherOccupationName(p.getOccupation());

            dto.setMotherOccupationName(p.getOccupation());





            if (p.getSector() != null) {
                dto.setMotherSectorId(p.getSector().getOccupation_sector_id());
                dto.setMotherSectorName(p.getSector().getSector_name());
            }
        }
    }

    // --- Address ---
    StudentAddress address = studentAddressRepository.findByStudentAcademicDetails(academic).orElse(null);

    if (address != null) {
        dto.setDoorNo(address.getHouse_no());
        dto.setStreet(address.getStreet());
        dto.setArea(address.getArea());
        dto.setLandmark(address.getLandmark());
        dto.setPincode(address.getPostalCode());

        if (address.getState() != null) {
            dto.setAddressStateId(address.getState().getStateId());
            dto.setAddressStateName(address.getState().getStateName());
        }
        if (address.getDistrict() != null) {
            dto.setAddressDistrictId(address.getDistrict().getDistrictId());
            dto.setAddressDistrictName(address.getDistrict().getDistrictName());
        }
        if (address.getCity() != null) {
            dto.setAddressCityId(address.getCity().getCityId());
            dto.setAddressCityName(address.getCity().getCityName());
        }
        if (address.getMandal() != null) {
            dto.setAddressMandalId(address.getMandal().getMandal_id());
            dto.setAddressMandalName(address.getMandal().getMandal_name());
        }
    }

    // --- Payment (Only required fields) ---
//    PaymentDetails payment = paymentDetailsRepository.findByStudentAcademicDetails(academic).orElse(null);
//
//    if (payment != null) {
//        dto.setPaidAmount(payment.getPaid_amount());
//        dto.setPaymentDate(payment.getApplication_fee_pay_date());
//        dto.setReceiptNo(payment.getPre_print_receipt_no());
//        dto.setRemarks(payment.getRemarks());
//
//        if (payment.getPaymenMode() != null) {
//            dto.setPaymentModeId(payment.getPaymenMode().getPayment_mode_id());
//            dto.setPaymentModeName(payment.getPaymenMode().getPayment_type());
//        }
//
//        StudentApplicationTransaction trans =
//                studentApplicationTransactionRepository.findByPaymentDetails(payment).orElse(null);
//
//        if (trans != null) {
//            dto.setTransactionNumber(trans.getNumber());
//            dto.setTransactionDate(trans.getDate());
//            dto.setIfscCode(trans.getIfsc_code());
//
//            if (trans.getOrgBank() != null) {
//                dto.setBankId(trans.getOrgBank().getOrg_bank_id());
//                dto.setBankName(trans.getOrgBank().getBank_name());
//            }
//            if (trans.getOrgBankBranch() != null) {
//                dto.setBankBranchId(trans.getOrgBankBranch().getOrg_bank_branch_id());
//                dto.setBankBranchName(trans.getOrgBankBranch().getBranch_name());
//            }
//        }
//    }

    // --- Siblings ---
    List<Sibling> siblingList = siblingRepository.findByStudentAcademicDetails(academic);

    for (Sibling s : siblingList) {

        StudentApplicationSingleDTO.SiblingItem item =
                new StudentApplicationSingleDTO.SiblingItem();

        item.setFullName(s.getSibling_name());
        item.setSchoolName(s.getSibling_school());

        if (s.getStudentClass() != null) {
            item.setClassId(s.getStudentClass().getClassId());
            item.setClassName(s.getStudentClass().getClassName());
        }

        if (s.getStudentRelation() != null) {
            item.setRelationId(s.getStudentRelation().getRelationId());
            item.setRelationName(s.getStudentRelation().getRelationType());
        }


        dto.getSiblings().add(item);
    }

    // --- Concessions ---
    List<StudentConcessionType> concList =
            concessionRepository.findByStudAdmsId(academic.getStud_adms_id());

    for (StudentConcessionType c : concList) {

        StudentApplicationSingleDTO.ConcessionItem item =
                new StudentApplicationSingleDTO.ConcessionItem();

        item.setAmount(c.getConc_amount());
        item.setComments(c.getComments());

        if (c.getConcessionType() != null) {
            item.setConcessionTypeId(c.getConcessionType().getConcTypeId());
            item.setConcessionTypeName(c.getConcessionType().getConc_type());
        }

        if (c.getConcessionReason() != null) {
            item.setReasonId(c.getConcessionReason().getConc_reason_id());
            item.setReasonName(c.getConcessionReason().getConc_reason());
        }

        dto.getConcessions().add(item);
    }

    return dto;
}


	// ApplicationSale -college - upadte
	@Transactional
	public String updateApplicationSale(Long studAdmsNo, StudentApplicationUpdateDTO formData) {

	    // ============================================================== 
	    // 1. FETCH EXISTING ENTITIES (UPSERT) 
	    // ============================================================== 
	    StudentAcademicDetails academicDetails = studentAcademicDetailsRepository
	            .findByStudAdmsNo(studAdmsNo)
	            .orElseThrow(() -> new EntityNotFoundException("Student not found: " + studAdmsNo));

	    StudentPersonalDetails personalDetails = personalDetailsRepository
	            .findByStudentAcademicDetails(academicDetails)
	            .orElseGet(StudentPersonalDetails::new);

	    StudentOrientationDetails orientationDetails = orientationDetailsRepository
	            .findByStudentAcademicDetails(academicDetails)
	            .orElseGet(StudentOrientationDetails::new);

	    StudentAddress address = studentAddressRepository
	            .findByStudentAcademicDetails(academicDetails)
	            .orElseGet(StudentAddress::new);

	    // ============================================================== 
	    // 2. UPDATE ACADEMIC DETAILS (NULL-SAFE & ZERO-SAFE) 
	    // ============================================================== 

	    if (formData.getAcademicYearId() != null && formData.getAcademicYearId() > 0) {
	        academicYearRepository.findById(formData.getAcademicYearId()).ifPresent(academicDetails::setAcademicYear);
	    }

	    if (formData.getFirstName() != null) {
	        academicDetails.setFirst_name(formData.getFirstName());
	    }

	    if (formData.getLastName() != null) {
	        academicDetails.setLast_name(formData.getLastName());
	    }

	    if (formData.getApaarNo() != null) {
	        academicDetails.setApaar_no(formData.getApaarNo());
	    }

	    if (formData.getAppSaleDate() != null) {
	        academicDetails.setApp_sale_date(formData.getAppSaleDate());
	    }

	    if (formData.getProReceiptNo() != null) {
	        academicDetails.setPro_receipt_no(formData.getProReceiptNo().intValue());
	    }

	    if (formData.getProId() != null && formData.getProId() > 0) {
	        employeeRepository.findById(formData.getProId()).ifPresent(emp -> {
	            academicDetails.setEmployee(emp);
	            academicDetails.setAdmission_referred_by(String.valueOf(emp.getEmp_id()));
	        });
	    }

	    if (formData.getQuotaId() != null && formData.getQuotaId() > 0) {
	        quotaRepository.findById(formData.getQuotaId()).ifPresent(academicDetails::setQuota);
	    }

	    if (formData.getGenderId() != null && formData.getGenderId() > 0) {
	        genderRepository.findById(formData.getGenderId()).ifPresent(academicDetails::setGender);
	    }

	    if (formData.getAppTypeId() != null && formData.getAppTypeId() > 0) {
	        admissionTypeRepository.findById(formData.getAppTypeId()).ifPresent(academicDetails::setAdmissionType);
	    }

	    if (formData.getStudentTypeId() != null && formData.getStudentTypeId() > 0) {
	        studentTypeRepository.findById(formData.getStudentTypeId()).ifPresent(academicDetails::setStudentType);
	    }

	    if (formData.getHallTicketNumber() != null) {
	        academicDetails.setHt_no(formData.getHallTicketNumber());
	    }

	    if (formData.getScoreAppNo() != null) {
	        academicDetails.setScore_app_no(formData.getScoreAppNo());
	    }

	    if (formData.getScoreMarks() != null) {
	        academicDetails.setScore_marks(formData.getScoreMarks());
	    }

	    if (formData.getSchoolName() != null) {
	        academicDetails.setPre_school_name(formData.getSchoolName());
	    }

	    if (formData.getSchoolStateId() != null && formData.getSchoolStateId() > 0) {
	        stateRepository.findById(formData.getSchoolStateId()).ifPresent(academicDetails::setState);
	    }

	    if (formData.getSchoolDistrictId() != null && formData.getSchoolDistrictId() > 0) {
	        districtRepository.findById(formData.getSchoolDistrictId()).ifPresent(academicDetails::setDistrict);
	    }

	    // Class update only if client sends valid id
	    if (formData.getClassId() != null && formData.getClassId() > 0) {
	        classRepository.findById(formData.getClassId()).ifPresent(academicDetails::setStudentClass);
	    }

	    // Branch update only if client sends valid id
	    if (formData.getBranchId() != null && formData.getBranchId() > 0) {
	        campusRepository.findById(formData.getBranchId()).ifPresent(academicDetails::setCampus);
	    }

	    StudentAcademicDetails updatedAcademicDetails = studentAcademicDetailsRepository.save(academicDetails);

	    // ============================================================== 
	    // 3. UPDATE PERSONAL DETAILS (NULL-SAFE & ZERO-SAFE) 
	    // ============================================================== 
	    personalDetails.setStudentAcademicDetails(updatedAcademicDetails);

	    if (formData.getAadharCardNo() != null) {
	        personalDetails.setStud_aadhaar_no(formData.getAadharCardNo());
	    }

	    if (formData.getDob() != null) {
	        personalDetails.setDob(formData.getDob());
	    }

	    if (formData.getCasteId() != null && formData.getCasteId() > 0) {
	        casteRepository.findById(formData.getCasteId()).ifPresent(personalDetails::setCaste);
	    }

	    if (formData.getReligionId() != null && formData.getReligionId() > 0) {
	        religionRepository.findById(formData.getReligionId()).ifPresent(personalDetails::setReligion);
	    }

	    if (formData.getBloodGroupId() != null && formData.getBloodGroupId() > 0) {
	        bloodGroupRepository.findById(formData.getBloodGroupId()).ifPresent(personalDetails::setBloodGroup);
	    }

	    // FOOD TYPE - important: ignore null or zero
	    if (formData.getFoodTypeId() != null && formData.getFoodTypeId() > 0) {
	        foodTypeRepository.findById(formData.getFoodTypeId()).ifPresent(personalDetails::setFoodType);
	    }

	    personalDetailsRepository.save(personalDetails);

	    // ============================================================== 
	    // 4. UPDATE ORIENTATION (NULL-SAFE) 
	    // ============================================================== 
	    orientationDetails.setStudentAcademicDetails(updatedAcademicDetails);
	    if (formData.getOrientationId() != null && formData.getOrientationId() > 0) {
	        orientationRepository.findById(formData.getOrientationId()).ifPresent(orientationDetails::setOrientation);
	    }
	    orientationDetailsRepository.save(orientationDetails);

	    // ============================================================== 
	    // 5. UPDATE PARENT DETAILS (NULL-SAFE UPSERT)
	    // ============================================================== 
	    List<ParentDetails> parents = parentDetailsRepository.findByStudentAcademicDetails(updatedAcademicDetails);

	    ParentDetails father = parents.stream()
	            .filter(p -> p.getStudentRelation() != null && p.getStudentRelation().getRelationId() == 1)
	            .findFirst()
	            .orElseGet(() -> {
	                ParentDetails p = new ParentDetails();
	                p.setStudentAcademicDetails(updatedAcademicDetails);
	                p.setStudentRelation(relationRepository.findById(1).orElse(null));
	                return p;
	            });

	    ParentDetails mother = parents.stream()
	            .filter(p -> p.getStudentRelation() != null && p.getStudentRelation().getRelationId() == 2)
	            .findFirst()
	            .orElseGet(() -> {
	                ParentDetails p = new ParentDetails();
	                p.setStudentAcademicDetails(updatedAcademicDetails);
	                p.setStudentRelation(relationRepository.findById(2).orElse(null));
	                return p;
	            });

	    // Father update
	    if (formData.getFatherName() != null ||
	            formData.getFatherMobileNo() != null ||
	            formData.getFatherEmail() != null ||
	            formData.getFatherSectorId() != null ||
	            formData.getFatherOccupationId() != null) {

	        father.setStudentAcademicDetails(updatedAcademicDetails);
	        if (father.getStudentRelation() == null) {
	            father.setStudentRelation(relationRepository.findById(1)
	                    .orElseThrow(() -> new EntityNotFoundException("Father relation missing")));
	        }

	        if (formData.getFatherName() != null) father.setName(formData.getFatherName());
	        if (formData.getFatherMobileNo() != null) father.setMobileNo(formData.getFatherMobileNo());
	        if (formData.getFatherEmail() != null) father.setEmail(formData.getFatherEmail());

	        if (formData.getFatherSectorId() != null && formData.getFatherSectorId() > 0) {
	            sectorRepository.findById(formData.getFatherSectorId()).ifPresent(father::setSector);
	        }

	        if (formData.getFatherOccupationId() != null && formData.getFatherOccupationId() > 0) {
	            // You currently store occupation as String in ParentDetails.
	            // We fetch occupation entity and store its name into the occupation column.
	            occupationRepository.findById(formData.getFatherOccupationId())
	                    .ifPresent(occupation -> father.setOccupation(occupation.getOccupation_name()));
	        }

	        // Only set created_by if new (optional)
	        if (father.getCreated_by() == 0 && formData.getCreatedBy() != null) {
	            father.setCreated_by(formData.getCreatedBy());
	        }

	        parentDetailsRepository.save(father);
	    }

	    // Mother update
	    if (formData.getMotherName() != null ||
	            formData.getMotherMobileNo() != null ||
	            formData.getMotherEmail() != null ||
	            formData.getMotherSectorId() != null ||
	            formData.getMotherOccupationId() != null) {

	        mother.setStudentAcademicDetails(updatedAcademicDetails);
	        if (mother.getStudentRelation() == null) {
	            mother.setStudentRelation(relationRepository.findById(2)
	                    .orElseThrow(() -> new EntityNotFoundException("Mother relation missing")));
	        }

	        if (formData.getMotherName() != null) mother.setName(formData.getMotherName());
	        if (formData.getMotherMobileNo() != null) mother.setMobileNo(formData.getMotherMobileNo());
	        if (formData.getMotherEmail() != null) mother.setEmail(formData.getMotherEmail());

	        if (formData.getMotherSectorId() != null && formData.getMotherSectorId() > 0) {
	            sectorRepository.findById(formData.getMotherSectorId()).ifPresent(mother::setSector);
	        }

	        if (formData.getMotherOccupationId() != null && formData.getMotherOccupationId() > 0) {
	            occupationRepository.findById(formData.getMotherOccupationId())
	                    .ifPresent(occupation -> mother.setOccupation(occupation.getOccupation_name()));
	        }

	        if (mother.getCreated_by() == 0 && formData.getCreatedBy() != null) {
	            mother.setCreated_by(formData.getCreatedBy());
	        }

	        parentDetailsRepository.save(mother);
	    }

	    // ============================================================== 
	    // 6. UPDATE SIBLINGS 
	    // ============================================================== 
	    if (formData.getSiblings() != null) {
	        Map<String, Sibling> existing = siblingRepository
	                .findByStudentAcademicDetails(updatedAcademicDetails).stream()
	                .filter(s -> s.getSibling_name() != null)
	                .collect(Collectors.toMap(Sibling::getSibling_name, Function.identity(), (a, b) -> a));

	        for (SiblingDTO s : formData.getSiblings()) {
	            Sibling sib = existing.getOrDefault(s.getFullName(), new Sibling());
	            sib.setStudentAcademicDetails(updatedAcademicDetails);

	            if (s.getFullName() != null) sib.setSibling_name(s.getFullName());
	            if (s.getSchoolName() != null) sib.setSibling_school(s.getSchoolName());
	            if (s.getRelationTypeId() != null && s.getRelationTypeId() > 0)
	                relationRepository.findById(s.getRelationTypeId()).ifPresent(sib::setStudentRelation);
	            if (s.getClassId() != null && s.getClassId() > 0)
	                classRepository.findById(s.getClassId()).ifPresent(sib::setStudentClass);
	            if (s.getGenderId() != null && s.getGenderId() > 0)
	                genderRepository.findById(s.getGenderId()).ifPresent(sib::setGender);

	            if (sib.getCreated_by() == 0 && s.getCreatedBy() != null) {
	                sib.setCreated_by(s.getCreatedBy());
	            }

	            siblingRepository.save(sib);
	        }
	    }

	    // ============================================================== 
	    // 7. UPDATE CONCESSIONS 
	    // ============================================================== 
	    if (formData.getConcessions() != null) {
	        Map<Integer, StudentConcessionType> existingMap = concessionRepository
	                .findByStudAdmsId(updatedAcademicDetails.getStud_adms_id())
	                .stream()
	                .filter(c -> c.getConcessionType() != null)
	                .collect(Collectors.toMap(
	                        c -> c.getConcessionType().getConcTypeId(),
	                        Function.identity(),
	                        (a, b) -> a));

	        AcademicYear year = updatedAcademicDetails.getAcademicYear();

	        for (ConcessionConfirmationDTO c : formData.getConcessions()) {

	            StudentConcessionType conc = existingMap.getOrDefault(c.getConcessionTypeId(), new StudentConcessionType());
	            conc.setStudAdmsId(updatedAcademicDetails.getStud_adms_id());
	            conc.setAcademicYear(year);

	            if (c.getConcessionTypeId() != null && c.getConcessionTypeId() > 0)
	                concessionTypeRepository.findById(c.getConcessionTypeId()).ifPresent(conc::setConcessionType);

	            if (c.getConcessionAmount() != null)
	                conc.setConc_amount(c.getConcessionAmount());

	            conc.setComments(c.getComments());

	            if (c.getReasonId() != null && c.getReasonId() > 0)
	                concessionReasonRepository.findById(c.getReasonId()).ifPresent(conc::setConcessionReason);

	            conc.setConc_referred_by(c.getConcReferedBy());
	            conc.setConc_issued_by(c.getGivenById());
	            conc.setConc_authorised_by(c.getAuthorizedById());

	            concessionRepository.save(conc);
	        }
	    }

	    // ============================================================== 
	    // 8. UPDATE ADDRESS 
	    // ============================================================== 
	    if (formData.getAddressDetails() != null) {
	        AddressDetailsDTO ad = formData.getAddressDetails();

	        address.setStudentAcademicDetails(updatedAcademicDetails);

	        if (ad.getDoorNo() != null) address.setHouse_no(ad.getDoorNo());
	        if (ad.getStreet() != null) address.setStreet(ad.getStreet());
	        if (ad.getLandmark() != null) address.setLandmark(ad.getLandmark());
	        if (ad.getArea() != null) address.setArea(ad.getArea());
	        if (ad.getPincode() != null) address.setPostalCode(ad.getPincode());

	        if (ad.getStateId() != null && ad.getStateId() > 0)
	            stateRepository.findById(ad.getStateId()).ifPresent(address::setState);

	        if (ad.getCityId() != null && ad.getCityId() > 0)
	            cityRepository.findById(ad.getCityId()).ifPresent(address::setCity);

	        if (ad.getMandalId() != null && ad.getMandalId() > 0)
	            mandalRepository.findById(ad.getMandalId()).ifPresent(address::setMandal);

	        if (ad.getDistrictId() != null && ad.getDistrictId() > 0)
	            districtRepository.findById(ad.getDistrictId()).ifPresent(address::setDistrict);

	        studentAddressRepository.save(address);
	    }

	    // ================================ 
	    // PAYMENT BLOCK REMOVED AS REQUESTED 
	    // ================================ 

	    return "Application Sale updated successfully for Admission Number: " + studAdmsNo;
	}



	// Application confirmation - college
	@Transactional
	public String confirmCollegeEnrollment(StudentCollegeConfirmationDto formData) {

		// 1. Fetch main Academic Entity
		StudentAcademicDetails academicDetails = studentAcademicDetailsRepository.findByStudAdmsNo(formData.getStudAdmsNo())
	            .orElseThrow(() -> new EntityNotFoundException("Student not found with Admission No: " + formData.getStudAdmsNo()));
	 
		// 2. Update/Set Enrollment Details on StudentAcademicDetails
		// ... (Enrollment update logic remains the same) ...

		// Academic Year
		if (formData.getAcademicYearId() != null)
			academicYearRepository.findById(formData.getAcademicYearId()).ifPresent(academicDetails::setAcademicYear);

		// Joining Class
		if (formData.getJoiningClassId() != null)
			classRepository.findById(formData.getJoiningClassId()).ifPresent(academicDetails::setStudentClass);

		// Branch (Campus)
		if (formData.getBranchId() != null)
			campusRepository.findById(formData.getBranchId()).ifPresent(academicDetails::setCampus);

		// Student Type
		if (formData.getStudentTypeId() != null)
			studentTypeRepository.findById(formData.getStudentTypeId()).ifPresent(academicDetails::setStudentType);

		// City & Course mapping... (Placeholders)

		Status defaultStatus = statusRepository.findById(1)
				.orElseThrow(() -> new EntityNotFoundException("Default Status (ID: 2) not found"));
		academicDetails.setStatus(defaultStatus);

		StudentAcademicDetails savedAcademicDetails = studentAcademicDetailsRepository.save(academicDetails);

		// 3. Save/Update Concession Details (Concession logic remains the same)

		if (formData.getConcessions() != null && !formData.getConcessions().isEmpty()) {
			// ... (Concession UPSERT logic runs here) ...

			Map<Integer, StudentConcessionType> existingConcessionsMap = concessionRepository
					.findByStudAdmsId(savedAcademicDetails.getStud_adms_id()).stream()
					.filter(c -> c.getConcessionType() != null).collect(Collectors.toMap(
							c -> c.getConcessionType().getConcTypeId(), Function.identity(), (first, second) -> first));

			AcademicYear currentYear = savedAcademicDetails.getAcademicYear();
			if (currentYear == null) {
			    if (formData.getAcademicYearId() == null) {
			        throw new IllegalArgumentException("Academic Year must be provided when no existing year is found.");
			    }
			    currentYear = academicYearRepository.findById(formData.getAcademicYearId())
			            .orElseThrow(() -> new EntityNotFoundException("Academic Year not found"));
			}

			for (ConcessionConfirmationDTO concDto : formData.getConcessions()) {
				StudentConcessionType concession = existingConcessionsMap.get(concDto.getConcessionTypeId());

				if (concession == null) {
					concession = new StudentConcessionType();
					concession.setStudAdmsId(savedAcademicDetails.getStud_adms_id());
					concession.setAcademicYear(currentYear);
					concession.setCreated_by(concDto.getCreatedBy());
					concession.setCreated_Date(LocalDateTime.now());

					if (concDto.getConcessionTypeId() != null) {
						concessionTypeRepository.findById(concDto.getConcessionTypeId())
								.ifPresent(concession::setConcessionType);
					}
				}

				concession.setConc_amount(concDto.getConcessionAmount());
				concession.setComments(concDto.getComments());

				if (concDto.getReasonId() != null) {
					concessionReasonRepository.findById(concDto.getReasonId())
							.ifPresent(concession::setConcessionReason);
				}
				concession.setConc_referred_by(concDto.getConcReferedBy());
				if (concDto.getGivenById() != null) {
					concession.setConc_issued_by(concDto.getGivenById());
				}
				if (concDto.getAuthorizedById() != null) {
					concession.setConc_authorised_by(concDto.getAuthorizedById());
				}

				concessionRepository.save(concession);
			}
		}

		// ==============================================================
		// 🔑 PART 4: CREATE THE PAYMENT AND TRANSACTION (New Logic)
		// ==============================================================
		// ==============================================================
		// 🔑 PART 4: ALWAYS CREATE NEW PAYMENT + TRANSACTION
		// ==============================================================
		PaymentDetailsDTO paymentDTO = formData.getPaymentDetails();

		if (paymentDTO != null && paymentDTO.getAmount() != null) {

			// 🟢 Always create NEW payment record
			PaymentDetails paymentDetails = new PaymentDetails();
			paymentDetails.setStudentAcademicDetails(savedAcademicDetails);
			paymentDetails.setApplication_fee_pay_date(paymentDTO.getPaymentDate());
			paymentDetails.setPre_print_receipt_no(paymentDTO.getPrePrintedReceiptNo());
			paymentDetails.setRemarks(paymentDTO.getRemarks());
			paymentDetails.setCreated_by(paymentDTO.getCreatedBy());
			paymentDetails.setApp_fee(paymentDTO.getAmount());
			paymentDetails.setPaid_amount(paymentDTO.getAmount());
			paymentDetails.setAcedemicYear(savedAcademicDetails.getAcademicYear());
			paymentDetails.setStudentClass(savedAcademicDetails.getStudentClass());

			// Set PaymentMode
			if (paymentDTO.getPaymentModeId() != null) {
				paymentModeRepository.findById(paymentDTO.getPaymentModeId()).ifPresent(paymentDetails::setPaymenMode);
			}

			paymentDetails.setStatus(defaultStatus);
			// 💾 SAVE NEW Payment Record
			PaymentDetails savedPaymentDetails = paymentDetailsRepository.save(paymentDetails);

			// ==============================================================
			// 🔄 TRANSACTION (Only for DD / Cheque — Always NEW Transaction)
			// ==============================================================
			Integer paymentModeId = paymentDTO.getPaymentModeId();
			final int DD_PAYMENT_ID = 2;
			final int CHEQUE_PAYMENT_ID = 3;

			if (paymentModeId != null && (paymentModeId == DD_PAYMENT_ID || paymentModeId == CHEQUE_PAYMENT_ID)) {

				StudentApplicationTransaction transaction = new StudentApplicationTransaction(); // 🔥 Always NEW

				transaction.setPaymentDetails(savedPaymentDetails);
				transaction.setPaymentMode(savedPaymentDetails.getPaymenMode());
				transaction.setNumber(paymentDTO.getTransactionNumber());
				transaction.setDate(paymentDTO.getTransactionDate());
				transaction.setApplication_fee_pay_date(paymentDTO.getPaymentDate());
				transaction.setCreated_by(paymentDTO.getCreatedBy());
				transaction.setStatus("Pending"); // default

				// DD / Cheque extra fields
				transaction.setIfsc_code(paymentDTO.getIfscCode());
				if (paymentDTO.getOrganisationId() != null)
					transaction.setOrg_id(paymentDTO.getOrganisationId());
				if (paymentDTO.getBankId() != null)
					orgBankRepository.findById(paymentDTO.getBankId()).ifPresent(transaction::setOrgBank);
				if (paymentDTO.getBranchId() != null)
					orgBankBranchRepository.findById(paymentDTO.getBranchId()).ifPresent(transaction::setOrgBankBranch);
				if (paymentDTO.getCityId() != null)
					cityRepository.findById(paymentDTO.getCityId()).ifPresent(transaction::setCity);

				// 💾 SAVE NEW Transaction
				studentApplicationTransactionRepository.save(transaction);
			}
		}

		return "College enrollment confirmed, concessions and payment details updated for student: "
				+ formData.getStudAdmsNo();
	}
}