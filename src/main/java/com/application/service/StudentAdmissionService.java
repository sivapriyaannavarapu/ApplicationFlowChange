package com.application.service;
 
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.dto.AddressDetailsDTO;
import com.application.dto.AddressDetailsNewDTO;
import com.application.dto.ApplicationDetailsDTO;
import com.application.dto.BankDetailsDTO;
import com.application.dto.CampusAndZoneDTO;
import com.application.dto.CampusDetailsDTO;
import com.application.dto.ClassDTO;
import com.application.dto.ConcessionTypeDTO;
import com.application.dto.CourseFeeDTO;
import com.application.dto.GenericDropdownDTO;
import com.application.dto.OrientationBatchDetailsDTO;
import com.application.dto.OrientationDTO;
import com.application.dto.OrientationResponseDTO;
import com.application.dto.ParentSummaryDTO;
import com.application.dto.PaymentDetailsDTO;
import com.application.dto.PinCodeLocationDTO;
import com.application.dto.StudentAdmissionDTO;
import com.application.dto.StudentSaleDTO;
import com.application.entity.AcademicYear;
import com.application.entity.AdminApp;
import com.application.entity.AppStatusTrackView;
import com.application.entity.BalanceTrack;
import com.application.entity.BusinessType;
import com.application.entity.Campus;
import com.application.entity.CampusDetails;
import com.application.entity.CmpsOrientation;
import com.application.entity.CmpsOrientationBatchFeeView;
import com.application.entity.Distribution;
import com.application.entity.Employee;
import com.application.entity.OrganizationBankDetails;
import com.application.entity.ParentDetails;
import com.application.entity.PaymentDetails;
import com.application.entity.Status;
import com.application.entity.StudentAcademicDetails;
import com.application.entity.StudentAddress;
import com.application.entity.StudentApplicationTransaction;
import com.application.entity.StudentClass;
import com.application.entity.StudentOrientationDetails;
import com.application.entity.StudentPersonalDetails;
import com.application.entity.StudentRelation;
import com.application.entity.StudyType;
import com.application.entity.Zone;
import com.application.repository.AcademicYearRepository;
import com.application.repository.AdminAppRepository;
import com.application.repository.AdmissionTypeRepository;
import com.application.repository.AppStatusTrackViewRepository;
import com.application.repository.BalanceTrackRepository;
import com.application.repository.BloodGroupRepository;
import com.application.repository.CampusDetailsRepository;
import com.application.repository.CampusRepository;
import com.application.repository.CampusSchoolTypeRepository;
import com.application.repository.CasteRepository;
import com.application.repository.CityRepository;
import com.application.repository.CmpsOrientationBatchFeeViewRepository;
import com.application.repository.CmpsOrientationProgramViewRepository;
import com.application.repository.CmpsOrientationRepository;
import com.application.repository.CmpsOrientationStreamViewRepository;
import com.application.repository.ConcessionReasonRepository;
import com.application.repository.ConcessionTypeRepository;
import com.application.repository.DistributionRepository;
import com.application.repository.DistrictRepository;
import com.application.repository.EmployeeRepository;
import com.application.repository.ExamProgramRepository;
import com.application.repository.GenderRepository;
import com.application.repository.MandalRepository;
import com.application.repository.OrgBankBranchRepository;
import com.application.repository.OrgBankRepository;
import com.application.repository.OrganizationBankDetailsRepository;
import com.application.repository.OrganizationRepository;
import com.application.repository.OrientationBatchRepository;
import com.application.repository.OrientationRepository;
import com.application.repository.ParentDetailsRepository;
import com.application.repository.PaymentDetailsRepository;
import com.application.repository.PaymentModeRepository;
import com.application.repository.PinCodeRepository;
import com.application.repository.ProConcessionRepository;
import com.application.repository.ProgramNameRepository;
import com.application.repository.QuotaRepository;
import com.application.repository.ReligionRepository;
import com.application.repository.SchoolDetailsRepository;
import com.application.repository.SectionRepository;
import com.application.repository.SiblingRepository;
import com.application.repository.StateAppRepository;
import com.application.repository.StateRepository;
import com.application.repository.StatusRepository;
import com.application.repository.StreamRepository;
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
import com.application.repository.ZoneRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StudentAdmissionService {

    private final CampusDetailsRepository campusDetailsRepository;
 
    private static final Logger logger = LoggerFactory.getLogger(StudentAdmissionService.class);
 
    // region Repositories
    @Autowired private StudentAcademicDetailsRepository academicDetailsRepo;
    @Autowired private StudentPersonalDetailsRepository personalDetailsRepo;
    @Autowired private StudentAddressRepository addressRepo;
    @Autowired private SiblingRepository siblingRepo;
    @Autowired private AdmissionTypeRepository admissionTypeRepo;
    @Autowired private StudentTypeRepository studentTypeRepo;
    @Autowired private StudyTypeRepository studyTypeRepo;
    @Autowired private GenderRepository genderRepo;
    @Autowired private CampusRepository campusRepo;
    @Autowired private StateRepository stateRepo;
    @Autowired private DistrictRepository districtRepo;
    @Autowired private CampusSchoolTypeRepository schoolTypeRepo;
    @Autowired private QuotaRepository quotaRepo;
    @Autowired private StudentRelationRepository relationTypeRepo;
    @Autowired private StudentClassRepository classRepo;
    @Autowired private OrientationRepository orientationRepo;
    @Autowired private MandalRepository mandalRepo;
    @Autowired private CityRepository cityRepo;
    @Autowired private AcademicYearRepository academicYearRepository;
    @Autowired private SectionRepository sectionRepo;
    @Autowired private StatusRepository statusRepo;
    @Autowired private EmployeeRepository employeeRepo;
    @Autowired private PaymentDetailsRepository paymentDetailsRepo;
    @Autowired private PaymentModeRepository paymentModeRepo;
    @Autowired private StudentConcessionTypeRepository studentConcessionTypeRepo;
    @Autowired private ConcessionTypeRepository concessionTypeRepo;
    @Autowired private ConcessionReasonRepository concessionReasonRepo;
    @Autowired private StudentApplicationTransactionRepository appTransactionRepo;
    @Autowired private OrganizationRepository orgRepo;
    @Autowired private OrgBankRepository orgBankRepo;
    @Autowired private OrgBankBranchRepository orgBankBranchRepo;
    @Autowired private SchoolDetailsRepository schoolDetailsRepo;
    @Autowired private ReligionRepository religionRepo;
    @Autowired private CasteRepository casteRepo;
    @Autowired private ParentDetailsRepository parentDetailsRepo;
    @Autowired private ProConcessionRepository proConcessionRepo;
    @Autowired private StudentOrientationDetailsRepository studentOrientationDetailsRepo;
    @Autowired private OrientationBatchRepository orientationBatchRepo;
    @Autowired private CmpsOrientationRepository cmpsOrientationRepo;
    @Autowired private CmpsOrientationStreamViewRepository cmpsOrientationStreamViewRepo;
    @Autowired private CmpsOrientationProgramViewRepository cmpsOrientationProgramViewRepo;
    @Autowired private CmpsOrientationBatchFeeViewRepository cmpsOrientationBatchFeeViewRepo;
    @Autowired private BloodGroupRepository bloodGroupRepo;
    @Autowired private StreamRepository streamRepo;
    @Autowired private ProgramNameRepository programNameRepo;
    @Autowired private ExamProgramRepository examProgramRepo;
    @Autowired private CmpsOrientationBatchFeeViewRepository repository;
    @Autowired private BloodGroupRepository bloodGroupRepository;
    @Autowired private OrganizationRepository organizationRepo;
    @Autowired private OrganizationBankDetailsRepository orgBankDetailsRepo;
    @Autowired private CampusSchoolTypeRepository campusSchoolTypeReposirtory;
    @Autowired private StudentRelationRepository studentRelationRepo;
    @Autowired private DistributionRepository distributionRepo; // Injected the new repository
    @Autowired private PinCodeRepository pinCodeRepository;
    @Autowired private StateAppRepository stateAppRepository;
    @Autowired private DistributionRepository distributionRepository;
    @Autowired private StudentOrientationDetailsRepository orientationDetailsRepo;
    @Autowired private AppStatusTrackViewRepository appStatusTrackViewRepository;
    @Autowired private ZoneRepository zonerepo;
    @Autowired private BalanceTrackRepository balanceTrackRepository;
    @Autowired private StudentApplicationTransactionRepository studentApplicationTransactionRepo;
    @Autowired private MandalRepository mandalRepository;
    @Autowired private AdminAppRepository adminAppRepository;


    StudentAdmissionService(CampusDetailsRepository campusDetailsRepository) {
        this.campusDetailsRepository = campusDetailsRepository;
    }
 
 
//    @Cacheable("religions")
    public List<GenericDropdownDTO> getAllReligions() {
        return religionRepo.findAll().stream()
                .map(r -> new GenericDropdownDTO(r.getReligion_id(), r.getReligion_type()))
                .collect(Collectors.toList());
    }
 
//    @Cacheable("castes")
    public List<GenericDropdownDTO> getAllCastes() {
        return casteRepo.findAll().stream()
                .map(c -> new GenericDropdownDTO(c.getCaste_id(), c.getCaste_type()))
                .collect(Collectors.toList());
    }
    
//    @Cacheable("admissionTypes")
    public List<GenericDropdownDTO> getAllAdmissionTypes() {
        return admissionTypeRepo.findAll().stream()
                .map(t -> new GenericDropdownDTO(t.getAdms_type_id(), t.getAdms_type_name()))
                .collect(Collectors.toList());
    }
 
//    @Cacheable("genders")
    public List<GenericDropdownDTO> getAllGenders() {
        return genderRepo.findAll().stream().map(g -> new GenericDropdownDTO(g.getGender_id(), g.getGenderName()))
                .collect(Collectors.toList());
    }
 
//    @Cacheable("campuses")
    public List<GenericDropdownDTO> getAllCampuses() {
        return campusRepo.findAll().stream().map(c -> new GenericDropdownDTO(c.getCampusId(), c.getCampusName()))
                .collect(Collectors.toList());
    }
    
//    @Cacheable("studentClasses")
    public List<GenericDropdownDTO> getAllStudentclass() {
        return classRepo.findAll().stream()
                .map(studentClass -> new GenericDropdownDTO(studentClass.getClassId(), studentClass.getClassName()))
                .collect(Collectors.toList());
    }
    
//    @Cacheable(value = "studyTypes", key = "#id")
    public StudyType getStudyTypeById(int id) {
        Optional<StudyType> studyTypeOptional = studyTypeRepo.findById(id);
        return studyTypeOptional.orElse(null);
    }
    
//    @Cacheable("quotas")
    public List<GenericDropdownDTO> getAllQuotas() {
        return quotaRepo.findAll().stream()
                .map(quota -> new GenericDropdownDTO(quota.getQuota_id(), quota.getQuota_name()))
                .collect(Collectors.toList());
    }
    
//    @Cacheable("employees")
    public List<GenericDropdownDTO> getAllEmployees() {
        // 1. Call the repository method to fetch only employees where is_active is 1
        List<Employee> activeEmployees = employeeRepo.findByIsActive(1); 
        
        // 2. Stream the filtered list and map to DTOs
        return activeEmployees.stream()
                .map(employee -> new GenericDropdownDTO(
                    employee.getEmp_id(), 
                    employee.getFirst_name() + " " + employee.getLast_name()
                ))
                .collect(Collectors.toList());
    }
    
//    @Cacheable("schoolTypes")
    public List<GenericDropdownDTO> getAllSchoolTypes()
    {         return campusSchoolTypeReposirtory.findAll().stream()            
    		.map(schoolType -> new GenericDropdownDTO(schoolType.getSchool_type_id(), schoolType.getSchool_type_name())) .collect(Collectors.toList()); }
    
//    @Cacheable("concessionReasons")
    public List<GenericDropdownDTO> getAllConcessionReasons() {
        return concessionReasonRepo.findAll().stream()
                .map(reason -> new GenericDropdownDTO(reason.getConc_reason_id(), reason.getConc_reason()))
                .collect(Collectors.toList());
    }
    
//    @Cacheable("bloodGroups")
    public List<GenericDropdownDTO> getAllBloodGroups() {
        return bloodGroupRepository.findAll().stream()
                .map(group -> new GenericDropdownDTO(group.getBlood_group_id(), group.getBlood_group_name()))
                .collect(Collectors.toList());
    }
    
//    @Cacheable("paymentModes")
    public List<GenericDropdownDTO> getAllPaymentModes() {
        return paymentModeRepo.findAll().stream()
                .map(mode -> new GenericDropdownDTO(mode.getPayment_mode_id(), mode.getPayment_type()))
                .collect(Collectors.toList());
    }
    
//    @Cacheable(value = "orientationsByClass", key = "#classId")
    public List<OrientationDTO> getOrientationsByClassId(int classId) {
        return cmpsOrientationBatchFeeViewRepo.findOrientationsByClassId(classId);
    }
    
//    @Cacheable(value = "classesByCampus", key = "#campusId")
    public List<ClassDTO> getClassesByCampusId(int campusId) {
        return cmpsOrientationBatchFeeViewRepo.findClassesByCampusId(campusId);
    }
    
    public List<OrientationDTO> getDistinctActiveOrientationsByClassIdAndCmpsId(int classId, int cmpsId) {
        return cmpsOrientationBatchFeeViewRepo.findDistinctOrientationsByClassIdAndCmpsId(classId, cmpsId);
    }
    
//    @Cacheable("studentTypes")
    public List<GenericDropdownDTO> getAllStudentTypes() {
        return studentTypeRepo.findAll().stream()
                .map(t -> new GenericDropdownDTO(t.getStud_type_id(), t.getStud_type())).collect(Collectors.toList());
    }
 
//    @Cacheable("studentRelations")
    public List<GenericDropdownDTO> getAllStudentRelations()
    {         return studentRelationRepo.findAll().stream().map(relation -> new GenericDropdownDTO(relation.getStudentRelationId(), relation.getStudentRelationType())).collect(Collectors.toList()); }
    
    
//    @Cacheable(value = "studyTypesByCampusAndClass", key = "{#cmpsId, #classId}")
    public List<GenericDropdownDTO> getStudyTypesByCampusAndClass(int cmpsId, int classId) {
        return cmpsOrientationBatchFeeViewRepo.findDistinctStudyTypesByCmpsIdAndClassId(cmpsId, classId);
    }
 
//    @Cacheable(value = "orientationsByCampusClassStudyType", key = "{#cmpsId, #classId, #studyTypeId}")
    public List<GenericDropdownDTO> getOrientationsByCampusClassAndStudyType(int cmpsId, int classId, int studyTypeId) {
        return cmpsOrientationBatchFeeViewRepo.findDistinctOrientationsByCmpsIdAndClassIdAndStudyTypeId(cmpsId, classId, studyTypeId);
    }
 
//    @Cacheable(value = "orientationBatchesByCriteria", key = "{#cmpsId, #classId, #studyTypeId, #orientationId}")
    public List<GenericDropdownDTO> getOrientationBatchesByAllCriteria(int cmpsId, int classId, int studyTypeId, int orientationId) {
        return cmpsOrientationBatchFeeViewRepo.findDistinctOrientationBatchesByCmpsIdAndClassIdAndStudyTypeIdAndOrientationId(cmpsId, classId, studyTypeId, orientationId);
    }
 
//    @Cacheable(value = "batchDetails", key = "{#cmpsId, #classId, #studyTypeId, #orientationId, #orientationBatchId}")
    public Optional<OrientationBatchDetailsDTO> getBatchDetails(int cmpsId, int classId, int studyTypeId, int orientationId, int orientationBatchId) {
        return cmpsOrientationBatchFeeViewRepo.findBatchDetailsByAllCriteria(cmpsId, classId, studyTypeId, orientationId, orientationBatchId);
    }
 
//    @Cacheable(value = "orientationsByCampus", key = "#campusId")
    public List<OrientationResponseDTO> getOrientationsByCampus(int campusId) {
        List<CmpsOrientationBatchFeeView> orientations = cmpsOrientationBatchFeeViewRepo.findByCmpsId(campusId);
        return orientations.stream()
                .filter(entity -> entity != null)
                .map(this::convertToOrientationResponseDto)
                .collect(Collectors.toList());
    }
 
    private OrientationResponseDTO convertToOrientationResponseDto(CmpsOrientationBatchFeeView entity) {
        return new OrientationResponseDTO(
            entity.getCmpsId(),
            entity.getCmpsName(),
            entity.getOrientationId(),
            entity.getOrientationName(),
            entity.getOrientationBatchId(),
            entity.getOrientationBatchName(),
            entity.getOrientationStartDate(),
            entity.getOrientationEndDate(),
            entity.getOrientationFee(),
            entity.getSectionName(),
            entity.getSectionId()
        );
    }
    
//    @Cacheable(value = "districtsByState", key = "#stateId")
    public List<GenericDropdownDTO> getDistrictsByState(int stateId) {
        return districtRepo.findByStateStateId(stateId).stream()
                .map(d -> new GenericDropdownDTO(d.getDistrictId(), d.getDistrictName()))
                .collect(Collectors.toList());
    }
 
//    @Cacheable(value = "mandalsByDistrict", key = "#districtId")
    public List<GenericDropdownDTO> getMandalsByDistrict(int districtId) {
        return mandalRepo.findByDistrictDistrictId(districtId).stream()
                .map(m -> new GenericDropdownDTO(m.getMandal_id(), m.getMandal_name()))
                .collect(Collectors.toList());
    }
    
//    @Cacheable(value = "citiesByDistrict", key = "#districtId")
    public List<GenericDropdownDTO> getCitiesByDistrict(int districtId) {
    	
    	final int ACTIVE_STATUS = 1;
        return cityRepo.findByDistrictDistrictIdAndStatus(districtId,ACTIVE_STATUS).stream()
                .map(c -> new GenericDropdownDTO(c.getCityId(), c.getCityName()))
                .collect(Collectors.toList());
    }
 
//    @Cacheable(value = "orientationFee", key = "{#campusId, #orientationId}")
    public CourseFeeDTO getOrientationFee(int campusId, int orientationId) {
        List<CmpsOrientation> orientations = cmpsOrientationRepo
                .findByCmpsIdAndOrientationOrientationId(campusId, orientationId);
 
        if (orientations.isEmpty()) {
            throw new EntityNotFoundException(
                "Fee not found for Campus ID: " + campusId + " and Orientation ID: " + orientationId
            );
        }
        float fee = orientations.get(0).getOrientation_fee();
        return new CourseFeeDTO(fee);
    }
    
//    @Cacheable("organizations")
    public List<GenericDropdownDTO> getAllOrganizations() {
        return organizationRepo.findAll().stream()
                .map(org -> new GenericDropdownDTO(org.getOrgId(), org.getOrg_name()))
                .collect(Collectors.toList());
    }
 
//    @Cacheable(value = "banksByOrganization", key = "#orgId")
    public List<GenericDropdownDTO> getBanksByOrganization(int orgId) {
        return orgBankDetailsRepo.findDistinctBanksByOrganizationId(orgId).stream()
                .map(bank -> new GenericDropdownDTO(bank.getOrg_bank_id(), bank.getBank_name()))
                .collect(Collectors.toList());
    }
    
//    @Cacheable(value = "bankDetails", key = "{#orgId, #bankId, #branchId}")
    public BankDetailsDTO getBankDetails(int orgId, int bankId, int branchId) {
        List<OrganizationBankDetails> detailsList = orgBankDetailsRepo.findDetailsByAllIds(orgId, bankId, branchId);
 
        if (detailsList.isEmpty()) {
            throw new EntityNotFoundException("Details not found for the given combination");
        }
        
        OrganizationBankDetails details = detailsList.get(0);
        
        return new BankDetailsDTO(details.getIfsc_code());
    }
 
//    @Cacheable(value = "branchesByOrgAndBank", key = "{#orgId, #bankId}")
    public List<GenericDropdownDTO> getBranchesByOrganizationAndBank(int orgId, int bankId) {
        return orgBankDetailsRepo.findDistinctBranchesByOrganizationAndBankId(orgId, bankId).stream()
                .map(branch -> new GenericDropdownDTO(branch.getOrg_bank_branch_id(), branch.getBranch_name()))
                .collect(Collectors.toList());
    }
    
    public PinCodeLocationDTO getLocationByPinCode(int pinCode) {
        return pinCodeRepository.findStateAndDistrictByPinCode(pinCode)
                .orElseThrow(() -> new RuntimeException("No data found for pin code: " + pinCode));
    }
    
    public CampusDetailsDTO getCampusDetails(int campusId) {
        return campusRepo.findCampusDetailsById(campusId)
                .orElseThrow(() -> new RuntimeException("Campus not found for ID: " + campusId));
    }
    
    @Transactional(readOnly = true)
public CampusAndZoneDTO getApplicationDetailsWithFee(long applicationNo) {
    logger.info("Fetching details with fee for Application No: {}", applicationNo);

    // 1. Fetch from AppStatusTrackView
    AppStatusTrackView statusTrack = appStatusTrackViewRepository.findByNum(applicationNo)
            .orElseThrow(() -> new EntityNotFoundException("Application status not found for No: " + applicationNo));

    Integer campusId = statusTrack.getCmps_id();
    String campusName = statusTrack.getCmps_name();
    String zoneName = statusTrack.getZone_name();

    if (campusId == null) {
        logger.error("Campus ID is null in status track for App No: {}", applicationNo);
        throw new EntityNotFoundException("Campus ID not found in status track for App No: " + applicationNo);
    }
    logger.debug("Found Status Track: CampusId={}, CampusName='{}', ZoneName='{}'", campusId, campusName, zoneName);

    // 2. Fetch Zone ID using Zone Name
    Integer zoneId = getZoneIdByName(zoneName);

    // 3. Fetch Campus to get Business Type
    Campus campus = campusRepo.findById(campusId)
            .orElseThrow(() -> new EntityNotFoundException("Campus entity not found for ID: " + campusId));

    BusinessType businessType = campus.getBusinessType();
    if (businessType == null) {
        logger.error("Business Type is not linked for Campus ID: {}", campusId);
        throw new EntityNotFoundException("Business Type not linked for Campus ID: " + campusId);
    }
    String businessTypeName = businessType.getBusinessTypeName();
    logger.debug("Found Campus: BusinessType='{}'", businessTypeName);

    // 4. Fetch Academic Year from BalanceTrack
    logger.debug("Fetching Academic Year from BalanceTrack for App No: {}", applicationNo);
    BalanceTrack balanceTrack = balanceTrackRepository.findActiveBalanceTrackByAppNoRange(applicationNo)
            .orElseThrow(() -> new EntityNotFoundException(
                    "BalanceTrack record covering Application No: " + applicationNo + " not found. Cannot determine Academic Year."));

    AcademicYear academicYear = balanceTrack.getAcademicYear();
    if (academicYear == null) {
        logger.error("Academic Year is null in balance track record for App No: {}", applicationNo);
        throw new EntityNotFoundException("Academic Year not linked for BalanceTrack covering App No: " + applicationNo);
    }
    Integer academicYearId = academicYear.getAcdcYearId();
    String academicYearString = academicYear.getAcademicYear();
    logger.debug("Found BalanceTrack: AcademicYearId={}, AcademicYear='{}'", academicYearId, academicYearString);

    // 5. Determine Fees based on Business Type
    Float applicationFee = null;
    Float amount = null;  // This will hold AdminApp.app_amount for COLLEGE

    if ("SCHOOL".equalsIgnoreCase(businessTypeName)) {
        // SCHOOL: Fee comes from CampusDetails.app_fee
        logger.debug("Business type is SCHOOL. Fetching fee from CampusDetails...");
        Optional<CampusDetails> campusDetailsOpt = campusDetailsRepository
                .findByCampusCampusIdAndAcademicYearAcdcYearId(campusId, academicYearId);

        if (campusDetailsOpt.isPresent()) {
            applicationFee = campusDetailsOpt.get().getApp_fee();
            logger.debug("SCHOOL Application Fee from CampusDetails: {}", applicationFee);
        } else {
            logger.warn("CampusDetails not found for CampusId={}, AcademicYearId={}. Application fee will be null.", 
                    campusId, academicYearId);
        }

    } else if ("COLLEGE".equalsIgnoreCase(businessTypeName)) {
        // COLLEGE: Fees now come from AdminApp table
        logger.debug("Business type is COLLEGE. Fetching fees from AdminApp for AppNo={} and AcademicYearId={}", 
                applicationNo, academicYearId);

        Optional<AdminApp> adminAppOpt = adminAppRepository
                .findActiveAdminAppByAppNoAndAcademicYear(applicationNo, academicYearId);

        if (adminAppOpt.isPresent()) {
            AdminApp adminApp = adminAppOpt.get();

            // Map app_amount (int) → Float for DTO.amount
            Integer appAmountInt = adminApp.getApp_amount();
            amount = (appAmountInt != null) ? appAmountInt.floatValue() : null;

            // Map app_fee (Integer) → Float for DTO.applicationFee
            Integer appFeeInt = adminApp.getApp_fee();
            applicationFee = (appFeeInt != null) ? appFeeInt.floatValue() : null;

            logger.debug("COLLEGE Fees from AdminApp → amount: {}, applicationFee: {}", amount, applicationFee);
        } else {
            logger.warn("No active AdminApp record found for Application No: {} and AcademicYearId: {}. Both fees will be null.", 
                    applicationNo, academicYearId);
            amount = null;
            applicationFee = null;
        }

    } else {
        logger.warn("Unhandled Business Type '{}' for Campus ID: {}. Fees will remain null.", businessTypeName, campusId);
    }

    // 6. Build and return DTO
    CampusAndZoneDTO resultDTO = new CampusAndZoneDTO();
    resultDTO.setCampusId(campusId);
    resultDTO.setCampusName(campusName);
    resultDTO.setZoneId(zoneId);
    resultDTO.setZoneName(zoneName);
    resultDTO.setAcademicYearId(academicYearId);
    resultDTO.setAcademicYear(academicYearString);
    resultDTO.setApplicationFee(applicationFee);
    resultDTO.setAmount(amount);  // This now correctly comes from AdminApp.app_amount for COLLEGE

    logger.info("Successfully fetched details with fee for Application No: {}", applicationNo);
    return resultDTO;
}
	// --- Helper method to cache Zone lookup by name (No changes needed here) ---
 
	public Integer getZoneIdByName(String zoneName) {
		if (zoneName == null || zoneName.isBlank()) {
			logger.debug("Zone name provided is blank or null.");
			return null;
		}
		logger.debug("Looking up Zone ID for name: '{}'", zoneName);
		Optional<Zone> zoneOpt = zonerepo.findByZoneNameIgnoreCase(zoneName); // Uses updated repo method
 
		if (zoneOpt.isEmpty()) {
			logger.warn("Zone entity not found for name: {}", zoneName);
			return null;
		} else {
			logger.debug("Found Zone ID: {}", zoneOpt.get().getZoneId());
			return zoneOpt.get().getZoneId();
		}
	}
 
    public List<ConcessionTypeDTO> getConcessionTypesByNames(List<String> concTypes) {
        return concessionTypeRepo.findConcessionTypesByNames(concTypes);
    }

    
    @Transactional
	public StudentAcademicDetails createAdmissionAndSale(StudentAdmissionDTO formData) {
 
		// ==============================================================
		// PART 1: CREATE THE STUDENT
		// ==============================================================
    	
    	Long admissionNumberNumeric = formData.getStudAdmsNo();
 
    	if (admissionNumberNumeric == null) {
    	    throw new IllegalArgumentException("Admission Number must be provided.");
    	}
 
    	Distribution distribution = distributionRepo.findProDistributionForAdmissionNumber(admissionNumberNumeric)
    	        .orElseThrow(() -> new EntityNotFoundException(
    	            "No PRO has been assigned for Admission Number: " + admissionNumberNumeric
    	        ));
 
    	Employee pro = distribution.getIssuedToEmployee();
    	if (pro == null) {
    	     throw new EntityNotFoundException("A PRO has not been linked to the distribution for Admission Number: " + admissionNumberNumeric);
    	}
		// --- 1. Save Academic Details ---
		StudentAcademicDetails academicDetails = new StudentAcademicDetails();
		
		academicDetails.setIs_active(1);
 
		// ... (all the code from Part 1 is correct) ...
 
		if (formData.getAcademicYearId() != null) {
			academicYearRepository.findById(formData.getAcademicYearId()).ifPresent(academicDetails::setAcademicYear);
		}
 
		academicDetails.setStudAdmsNo(formData.getStudAdmsNo());
		if (formData.getProReceiptNo() != null) {
			academicDetails.setPro_receipt_no(formData.getProReceiptNo().intValue());
		}
		academicDetails.setFirst_name(formData.getFirstName());
		academicDetails.setLast_name(formData.getLastName());
		academicDetails.setApaar_no(formData.getApaarNo());
		academicDetails.setAdms_date(LocalDate.now());
		academicDetails.setApp_sale_date(formData.getAppSaleDate());
		academicDetails.setIs_active(1);
 
		
		if (formData.getProId() != null) {
		    // Convert the Integer Employee ID from the DTO to a String
		    String referredByEmployeeId = String.valueOf(formData.getProId());
		    
		    // Set the String value in the admission_referred_by column
		    academicDetails.setAdmission_referred_by(referredByEmployeeId);
		}
 
		if (formData.getGenderId() != null)
			genderRepo.findById(formData.getGenderId()).ifPresent(academicDetails::setGender);
		if (formData.getAppTypeId() != null)
			admissionTypeRepo.findById(formData.getAppTypeId()).ifPresent(academicDetails::setAdmissionType);
		if (formData.getStudentTypeId() != null)
			studentTypeRepo.findById(formData.getStudentTypeId()).ifPresent(academicDetails::setStudentType);
		if (formData.getQuotaId() != null)
			quotaRepo.findById(formData.getQuotaId()).ifPresent(academicDetails::setQuota);
//		if (formData.getBranchTypeId() != null) {
//			schoolTypeRepo.findById(formData.getBranchTypeId()).ifPresent(academicDetails::setCampusSchoolType);
//		}
		
		StudentClass studentClass = classRepo.findById(formData.getClassId())
				.orElseThrow(() -> new EntityNotFoundException("Invalid Class ID: " + formData.getClassId()));
		academicDetails.setStudentClass(studentClass);
 
		Campus campus = campusRepo.findById(formData.getBranchId())
				.orElseThrow(() -> new EntityNotFoundException("Invalid Branch ID: " + formData.getBranchId()));
		academicDetails.setCampus(campus);
 
		if (formData.getProId() != null && formData.getProId() > 0) {
			employeeRepo.findById(formData.getProId()).ifPresent(academicDetails::setEmployee);
		}
 
		academicDetails.setCreated_by(formData.getCreatedBy());
		academicDetails.setEmployee(pro);
 
		// --- FIX 1 (from previous step) ---
		StudyType defaultStudyType = studyTypeRepo.findById(1)
				.orElseThrow(() -> new EntityNotFoundException("Default StudyType (ID: 1) not found"));
		academicDetails.setStudyType(defaultStudyType);
 
		Status defaultStatus = statusRepo.findById(2)
				.orElseThrow(() -> new EntityNotFoundException("Default Status (ID: 1) not found"));
		academicDetails.setStatus(defaultStatus);
 
		StudentAcademicDetails savedAcademicDetails = academicDetailsRepo.save(academicDetails);
 
		// --- 2. Save Personal Details ---
		StudentPersonalDetails personalDetails = new StudentPersonalDetails();
		personalDetails.setStudentAcademicDetails(savedAcademicDetails);
		personalDetails.setStud_aadhaar_no(formData.getAadharCardNo());
		personalDetails.setDob(formData.getDob());
		personalDetails.setCreated_by(formData.getCreatedBy());
 
		// these setted to 1 again these will be updated in the confirmation form
		personalDetails.setCaste_id(3); // Or use personalDetails.setCaste_id(1) if you don't have the mapping
		personalDetails.setReligion_id(3); // Or use personalDetails.setReligion_id(1)
 
		// TODO: Check if blood_group_id is also NOT NULL and add a default if needed
		// BloodGroup defaultBloodGroup = bloodGroupRepo.findById(1)
		// .orElseThrow(() -> new EntityNotFoundException("Default BloodGroup (ID: 1)
		// not found"));
		// personalDetails.setBloodGroup(defaultBloodGroup);
		// --- END FIX 2 ---
 
		personalDetailsRepo.save(personalDetails);
 
		// --- 3. Save Student Orientation Details ---
		StudentOrientationDetails orientationDetails = new StudentOrientationDetails();
		orientationDetails.setStudentAcademicDetails(savedAcademicDetails);
		if (formData.getOrientationId() != null)
			orientationRepo.findById(formData.getOrientationId()).ifPresent(orientationDetails::setOrientation);
		studentOrientationDetailsRepo.save(orientationDetails);
 
		// --- 4. Save Parent Details ---
		if (formData.getFatherName() != null || formData.getFatherMobileNo() != null) {
 
			StudentRelation fatherRelation = relationTypeRepo.findById(1) // Assuming 1 = Father
					.orElseThrow(() -> new EntityNotFoundException("StudentRelation 'Father' (ID: 1) not found"));
 
			ParentDetails parent = new ParentDetails();
			parent.setStudentAcademicDetails(savedAcademicDetails);
			parent.setName(formData.getFatherName());
			parent.setMobileNo(formData.getFatherMobileNo());
			parent.setCreated_by(formData.getCreatedBy());
			parent.setStudentRelation(fatherRelation);
			// these setted to 1 again these will be updated in the confirmation form
			parent.setOccupation("Not Provided");
			parent.setEmail("not provided");
			
			
			parentDetailsRepo.save(parent);
		}
 
		// --- 5. Save Address Details ---
		if (formData.getAddressDetails() != null) {
            AddressDetailsDTO addressDTO = formData.getAddressDetails();
            StudentAddress address = new StudentAddress();
            address.setStudentAcademicDetails(savedAcademicDetails);
            address.setHouse_no(addressDTO.getDoorNo());
            address.setStreet(addressDTO.getStreet());
            address.setLandmark(addressDTO.getLandmark());
            address.setArea(addressDTO.getArea());
            if (addressDTO.getPincode() != null) address.setPostalCode(addressDTO.getPincode());
            if (addressDTO.getStateId()!= null) stateRepo.findById(addressDTO.getStateId()).ifPresent(address::setState);
            if (addressDTO.getCityId() != null) cityRepo.findById(addressDTO.getCityId()).ifPresent(address::setCity);
            if (addressDTO.getMandalId() != null) mandalRepo.findById(addressDTO.getMandalId()).ifPresent(address::setMandal);
            if (addressDTO.getDistrictId() != null) districtRepo.findById(addressDTO.getDistrictId()).ifPresent(address::setDistrict);
            
            // --- FIX 1: Use the 'createdBy' from the AddressDetailsDTO ---
            address.setCreated_by(addressDTO.getCreatedBy());
            
            // --- FIX 2: SET DEFAULT VALUE FOR 'NOT NULL' STATE FIELD ---
            // TODO: Confirm the default State ID (e.g., 1) from your 'state' table.
//            State defaultState = stateRepo.findById(1) // Assuming 1 = "Not Applicable" or a default state
//                    .orElseThrow(() -> new EntityNotFoundException("Default State (ID: 1) not found"));
//            address.setState(defaultState);
            // --- END FIX ---
            
            addressRepo.save(address);
		}
 
		// ==============================================================
		// PART 2: CREATE THE PAYMENT
		// ==============================================================
 
		PaymentDetailsDTO paymentDTO = formData.getPaymentDetails();
 
		if (paymentDTO != null && paymentDTO.getAmount() != null) {
		    PaymentDetails paymentDetails = new PaymentDetails();
 
		    paymentDetails.setStudentAcademicDetails(savedAcademicDetails);
		    paymentDetails.setApplication_fee_pay_date(paymentDTO.getPaymentDate());
		    paymentDetails.setPre_print_receipt_no(paymentDTO.getPrePrintedReceiptNo());
		    
		    // Use the Remarks from the DTO, which can come from either UI
		    paymentDetails.setRemarks(paymentDTO.getRemarks());
		    
		    paymentDetails.setCreated_by(paymentDTO.getCreatedBy());
		    paymentDetails.setApp_fee(paymentDTO.getAmount());
		    paymentDetails.setPaid_amount(paymentDTO.getAmount());
 
		    paymentDetails.setAcedemicYear(savedAcademicDetails.getAcademicYear());
		    paymentDetails.setStudentClass(savedAcademicDetails.getStudentClass());
 
		    // Set the PaymentMode (e.g., Cash, DD, Cheque)
		    if (paymentDTO.getPaymentModeId() != null) {
		        paymentModeRepo.findById(paymentDTO.getPaymentModeId())
		            .ifPresent(paymentDetails::setPaymenMode);
		    }
 
		    // Use the same default status you found earlier (ID: 2)
		    paymentDetails.setStatus(defaultStatus);
 
		    // 1. SAVE THE MAIN PAYMENT RECORD
		    PaymentDetails savedPaymentDetails = paymentDetailsRepo.save(paymentDetails);
 
		    // 2. CHECK IF A TRANSACTION RECORD IS ALSO NEEDED (for DD or Cheque)
		    Integer paymentModeId = paymentDTO.getPaymentModeId();
 
		    // --- IMPORTANT: VERIFY THESE IDs FROM YOUR 'payment_mode' TABLE ---
		    final int DD_PAYMENT_ID = 2;     // Example ID for 'DD'
		    final int CHEQUE_PAYMENT_ID = 3; // Example ID for 'Cheque'
		    // ------------------------------------------------------------------
 
		    if (paymentModeId != null && (paymentModeId == DD_PAYMENT_ID || paymentModeId == CHEQUE_PAYMENT_ID)) {
		        
		        StudentApplicationTransaction transaction = new StudentApplicationTransaction();
		        
		        // Link to the main payment record
		        transaction.setPaymnetDetails(savedPaymentDetails);
		        transaction.setPaymentMode(savedPaymentDetails.getPaymenMode());
		        
		        // Set common transaction fields
		        transaction.setNumber(paymentDTO.getTransactionNumber()); // DD No or Cheque No
		        transaction.setDate(paymentDTO.getTransactionDate());     // DD Date or Cheque Date
		        transaction.setApplication_fee_pay_date(paymentDTO.getPaymentDate());
		        transaction.setCreated_by(paymentDTO.getCreatedBy());
		        transaction.setStatus("Pending"); // Set a default status, e.g., "Pending" or "Submitted"
 
		        if (paymentModeId == DD_PAYMENT_ID) {
		            // --- Set DD-Specific Fields ---
		            if (paymentDTO.getOrganisationId() != null) {
		                transaction.setOrg_id(paymentDTO.getOrganisationId());
		            }
		            if (paymentDTO.getBankId() != null) {
		                orgBankRepo.findById(paymentDTO.getBankId()).ifPresent(transaction::setOrgBank);
		            }
		            if (paymentDTO.getBranchId() != null) {
		                orgBankBranchRepo.findById(paymentDTO.getBranchId()).ifPresent(transaction::setOrgBankBranch);
		            }
		            
		        } else if (paymentModeId == CHEQUE_PAYMENT_ID) {
		            // --- Set Cheque-SpecFfic Fields ---
		            transaction.setIfsc_code(paymentDTO.getIfscCode());
		            
		            if (paymentDTO.getCityId() != null) {
		                cityRepo.findById(paymentDTO.getCityId()).ifPresent(transaction::setCity);
		            }
		            // Assuming Cheque also uses Bank and Branch, based on your UI and entity
		            if (paymentDTO.getBankId() != null) {
		                orgBankRepo.findById(paymentDTO.getBankId()).ifPresent(transaction::setOrgBank);
		            }
		            if (paymentDTO.getBranchId() != null) {
		                orgBankBranchRepo.findById(paymentDTO.getBranchId()).ifPresent(transaction::setOrgBankBranch);
		            }
		        }
 
		        // 3. SAVE THE TRANSACTION RECORD
		        studentApplicationTransactionRepo.save(transaction);
		    }
		}
 
		return savedAcademicDetails;
	}
	
	@Transactional
	public StudentAcademicDetails createAdmission(StudentSaleDTO formData) {
 
		// ==============================================================
		// PART 1: CREATE THE STUDENT
		// ==============================================================
		
		Long admissionNumberNumeric = formData.getStudAdmsNo();

    	if (admissionNumberNumeric == null) {
    	    throw new IllegalArgumentException("Admission Number must be provided.");
    	}

    	Distribution distribution = distributionRepo.findProDistributionForAdmissionNumber(admissionNumberNumeric)
    	        .orElseThrow(() -> new EntityNotFoundException(
    	            "No PRO has been assigned for Admission Number: " + admissionNumberNumeric
    	        ));

    	Employee pro = distribution.getIssuedToEmployee();
    	if (pro == null) {
    	     throw new EntityNotFoundException("A PRO has not been linked to the distribution for Admission Number: " + admissionNumberNumeric);
    	}
		// --- 1. Save Academic Details ---
		StudentAcademicDetails academicDetails = new StudentAcademicDetails();
 
		// ... (all the code from Part 1 is correct) ...
		
		academicDetails.setIs_active(0);
		if (formData.getAcademicYearId() != null) {
			academicYearRepository.findById(formData.getAcademicYearId()).ifPresent(academicDetails::setAcademicYear);
		}
 
		academicDetails.setStudAdmsNo(formData.getStudAdmsNo());
		if (formData.getProReceiptNo() != null) {
			academicDetails.setPro_receipt_no(formData.getProReceiptNo().intValue());
		}
		academicDetails.setFirst_name(formData.getFirstName());
		academicDetails.setLast_name(formData.getLastName());
		academicDetails.setApaar_no(formData.getApaarNo());
		academicDetails.setAdms_date(LocalDate.now());
		academicDetails.setApp_sale_date(formData.getAppSaleDate());
		
		if (formData.getProId() != null) {
		    // Convert the Integer Employee ID from the DTO to a String
		    String referredByEmployeeId = String.valueOf(formData.getProId());
		    
		    // Set the String value in the admission_referred_by column
		    academicDetails.setAdmission_referred_by(referredByEmployeeId);
		}
 
		if (formData.getGenderId() != null)
			genderRepo.findById(formData.getGenderId()).ifPresent(academicDetails::setGender);
		if (formData.getAppTypeId() != null)
			admissionTypeRepo.findById(formData.getAppTypeId()).ifPresent(academicDetails::setAdmissionType);
		if (formData.getStudentTypeId() != null)
			studentTypeRepo.findById(formData.getStudentTypeId()).ifPresent(academicDetails::setStudentType);
		if (formData.getQuotaId() != null)
			quotaRepo.findById(formData.getQuotaId()).ifPresent(academicDetails::setQuota);
 
		StudentClass studentClass = classRepo.findById(formData.getClassId())
				.orElseThrow(() -> new EntityNotFoundException("Invalid Class ID: " + formData.getClassId()));
		academicDetails.setStudentClass(studentClass);
 
		Campus campus = campusRepo.findById(formData.getBranchId())
				.orElseThrow(() -> new EntityNotFoundException("Invalid Branch ID: " + formData.getBranchId()));
		academicDetails.setCampus(campus);
 
		if (formData.getProId() != null && formData.getProId() > 0) {
			employeeRepo.findById(formData.getProId()).ifPresent(academicDetails::setEmployee);
		}
 
		academicDetails.setCreated_by(formData.getCreatedBy());
		academicDetails.setEmployee(pro);
 
		// --- FIX 1 (from previous step) ---
		StudyType defaultStudyType = studyTypeRepo.findById(1)
				.orElseThrow(() -> new EntityNotFoundException("Default StudyType (ID: 1) not found"));
		academicDetails.setStudyType(defaultStudyType);
 
		Status defaultStatus = statusRepo.findById(8)
				.orElseThrow(() -> new EntityNotFoundException("Default Status (ID: 1) not found"));
		academicDetails.setStatus(defaultStatus);
 
		StudentAcademicDetails savedAcademicDetails = academicDetailsRepo.save(academicDetails);
 
		// --- 2. Save Personal Details ---
		StudentPersonalDetails personalDetails = new StudentPersonalDetails();
		personalDetails.setStudentAcademicDetails(savedAcademicDetails);
		personalDetails.setStud_aadhaar_no(formData.getAadharCardNo());
		personalDetails.setDob(formData.getDob());
		personalDetails.setCreated_by(formData.getCreatedBy());
 
		// these setted to 1 again these will be updated in the confirmation form
		personalDetails.setCaste_id(3); // Or use personalDetails.setCaste_id(1) if you don't have the mapping
		personalDetails.setReligion_id(3); // Or use personalDetails.setReligion_id(1)
 
		// TODO: Check if blood_group_id is also NOT NULL and add a default if needed
		// BloodGroup defaultBloodGroup = bloodGroupRepo.findById(1)
		// .orElseThrow(() -> new EntityNotFoundException("Default BloodGroup (ID: 1)
		// not found"));
		// personalDetails.setBloodGroup(defaultBloodGroup);
		// --- END FIX 2 ---
 
		personalDetailsRepo.save(personalDetails);
 
		// --- 3. Save Student Orientation Details ---
		StudentOrientationDetails orientationDetails = new StudentOrientationDetails();
		orientationDetails.setStudentAcademicDetails(savedAcademicDetails);
		if (formData.getOrientationId() != null)
			orientationRepo.findById(formData.getOrientationId()).ifPresent(orientationDetails::setOrientation);
		studentOrientationDetailsRepo.save(orientationDetails);
 
		// --- 4. Save Parent Details ---
		if (formData.getFatherName() != null || formData.getFatherMobileNo() != null) {
 
			StudentRelation fatherRelation = relationTypeRepo.findById(1) // Assuming 1 = Father
					.orElseThrow(() -> new EntityNotFoundException("StudentRelation 'Father' (ID: 1) not found"));
 
			ParentDetails parent = new ParentDetails();
			parent.setStudentAcademicDetails(savedAcademicDetails);
			parent.setName(formData.getFatherName());
			parent.setMobileNo(formData.getFatherMobileNo());
			parent.setCreated_by(formData.getCreatedBy());
			parent.setStudentRelation(fatherRelation);
			// these setted to 1 again these will be updated in the confirmation form
			parent.setOccupation("Not Provided");
			parent.setEmail("not provided");
			
			
			parentDetailsRepo.save(parent);
		}
 
		// --- 5. Save Address Details ---
		if (formData.getAddressDetails() != null) {
            AddressDetailsDTO addressDTO = formData.getAddressDetails();
            StudentAddress address = new StudentAddress();
            address.setStudentAcademicDetails(savedAcademicDetails);
            address.setHouse_no(addressDTO.getDoorNo());
            address.setStreet(addressDTO.getStreet());
            address.setLandmark(addressDTO.getLandmark());
            address.setArea(addressDTO.getArea());
            if (addressDTO.getPincode() != null) address.setPostalCode(addressDTO.getPincode());
            if (addressDTO.getStateId()!= null) stateRepo.findById(addressDTO.getStateId()).ifPresent(address::setState);
            if (addressDTO.getCityId() != null) cityRepo.findById(addressDTO.getCityId()).ifPresent(address::setCity);
            if (addressDTO.getMandalId() != null) mandalRepo.findById(addressDTO.getMandalId()).ifPresent(address::setMandal);
            if (addressDTO.getDistrictId() != null) districtRepo.findById(addressDTO.getDistrictId()).ifPresent(address::setDistrict);
            
            // --- FIX 1: Use the 'createdBy' from the AddressDetailsDTO ---
            address.setCreated_by(addressDTO.getCreatedBy());
            
            // --- FIX 2: SET DEFAULT VALUE FOR 'NOT NULL' STATE FIELD ---
            // TODO: Confirm the default State ID (e.g., 1) from your 'state' table.
//            State defaultState = stateRepo.findById(1) // Assuming 1 = "Not Applicable" or a default state
//                    .orElseThrow(() -> new EntityNotFoundException("Default State (ID: 1) not found"));
//            address.setState(defaultState);
            // --- END FIX ---
            
            addressRepo.save(address);
		}
		return savedAcademicDetails;
	}
	
	public ApplicationDetailsDTO getApplicationDetailsByAdmissionNo(Long studAdmsNo) {
        // 1. Fetch the main academic record
        StudentAcademicDetails student = academicDetailsRepo.findByStudAdmsNo(studAdmsNo)
            .orElseThrow(() -> new EntityNotFoundException("Student not found with Admission No: " + studAdmsNo));

        // 2. Fetch related records (handle possibility of them being null)
        Optional<StudentPersonalDetails> personalOpt = personalDetailsRepo.findByStudentAcademicDetails(student);
        Optional<StudentOrientationDetails> orientationOpt = orientationDetailsRepo.findByStudentAcademicDetails(student);
        // Assuming Father has relationTypeId = 1
        Optional<ParentDetails> fatherOpt = parentDetailsRepo.findByStudentAcademicDetailsAndStudentRelationStudentRelationId(student, 1);
        Optional<StudentAddress> addressOpt = addressRepo.findByStudentAcademicDetails(student);

        // 3. Create the main DTO
        ApplicationDetailsDTO detailsDTO = new ApplicationDetailsDTO();

        // 4. Map Academic Details
        detailsDTO.setFirstName(student.getFirst_name());
        detailsDTO.setLastName(student.getLast_name());
        detailsDTO.setApaarNo(student.getApaar_no());
        detailsDTO.setProReceiptNo((long) student.getPro_receipt_no());
        if (student.getGender() != null) {
            detailsDTO.setGenderId(student.getGender().getGender_id());
            detailsDTO.setGenderName(student.getGender().getGenderName());
        }
        // Assuming admissionReferredBy on academicDetails is the ID/Name String? Adjust if it's an object.
        // detailsDTO.setAdmissionReferredById(...); // Map based on how referred by is stored
        // detailsDTO.setAdmissionReferredByName(student.getAdmission_referred_by());

        if (student.getQuota() != null) {
            detailsDTO.setQuotaId(student.getQuota().getQuota_id());
            detailsDTO.setQuotaName(student.getQuota().getQuota_name());
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
            detailsDTO.setStudentTypeName(student.getStudentType().getStud_type()); // Use Lombok's generated getter
            }
        if (student.getStudentClass() != null) {
            detailsDTO.setJoiningClassId(student.getStudentClass().getClassId());
            detailsDTO.setJoiningClassName(student.getStudentClass().getClassName());
        }
         if (student.getCampusSchoolType() != null) { // Assuming this is Branch Type
             detailsDTO.setBranchTypeId(student.getCampusSchoolType().getSchool_type_id());
             detailsDTO.setBranchTypeName(student.getCampusSchoolType().getSchool_type_name());
         }
        if (student.getAdmissionType() != null) {
            detailsDTO.setAdmissionTypeId(student.getAdmissionType().getAdms_type_id());
            detailsDTO.setAdmissionTypeName(student.getAdmissionType().getAdms_type_name());
        }

        // 5. Map Personal Details
        personalOpt.ifPresent(personal -> {
            detailsDTO.setDob(personal.getDob());
            detailsDTO.setAadharCardNo(personal.getStud_aadhaar_no());
        });

        // 6. Map Orientation Details (including Orientation Name and City if applicable)
        orientationOpt.ifPresent(orientation -> {
            if (orientation.getOrientation() != null) {
                detailsDTO.setOrientationId(orientation.getOrientation().getOrientationId());
                detailsDTO.setOrientationName(orientation.getOrientation().getOrientation_name());
            }
            // Map Orientation City if it exists on StudentOrientationDetails
            // if (orientation.getCity() != null) {
            //     detailsDTO.setCityId(orientation.getCity().getCityId());
            //     detailsDTO.setCityName(orientation.getCity().getCityName());
            // }
        });

        // 7. Map Parent Details (Father only for this DTO)
        fatherOpt.ifPresent(father -> {
            detailsDTO.setParentInfo(new ParentSummaryDTO(father.getName(), father.getMobileNo()));
        });

        // 8. Map Address Details
        addressOpt.ifPresent(address -> {
            AddressDetailsNewDTO addressDTO = new AddressDetailsNewDTO();
            addressDTO.setDoorNo(address.getHouse_no());
            addressDTO.setStreet(address.getStreet());
            addressDTO.setLandmark(address.getLandmark());
            addressDTO.setArea(address.getArea());
            addressDTO.setPincode(address.getPostalCode());
            if (address.getDistrict() != null) {
                addressDTO.setDistrictId(address.getDistrict().getDistrictId());
                addressDTO.setDistrictName(address.getDistrict().getDistrictName());            }
            if (address.getMandal() != null) {
                addressDTO.setMandalId(address.getMandal().getMandal_id());
                addressDTO.setMandalName(address.getMandal().getMandal_name());
            }
            if (address.getCity() != null) {
                addressDTO.setCityId(address.getCity().getCityId());
                addressDTO.setCityName(address.getCity().getCityName());
            }
            detailsDTO.setAddressDetails(addressDTO);
        });

        return detailsDTO;
    }
	
	
	@Transactional // Ensures atomicity: all updates succeed or all rollback
	public StudentSaleDTO updateApplicationDetails(StudentSaleDTO saleDTO) {
	    Long studAdmsNo = saleDTO.getStudAdmsNo();
	    
	    // 1. Fetch the main academic record (must exist for an update)
	    StudentAcademicDetails academicDetails = academicDetailsRepo.findByStudAdmsNo(studAdmsNo)
	        .orElseThrow(() -> new EntityNotFoundException("Student not found with Admission No: " + studAdmsNo));

	    // 2. Fetch related records - use Optional for convenience
	    // For update, it's safer to fetch existing related records or initialize new ones.
	    StudentPersonalDetails personalDetails = personalDetailsRepo.findByStudentAcademicDetails(academicDetails)
	        .orElse(new StudentPersonalDetails());
	    personalDetails.setStudentAcademicDetails(academicDetails); // Set the relationship

	    // Assuming Father has relationTypeId = 1
	    ParentDetails fatherDetails = parentDetailsRepo.findByStudentAcademicDetailsAndStudentRelationStudentRelationId(academicDetails, 1)
	        .orElse(new ParentDetails());
	    fatherDetails.setStudentAcademicDetails(academicDetails);
	    // You'll need to fetch and set the StudentRelation object for Father (ID 1)
	    // For brevity, I'm assuming you have access to a repository for StudentRelation
	    // fatherDetails.setStudentRelation(studentRelationRepo.findById(1).orElseThrow(() -> new EntityNotFoundException("Relation type not found")));

	    StudentAddress studentAddress = addressRepo.findByStudentAcademicDetails(academicDetails)
	        .orElse(new StudentAddress());
	    studentAddress.setStudentAcademicDetails(academicDetails);
	    
	    // --- MAP DTO TO ENTITIES ---

	    // 3. Map Academic Details (StudentAcademicDetails)
	    academicDetails.setFirst_name(saleDTO.getFirstName());
	    academicDetails.setLast_name(saleDTO.getLastName());
	    academicDetails.setApaar_no(saleDTO.getApaarNo());
	    if (saleDTO.getProReceiptNo() != null) {
	        // Assuming StudentAcademicDetails.setPro_receipt_no() expects an int/Integer
	        academicDetails.setPro_receipt_no(saleDTO.getProReceiptNo().intValue());
	    }
	    
	    if (saleDTO.getGenderId() != null) {
	        academicDetails.setGender(
	            genderRepo.findById(saleDTO.getGenderId())
	                .orElseThrow(() -> new EntityNotFoundException("Gender not found"))
	        );
	    }

	    if (saleDTO.getQuotaId() != null) {
	        academicDetails.setQuota(
	            quotaRepo.findById(saleDTO.getQuotaId())
	                .orElseThrow(() -> new EntityNotFoundException("Quota not found"))
	        );
	    }
	    if (saleDTO.getAcademicYearId() != null) {
	        academicDetails.setAcademicYear(
	            academicYearRepository.findById(saleDTO.getAcademicYearId())
	                .orElseThrow(() -> new EntityNotFoundException("Academic year not found"))
	        );
	    }
	   
	    if (saleDTO.getBranchId() != null) {
	        academicDetails.setCampus(
	            campusRepo.findById(saleDTO.getBranchId())
	                .orElseThrow(() -> new EntityNotFoundException("Campus not found"))
	        );
	    }
	    if (saleDTO.getStudentTypeId() != null) {
	        academicDetails.setStudentType(
	            studentTypeRepo.findById(saleDTO.getStudentTypeId())
	                .orElseThrow(() -> new EntityNotFoundException("Student type not found"))
	        );
	    }

	    if (saleDTO.getClassId() != null) {
	        academicDetails.setStudentClass(
	            classRepo.findById(saleDTO.getClassId())
	                .orElseThrow(() -> new EntityNotFoundException("Class not found"))
	        );
	    }

//	    if (saleDTO.getSchoolTypeId() != null) {
//	        academicDetails.setCampusSchoolType(
//	            campusSchoolTypeRepo.findById(saleDTO.getSchoolTypeId())
//	                .orElseThrow(() -> new EntityNotFoundException("School type not found"))
//	        );
//	    }

	    if (saleDTO.getAppTypeId() != null) {
	        academicDetails.setAdmissionType(
	            admissionTypeRepo.findById(saleDTO.getAppTypeId())
	                .orElseThrow(() -> new EntityNotFoundException("Admission type not found"))
	        );
	    }
	    
	    // 4. Map Personal Details (StudentPersonalDetails)
	    personalDetails.setDob(saleDTO.getDob());
	    personalDetails.setStud_aadhaar_no(saleDTO.getAadharCardNo());

	    // 5. Map Parent Details (Father only - ParentDetails)
	    fatherDetails.setName(saleDTO.getFatherName());
	    fatherDetails.setMobileNo(saleDTO.getFatherMobileNo());
	    
	    StudentOrientationDetails orientationDetails = studentOrientationDetailsRepo.findByStudentAcademicDetails(academicDetails)
	    	    .orElse(new StudentOrientationDetails());
	    	orientationDetails.setStudentAcademicDetails(academicDetails);

	    	
	    	if (saleDTO.getOrientationId() != null) {
	    	    orientationRepo.findById(saleDTO.getOrientationId())
	    	        .ifPresent(orientationDetails::setOrientation);
	    	}
	    	
	    	studentOrientationDetailsRepo.save(orientationDetails);

	    // 6. Map Address Details (StudentAddress)
	    AddressDetailsDTO addressDTO = saleDTO.getAddressDetails();
	    if (addressDTO != null) {
	        studentAddress.setHouse_no(addressDTO.getDoorNo());
	        studentAddress.setStreet(addressDTO.getStreet());
	        studentAddress.setLandmark(addressDTO.getLandmark());
	        studentAddress.setArea(addressDTO.getArea());
	        studentAddress.setPostalCode(addressDTO.getPincode());
	        
	        if (addressDTO.getDistrictId() != null) {
	            studentAddress.setDistrict(
	                districtRepo.findById(addressDTO.getDistrictId())
	                    .orElseThrow(() -> new EntityNotFoundException("District not found"))
	            );
	        }

	        // Mandal
	        if (addressDTO.getMandalId() != null) {
	            studentAddress.setMandal(
	                mandalRepository.findById(addressDTO.getMandalId())
	                    .orElseThrow(() -> new EntityNotFoundException("Mandal not found"))
	            );
	        }

	        // City
	        if (addressDTO.getCityId() != null) {
	            studentAddress.setCity(
	                cityRepo.findById(addressDTO.getCityId())
	                    .orElseThrow(() -> new EntityNotFoundException("City not found"))
	            );
	        }
	        // Map DTO IDs to entity objects (City, Mandal, District)
	        // Similar to Academic details, fetch and set the actual entity objects (e.g., District, Mandal, City)
	    }

	    // --- SAVE ALL ENTITIES ---
	    // The save() method of Spring Data JPA handles both INSERT and UPDATE (MERGE)
	    academicDetailsRepo.save(academicDetails);
	    personalDetailsRepo.save(personalDetails);
	    parentDetailsRepo.save(fatherDetails);
	    addressRepo.save(studentAddress);

	    // *Optional:* Re-fetch and map the saved data back to a DTO to return the complete, persisted state
	    // For simplicity, we just return the input DTO, but a full service implementation might re-run the GET logic.
	    return saleDTO; 
	}
}