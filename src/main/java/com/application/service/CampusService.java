//package com.application.service;
// 
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.application.dto.AppNumberRangeDTO;
//import com.application.dto.DgmToCampusFormDTO;
//import com.application.dto.GenericDropdownDTO;
//import com.application.entity.BalanceTrack;
//import com.application.entity.Campaign;
//import com.application.entity.Campus;
//import com.application.entity.CampusProView;
//import com.application.entity.Distribution;
//import com.application.entity.District;
//import com.application.entity.Employee;
//import com.application.repository.AcademicYearRepository;
//import com.application.repository.AppIssuedTypeRepository;
//import com.application.repository.BalanceTrackRepository;
//import com.application.repository.CampaignRepository;
//import com.application.repository.CampusProViewRepository;
//import com.application.repository.CampusRepository;
//import com.application.repository.CityRepository;
//import com.application.repository.DistributionRepository;
//import com.application.repository.DistrictRepository;
//import com.application.repository.EmployeeRepository;
//import com.application.repository.StateRepository;
//
//import lombok.NonNull;
// 
//@Service
//public class CampusService {
// 
//    @Autowired private AcademicYearRepository academicYearRepository;
//    @Autowired private StateRepository stateRepository;
//    @Autowired private DistrictRepository districtRepository;
//    @Autowired private CityRepository cityRepository;
//    @Autowired private CampusRepository campusRepository;
//    @Autowired private CampaignRepository campaignRepository;
//    @Autowired private AppIssuedTypeRepository appIssuedTypeRepository;
//    @Autowired private DistributionRepository distributionRepository;
//    @Autowired private EmployeeRepository employeeRepository;
//    @Autowired private BalanceTrackRepository balanceTrackRepository;
//    @Autowired private CampusProViewRepository campusProViewRepository;
// 
//    // --- Dropdown Logic ---
//    public List<GenericDropdownDTO> getAllAcademicYears() {
//        return academicYearRepository.findAll().stream()
//                .map(y -> new GenericDropdownDTO(y.getAcdcYearId(), y.getAcademicYear())).collect(Collectors.toList());
//    }
//    public List<GenericDropdownDTO> getAllStates() {
//        return stateRepository.findAll().stream()
//                .map(s -> new GenericDropdownDTO(s.getStateId(), s.getStateName())).collect(Collectors.toList());
//    }
//    
//    public List<District> getAllDistricts()
//    {
//    	return districtRepository.findAll();
//    }
//    
//    public List<GenericDropdownDTO> getDistrictsByStateId(int stateId) {
//        return districtRepository.findByStateStateId(stateId).stream()
//                .map(d -> new GenericDropdownDTO(d.getDistrictId(), d.getDistrictName())).collect(Collectors.toList());
//    }
//    public List<GenericDropdownDTO> getCitiesByDistrictId(int districtId) {
//        return cityRepository.findByDistrictDistrictId(districtId).stream()
//                .map(c -> new GenericDropdownDTO(c.getCityId(), c.getCityName())).collect(Collectors.toList());
//    }
//    public List<GenericDropdownDTO> getCampusesByCityId(int cityId) {
//        return campusRepository.findByCityCityId(cityId).stream()
//                .map(c -> new GenericDropdownDTO(c.getCampusId(), c.getCampusName())).collect(Collectors.toList());
//    }
//    public List<GenericDropdownDTO> getAllCampaignAreas() {
//        return campaignRepository.findAll().stream()
//                .map(c -> new GenericDropdownDTO(c.getCampaignId(), c.getAreaName())).collect(Collectors.toList());
//    }
//    public List<GenericDropdownDTO> getProsByCampusId(int campusId) {
//        List<Integer> employeeIds = campusProViewRepository.findByCampusId(campusId).stream()
//                                                  .map(CampusProView::getEmp_id)
//                                                  .collect(Collectors.toList());
//        if (employeeIds.isEmpty()) {
//            return List.of();
//        }
//        return employeeRepository.findAllById(employeeIds).stream()
//                .map(employee -> new GenericDropdownDTO(employee.getEmp_id(), employee.getFirst_name() + " " + employee.getLast_name()))
//                .collect(Collectors.toList());
//    }
//    public List<GenericDropdownDTO> getAllIssuedToTypes() {
//        return appIssuedTypeRepository.findAll().stream()
//                .map(t -> new GenericDropdownDTO(t.getAppIssuedId(), t.getTypeName())).collect(Collectors.toList());
//    }
//    public List<AppNumberRangeDTO> getAvailableAppNumberRanges(int employeeId, int academicYearId) {
//        return balanceTrackRepository.findAppNumberRanges(academicYearId, employeeId).stream()
//                .map(r -> new AppNumberRangeDTO(r.getAppBalanceTrkId(), r.getAppFrom(), r.getAppTo())).collect(Collectors.toList());
//    }
//    public String getMobileNumberByEmpId(int empId) {
//        return employeeRepository.findMobileNoByEmpId(empId);
//    }
//    
//    private int getDgmUserTypeId() {
//        
//        return 3;
//    }
//    
//    public List<GenericDropdownDTO> getCampusByCampaignId(int campaignId) {
//        List<Campus> campuses = campusRepository.findCampusByCampaignId(campaignId);
//        return campuses.stream().map(c -> new GenericDropdownDTO(c.getCampusId(), c.getCampusName())).collect(Collectors.toList());
//    }
//    
//    public List<GenericDropdownDTO> getCampaignsByCityId(int cityId) {
//        List<Campaign> campaigns = campaignRepository.findByCity_CityId(cityId);
//        return campaigns.stream()
//            .map(c -> new GenericDropdownDTO(c.getCampaignId(), c.getAreaName()))
//            .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public void submitDgmToCampusForm(DgmToCampusFormDTO formDto) {
//        int dgmUserId = formDto.getUserId();
//        int proEmployeeId = formDto.getProEmployeeId();
//        int dgmUserTypeId = getDgmUserTypeId();
//        int appNoFrom = Integer.parseInt(formDto.getApplicationNoFrom());
//        int appNoTo = Integer.parseInt(formDto.getApplicationNoTo());
// 
//        BalanceTrack dgmBalance = balanceTrackRepository.findById(formDto.getSelectedBalanceTrackId())
//                .orElseThrow(() -> new RuntimeException("The selected application range for the DGM was not found."));
//       
//        if (appNoFrom < dgmBalance.getAppFrom() || appNoTo > dgmBalance.getAppTo()) {
//            throw new IllegalStateException("The submitted application number range is outside the DGM's available range.");
//        }
//       
//        dgmBalance.setAppAvblCnt(dgmBalance.getAppAvblCnt() - formDto.getRange());
//        dgmBalance.setAppFrom(appNoTo + 1);
//        balanceTrackRepository.save(dgmBalance);
// 
//        Optional<BalanceTrack> proBalanceOpt = balanceTrackRepository.findBalanceTrack(formDto.getAcademicYearId(), proEmployeeId);
//        BalanceTrack proBalance;
//        if (proBalanceOpt.isPresent()) {
//            proBalance = proBalanceOpt.get();
//            proBalance.setAppAvblCnt(proBalance.getAppAvblCnt() + formDto.getRange());
//            proBalance.setAppTo(appNoTo);
//        } else {
//            proBalance = new BalanceTrack();
//            Employee proEmployee = employeeRepository.findById(proEmployeeId)
//                    .orElseThrow(() -> new RuntimeException("Receiver PRO employee not found for ID: " + proEmployeeId));
//            proBalance.setEmployee(proEmployee);
//            proBalance.setAcademicYear(academicYearRepository.findById(formDto.getAcademicYearId()).orElse(null));
//            proBalance.setAppFrom(appNoFrom);
//            proBalance.setAppTo(appNoTo);
//            proBalance.setAppAvblCnt(formDto.getRange());
//            proBalance.setIssuedByType(appIssuedTypeRepository.findById(formDto.getIssuedToId()).orElse(null));
//            proBalance.setIsActive(1);
//            proBalance.setCreatedBy(dgmUserId);
//        }
//        balanceTrackRepository.save(proBalance);
// 
//        Distribution distribution = new Distribution();
//       
//        academicYearRepository.findById(formDto.getAcademicYearId()).ifPresent(distribution::setAcademicYear);
//        districtRepository.findById(formDto.getDistrictId()).ifPresent(distribution::setDistrict);
//        cityRepository.findById(formDto.getCityId()).ifPresent(distribution::setCity);
//        campusRepository.findById(formDto.getCampusId()).ifPresent(distribution::setCampus);
//        appIssuedTypeRepository.findById(dgmUserTypeId).ifPresent(distribution::setIssuedByType);
//        appIssuedTypeRepository.findById(formDto.getIssuedToId()).ifPresent(distribution::setIssuedToType);
//       
//        Campus selectedCampus = campusRepository.findById(formDto.getCampusId()).orElse(null);
//        distribution.setCampus(selectedCampus);
// 
//        if (selectedCampus != null) {
//            distribution.setZone(selectedCampus.getZone());
//        }
// 
//        if (distribution.getDistrict() != null) {
//            distribution.setState(distribution.getDistrict().getState());
//        }
//       
//        distribution.setAppStartNo(appNoFrom);
//        distribution.setAppEndNo(appNoTo);
//        distribution.setTotalAppCount(formDto.getRange());
//        distribution.setIssueDate(LocalDate.now());
//        distribution.setIsActive(1);
//        distribution.setCreated_by(dgmUserId);
//        distribution.setIssued_to_emp_id(proEmployeeId);
//       
//        distributionRepository.save(distribution);
//    }
// 
//    @Transactional
//    public void updateDgmToCampusForm(@NonNull Integer distributionId, @NonNull DgmToCampusFormDTO formDto) {
//        // Step 1: Find the existing distribution record and its original values.
//        Distribution existingDistribution = distributionRepository.findById(distributionId)
//                .orElseThrow(() -> new RuntimeException("Original distribution record not found for ID: " + distributionId));
// 
//        int oldRange = existingDistribution.getTotalAppCount();
//        int oldIssuedToEmpId = existingDistribution.getIssued_to_emp_id();
//        int newAppFrom = Integer.parseInt(formDto.getApplicationNoFrom());
//        int newAppTo = Integer.parseInt(formDto.getApplicationNoTo());
//        int newRange = formDto.getRange();
//        int dgmUserId = formDto.getUserId();
//        int newProEmployeeId = formDto.getProEmployeeId();
//        int academicYearId = formDto.getAcademicYearId();
// 
//        // Step 2: Adjust the DGM's (issuer) balance by the net difference.
//        // This is a more direct and reliable approach than a full revert and re-apply.
//        Optional<BalanceTrack> dgmBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, dgmUserId);
//        if (dgmBalanceOpt.isPresent()) {
//            BalanceTrack dgmBalance = dgmBalanceOpt.get();
//            int rangeDifference = newRange - oldRange;
//            dgmBalance.setAppAvblCnt(dgmBalance.getAppAvblCnt() - rangeDifference);
//            // Assuming sequential range usage, update the appFrom for the issuer.
//            dgmBalance.setAppFrom(newAppTo + 1);
//            balanceTrackRepository.save(dgmBalance);
//        } else {
//            throw new RuntimeException("DGM's balance track not found.");
//        }
// 
//        // Step 3: Adjust the balances of the old and new PROs (receivers).
//        if (oldIssuedToEmpId != newProEmployeeId) {
//            // A new PRO is receiving the applications, so adjust both balances.
//            // Revert the old PRO's balance.
//            Optional<BalanceTrack> oldProBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, oldIssuedToEmpId);
//            if (oldProBalanceOpt.isPresent()) {
//                BalanceTrack oldProBalance = oldProBalanceOpt.get();
//                oldProBalance.setAppAvblCnt(oldProBalance.getAppAvblCnt() - oldRange);
//                // This is the crucial fix: correctly adjust the appTo for the old record.
//                oldProBalance.setAppTo(newAppFrom - 1);
//                balanceTrackRepository.save(oldProBalance);
//            } else {
//                throw new RuntimeException("Original PRO's balance track not found.");
//            }
// 
//            // Add the new range to the new PRO's balance.
//            Optional<BalanceTrack> newProBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, newProEmployeeId);
//            BalanceTrack newProBalance;
//            if (newProBalanceOpt.isPresent()) {
//                newProBalance = newProBalanceOpt.get();
//                newProBalance.setAppAvblCnt(newProBalance.getAppAvblCnt() + newRange);
//                newProBalance.setAppTo(newAppTo);
//            } else {
//                newProBalance = new BalanceTrack();
//                newProBalance.setEmployee(employeeRepository.findById(newProEmployeeId).orElseThrow(() -> new RuntimeException("New PRO employee not found.")));
//                newProBalance.setAcademicYear(academicYearRepository.findById(academicYearId).orElseThrow(() -> new RuntimeException("Academic year not found.")));
//                newProBalance.setAppFrom(newAppFrom);
//                newProBalance.setAppTo(newAppTo);
//                newProBalance.setAppAvblCnt(newRange);
//                newProBalance.setIssuedByType(appIssuedTypeRepository.findById(formDto.getIssuedToId()).orElseThrow(() -> new RuntimeException("Issued to type not found.")));
//                newProBalance.setIsActive(1);
//                newProBalance.setCreatedBy(dgmUserId);
//            }
//            balanceTrackRepository.save(newProBalance);
//        } else {
//            // The PRO is the same, so just adjust their balance by the net change in range.
//            Optional<BalanceTrack> proBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, newProEmployeeId);
//            if (proBalanceOpt.isPresent()) {
//                BalanceTrack proBalance = proBalanceOpt.get();
//                int rangeDifference = newRange - oldRange;
//                proBalance.setAppAvblCnt(proBalance.getAppAvblCnt() + rangeDifference);
//                proBalance.setAppTo(newAppTo);
//                balanceTrackRepository.save(proBalance);
//            } else {
//                throw new RuntimeException("PRO's balance track not found.");
//            }
//        }
// 
//        // Step 4: Update the Distribution Record itself.
//        existingDistribution.setAcademicYear(academicYearRepository.findById(academicYearId).orElse(null));
//        existingDistribution.setDistrict(districtRepository.findById(formDto.getDistrictId()).orElse(null));
//        existingDistribution.setCity(cityRepository.findById(formDto.getCityId()).orElse(null));
//        existingDistribution.setCampus(campusRepository.findById(formDto.getCampusId()).orElse(null));
// 
//        if (existingDistribution.getDistrict() != null) {
//            existingDistribution.setState(existingDistribution.getDistrict().getState());
//        }
// 
//        existingDistribution.setAppStartNo(newAppFrom);
//        existingDistribution.setAppEndNo(newAppTo);
//        existingDistribution.setTotalAppCount(newRange);
//        existingDistribution.setIssueDate(LocalDate.now());
//        existingDistribution.setIssued_to_emp_id(newProEmployeeId);
//        existingDistribution.setCreated_by(dgmUserId);
// 
//        distributionRepository.save(existingDistribution);
//    }}
package com.application.service;

import com.application.dto.AppNumberRangeDTO;
import com.application.dto.DgmToCampusFormDTO;
import com.application.dto.GenericDropdownDTO;
import com.application.entity.*;
import com.application.repository.*;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampusService {

    private final AcademicYearRepository academicYearRepository;
    private final StateRepository stateRepository;
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;
    private final CampusRepository campusRepository;
    private final CampaignRepository campaignRepository;
    private final AppIssuedTypeRepository appIssuedTypeRepository;
    private final DistributionRepository distributionRepository;
    private final EmployeeRepository employeeRepository;
    private final BalanceTrackRepository balanceTrackRepository;
    private final CampusProViewRepository campusProViewRepository;
    
    @Autowired
    UserAdminViewRepository userAdminViewRepository;

    public CampusService(AcademicYearRepository academicYearRepository, StateRepository stateRepository,
                         DistrictRepository districtRepository, CityRepository cityRepository,
                         CampusRepository campusRepository, CampaignRepository campaignRepository,
                         AppIssuedTypeRepository appIssuedTypeRepository, DistributionRepository distributionRepository,
                         EmployeeRepository employeeRepository, BalanceTrackRepository balanceTrackRepository,
                         CampusProViewRepository campusProViewRepository) {
        this.academicYearRepository = academicYearRepository;
        this.stateRepository = stateRepository;
        this.districtRepository = districtRepository;
        this.cityRepository = cityRepository;
        this.campusRepository = campusRepository;
        this.campaignRepository = campaignRepository;
        this.appIssuedTypeRepository = appIssuedTypeRepository;
        this.distributionRepository = distributionRepository;
        this.employeeRepository = employeeRepository;
        this.balanceTrackRepository = balanceTrackRepository;
        this.campusProViewRepository = campusProViewRepository;
    }

    // --- Dropdowns & Helpers with caching ---
//    @Cacheable("academicYears")
    public List<GenericDropdownDTO> getAllAcademicYears() {
        return academicYearRepository.findAll().stream()
                .map(y -> new GenericDropdownDTO(y.getAcdcYearId(), y.getAcademicYear()))
                .collect(Collectors.toList());
    }

//    @Cacheable("states")
    public List<GenericDropdownDTO> getAllStates() {
        return stateRepository.findAll().stream()
                .map(s -> new GenericDropdownDTO(s.getStateId(), s.getStateName()))
                .collect(Collectors.toList());
    }

    @Cacheable("districts")
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

//    @Cacheable(cacheNames = "districtsByState", key = "#stateId")
    public List<GenericDropdownDTO> getDistrictsByStateId(int stateId) {
        return districtRepository.findByStateStateId(stateId).stream()
                .map(d -> new GenericDropdownDTO(d.getDistrictId(), d.getDistrictName()))
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "citiesByDistrict", key = "#districtId")
    public List<GenericDropdownDTO> getCitiesByDistrictId(int districtId) {
    	
    	final int ACTIVE_STATUS = 1;
        return cityRepository.findByDistrictDistrictIdAndStatus(districtId,ACTIVE_STATUS).stream()
                .map(c -> new GenericDropdownDTO(c.getCityId(), c.getCityName()))
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "campusesByCity", key = "#cityId")
    public List<GenericDropdownDTO> getCampusesByCityId(int cityId) {
        return campusRepository.findByCityCityId(cityId).stream()
                .map(c -> new GenericDropdownDTO(c.getCampusId(), c.getCampusName()))
                .collect(Collectors.toList());
    }

//    @Cacheable("campaignAreas")
    public List<GenericDropdownDTO> getAllCampaignAreas() {
        return campaignRepository.findAll().stream()
                .map(c -> new GenericDropdownDTO(c.getCampaignId(), c.getAreaName()))
                .collect(Collectors.toList());
    }

//    @Cacheable(cacheNames = "prosByCampus", key = "#campusId")
//    public List<GenericDropdownDTO> getProsByCampusId(int campusId) {
//        List<Integer> employeeIds = campusProViewRepository.findByCampusId(campusId).stream()
//                .map(CampusProView::getEmp_id)
//                .toList();
//        if (employeeIds.isEmpty()) return List.of();
//        return employeeRepository.findAllById(employeeIds).stream()
//                .map(e -> new GenericDropdownDTO(e.getEmp_id(), e.getFirst_name() + " " + e.getLast_name()))
//                .collect(Collectors.toList());
//    }
//    
  @Cacheable(cacheNames = "prosByCampus", key = "#campusId")
public List<GenericDropdownDTO> getEmployeeDropdownByCampus(int campusId) {
        
        // 1. Get the list of emp_id's from CampusProView based on campusId
        List<Integer> employeeIds = campusProViewRepository.findEmployeeIdsByCampusId(campusId);

        // Check if any IDs were found to avoid running an empty IN clause
        if (employeeIds.isEmpty()) {
            return List.of(); // Return an empty list
        }

        // 2. Use the list of IDs to fetch the employee details and map to DTO
        return employeeRepository.findEmployeesByIdsAsDropdown(employeeIds);
    }

//    @Cacheable("issuedToTypes")
    public List<GenericDropdownDTO> getAllIssuedToTypes() {
        return appIssuedTypeRepository.findAll().stream()
                .map(t -> new GenericDropdownDTO(t.getAppIssuedId(), t.getTypeName()))
                .collect(Collectors.toList());
    }

//    @Cacheable(cacheNames = "availableAppNumberRanges", key = "{#employeeId, #academicYearId}")
    public List<AppNumberRangeDTO> getAvailableAppNumberRanges(int employeeId, int academicYearId) {
        return balanceTrackRepository.findAppNumberRanges(academicYearId, employeeId).stream()
                .map(r -> new AppNumberRangeDTO(r.getAppBalanceTrkId(), r.getAppFrom(), r.getAppTo()))
                .collect(Collectors.toList());
    }

//    @Cacheable(cacheNames = "mobileNumberByEmpId", key = "#empId")
    public String getMobileNumberByEmpId(int empId) {
        return employeeRepository.findMobileNoByEmpId(empId);
    }

//    @Cacheable(cacheNames = "campusByCampaign", key = "#campaignId")
    public List<GenericDropdownDTO> getCampusByCampaignId(int campaignId) {
        return campusRepository.findCampusByCampaignId(campaignId).stream()
                .map(c -> new GenericDropdownDTO(c.getCampusId(), c.getCampusName()))
                .toList();
    }

//    @Cacheable(cacheNames = "campaignsByCity", key = "#cityId")
    public List<GenericDropdownDTO> getCampaignsByCityId(int cityId) {
        return campaignRepository.findByCity_CityId(cityId).stream()
                .map(c -> new GenericDropdownDTO(c.getCampaignId(), c.getAreaName()))
                .toList();
    }

    private int getDgmUserTypeId(int userId) { 
    	// NOTE: Ensure UserAdminViewRepository has the findByEmpIdUsingQuery method implemented
    	List<UserAdminView> userRoles = userAdminViewRepository.findByEmpIdUsingQuery(userId);

        if (userRoles.isEmpty()) {
            throw new RuntimeException("Issuer details not found for employee ID: " + userId + ". Cannot determine role.");
        }
        
        int highestPriorityTypeId = Integer.MAX_VALUE;

        for (UserAdminView userView : userRoles) {
            String roleName = userView.getRole_name();
            String normalizedRoleName = roleName.toUpperCase().trim();
            
            int currentTypeId = switch (normalizedRoleName) {
                case "ADMIN" -> 1;
                case "ZONAL ACCOUNTANT" -> 2;
                case "DGM" -> 3;
                default -> -1; // Ignore unsupported roles
            };

            if (currentTypeId != -1 && currentTypeId < highestPriorityTypeId) {
                highestPriorityTypeId = currentTypeId;
            }
        }

        if (highestPriorityTypeId == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Employee ID: " + userId + " holds roles, but none are authorized to issue applications.");
        }
        
        return highestPriorityTypeId;
    }

    // --- FORM SUBMISSION / UPDATE METHODS with cache eviction ---
    @Transactional
//    @CacheEvict(value = {
//            "availableAppNumberRanges",
//            "employeeApplications",
//            "nextAppNumber",
//            "balanceTrack"
//    }, allEntries = true)
    public void submitDgmToCampusForm(@NonNull DgmToCampusFormDTO formDto) {
        int dgmUserId = formDto.getUserId();
        int proEmployeeId = formDto.getProEmployeeId();
        int dgmUserTypeId = getDgmUserTypeId(dgmUserId); // Assuming this helper exists and works

        // The commented-out overlapping distribution logic is safe to keep commented 
        // out if that feature is not required for the DGM -> PRO transfer.

        // 1. Create and save the new distribution
        Distribution distribution = new Distribution();
        // Assuming mapDtoToDistribution is updated to handle DgmToCampusFormDTO
        mapDtoToDistribution(distribution, formDto, dgmUserTypeId); 
        distributionRepository.save(distribution);

        // 2. Recalculate balances
        // Recalculate for the DGM (who loses applications)
        recalculateBalanceForEmployee(dgmUserId, formDto.getAcademicYearId(), dgmUserTypeId, dgmUserId);
        
        // Recalculate for the PRO Employee (who gains applications). 
        // This correctly handles the creation of a new BalanceTrack record if none exists.
        recalculateBalanceForEmployee(proEmployeeId, formDto.getAcademicYearId(), formDto.getIssuedToId(), dgmUserId);
    }

    @Transactional
//    @CacheEvict(value = {
//            "availableAppNumberRanges",
//            "employeeApplications",
//            "nextAppNumber",
//            "balanceTrack"
//    }, allEntries = true)
    public void updateDgmToCampusForm(@NonNull Integer distributionId, @NonNull DgmToCampusFormDTO formDto) {
        Distribution existingDistribution = distributionRepository.findById(distributionId)
                .orElseThrow(() -> new RuntimeException("Distribution record not found for ID: " + distributionId));

        int oldReceiverId = existingDistribution.getIssued_to_emp_id();
        int dgmUserId = formDto.getUserId();
        int academicYearId = formDto.getAcademicYearId();

        int oldStart = existingDistribution.getAppStartNo();
        int oldEnd = existingDistribution.getAppEndNo();
        int newStart = Integer.parseInt(formDto.getApplicationNoFrom());
        int newEnd = Integer.parseInt(formDto.getApplicationNoTo());

        boolean isRecipientChanging = oldReceiverId != formDto.getProEmployeeId();
        boolean isRangeChanging = oldStart != newStart || oldEnd != newEnd;

        // 1. Inactivate the original record
        existingDistribution.setIsActive(0);
        distributionRepository.save(existingDistribution);

        // 2. Insert new record for the requested range (ONE new record)
        Distribution newRequestedDistribution = new Distribution();
        mapDtoToDistribution(newRequestedDistribution, formDto, getDgmUserTypeId(dgmUserId));
        distributionRepository.save(newRequestedDistribution);

        // 3. Handle the remainder (only if the range changed)
        if (isRangeChanging) {
            // Handle the part of the OLD range that is BEFORE the NEW range, if any
            if (oldStart < newStart) {
                // Create a remainder distribution for the old receiver
                Distribution beforeRemainder = createRemainderDistribution(existingDistribution, oldReceiverId);
                beforeRemainder.setAppStartNo(oldStart);
                beforeRemainder.setAppEndNo(newStart - 1);
                beforeRemainder.setTotalAppCount(beforeRemainder.getAppEndNo() - beforeRemainder.getAppStartNo() + 1);
                distributionRepository.save(beforeRemainder);
            }
            
            // Handle the part of the OLD range that is AFTER the NEW range, if any
            if (oldEnd > newEnd) {
                // Create a remainder distribution for the old receiver
                Distribution afterRemainder = createRemainderDistribution(existingDistribution, oldReceiverId);
                afterRemainder.setAppStartNo(newEnd + 1);
                afterRemainder.setAppEndNo(oldEnd);
                afterRemainder.setTotalAppCount(afterRemainder.getAppEndNo() - afterRemainder.getAppStartNo() + 1);
                distributionRepository.save(afterRemainder);
            }
        }
        
        

        // 4. Recalculate Balances
        int dgmUserTypeId = getDgmUserTypeId(dgmUserId);
        recalculateBalanceForEmployee(dgmUserId, academicYearId, dgmUserTypeId, dgmUserId); // Issuer (DGM)
        recalculateBalanceForEmployee(formDto.getProEmployeeId(), academicYearId, formDto.getIssuedToId(), dgmUserId); // New Receiver (PRO)

        // Recalculate for the old receiver if they lost applications
        if (isRecipientChanging || isRangeChanging) {
            balanceTrackRepository.findBalanceTrack(academicYearId, oldReceiverId).ifPresent(oldBalance -> {
                int oldReceiverTypeId = oldBalance.getIssuedByType().getAppIssuedId();
                recalculateBalanceForEmployee(oldReceiverId, academicYearId, oldReceiverTypeId, dgmUserId);
            });
        }
    }

    // --- PRIVATE HELPER METHODS ---

    private void handleOverlappingDistributions(List<Distribution> overlappingDists, DgmToCampusFormDTO request) {
        int newStartNo = Integer.parseInt(request.getApplicationNoFrom());
        int newEndNo = Integer.parseInt(request.getApplicationNoTo());

        for (Distribution oldDist : overlappingDists) {
            int oldReceiverId = oldDist.getIssued_to_emp_id();
            if (oldReceiverId == request.getProEmployeeId()) continue;

            if (newStartNo <= oldDist.getAppStartNo() && newEndNo >= oldDist.getAppEndNo()) {
                distributionRepository.delete(oldDist);
            } else if (newStartNo > oldDist.getAppStartNo() && newEndNo >= oldDist.getAppEndNo()) {
                oldDist.setAppEndNo(newStartNo - 1);
                oldDist.setTotalAppCount(oldDist.getAppEndNo() - oldDist.getAppStartNo() + 1);
                distributionRepository.save(oldDist);
            } else if (newStartNo <= oldDist.getAppStartNo() && newEndNo < oldDist.getAppEndNo()) {
                oldDist.setAppStartNo(newEndNo + 1);
                oldDist.setTotalAppCount(oldDist.getAppEndNo() - oldDist.getAppStartNo() + 1);
                distributionRepository.save(oldDist);
            } else if (newStartNo > oldDist.getAppStartNo() && newEndNo < oldDist.getAppEndNo()) {
                Distribution afterSplit = new Distribution();
                mapExistingToNewDistribution(afterSplit, oldDist);
                afterSplit.setAppStartNo(newEndNo + 1);
                afterSplit.setTotalAppCount(afterSplit.getAppEndNo() - afterSplit.getAppStartNo() + 1);
                distributionRepository.save(afterSplit);
                
                oldDist.setAppEndNo(newStartNo - 1);
                oldDist.setTotalAppCount(oldDist.getAppEndNo() - oldDist.getAppStartNo() + 1);
                distributionRepository.save(oldDist);
            }

            recalculateBalanceForEmployee(oldReceiverId, request.getAcademicYearId(), oldDist.getIssuedToType().getAppIssuedId(), request.getUserId());
        }
    }

 // REVISED recalculateBalanceForEmployee for CampusService
 // REVISED recalculateBalanceForEmployee in DgmService
    private void recalculateBalanceForEmployee(int employeeId, int academicYearId, int typeId, int createdBy) {
    	
    	if (typeId == 1 || employeeId == createdBy) {
            // You can log it if you want:
            System.out.println("Skipping balance recalculation for Admin/self-issued record. Employee ID: " + employeeId);
            return;
        }
        
        // Finds existing BalanceTrack or creates a new one.
        // If new, createNewBalanceTrack returns an object with appFrom/appTo set to 0.
        BalanceTrack balance = balanceTrackRepository.findBalanceTrack(academicYearId, employeeId)
                .orElseGet(() -> createNewBalanceTrack(employeeId, academicYearId, typeId, createdBy));

        // 1. Total Applications Received by this employee
        Integer totalReceived = distributionRepository.sumTotalAppCountByIssuedToEmpId(employeeId, academicYearId).orElse(0);

        // 2. Total Applications Distributed/Issued BY this employee
        Integer totalDistributed = distributionRepository.sumTotalAppCountByCreatedBy(employeeId, academicYearId).orElse(0);

        // 3. Available balance is Received - Distributed
        int availableCount = totalReceived - totalDistributed;

        // 4. Calculate current min/max range of received (Crucial: queries the Distribution table)
        Integer minReceived = distributionRepository.findMinAppStartNoByIssuedToEmpIdAndAcademicYearId(employeeId, academicYearId).orElse(0);
        Integer maxReceived = distributionRepository.findMaxAppEndNoByIssuedToEmpIdAndAcademicYearId(employeeId, academicYearId).orElse(0);

        // 5. Find the maximum end number the employee has distributed
        Integer maxDistributed = distributionRepository.findMaxAppEndNoByCreatedByAndAcademicYearId(employeeId, academicYearId).orElse(0);

        // 6. Adjust the available range (overwrites the 0/0 placeholder if new)
        if (maxDistributed != null && maxDistributed >= minReceived && maxDistributed < maxReceived) {
            // Employee has distributed some: AppFrom starts from the next number after the last distributed one
            balance.setAppFrom(maxDistributed + 1); 
            balance.setAppTo(maxReceived);
        } else {
            // Employee has distributed none: AppFrom/AppTo is the entire received range
            balance.setAppFrom(minReceived);
            balance.setAppTo(maxReceived);
        }

        balance.setAppAvblCnt(availableCount);
        // This save operation persists the calculated, non-zero appFrom/appTo values.
        balanceTrackRepository.save(balance); 
    }

    private void mapDtoToDistribution(Distribution distribution, DgmToCampusFormDTO formDto, int dgmUserTypeId) {
        int appNoFrom = Integer.parseInt(formDto.getApplicationNoFrom());
        int appNoTo = Integer.parseInt(formDto.getApplicationNoTo());

        academicYearRepository.findById(formDto.getAcademicYearId()).ifPresent(distribution::setAcademicYear);
        districtRepository.findById(formDto.getDistrictId()).ifPresent(distribution::setDistrict);
        cityRepository.findById(formDto.getCityId()).ifPresent(distribution::setCity);
        campusRepository.findById(formDto.getCampusId()).ifPresent(distribution::setCampus);
        appIssuedTypeRepository.findById(dgmUserTypeId).ifPresent(distribution::setIssuedByType);
        appIssuedTypeRepository.findById(formDto.getIssuedToId()).ifPresent(distribution::setIssuedToType);

        if (distribution.getCampus() != null) {
            distribution.setZone(distribution.getCampus().getZone());
        }
        if (distribution.getDistrict() != null) {
            distribution.setState(distribution.getDistrict().getState());
        }

        distribution.setAppStartNo(appNoFrom);
        distribution.setAppEndNo(appNoTo);
        distribution.setTotalAppCount(formDto.getRange());
        distribution.setIssueDate(LocalDate.now());
        distribution.setIsActive(1);
        distribution.setCreated_by(formDto.getUserId());
        distribution.setIssued_to_emp_id(formDto.getProEmployeeId());
    }
    
    private void mapExistingToNewDistribution(Distribution newDist, Distribution oldDist) {
        newDist.setAcademicYear(oldDist.getAcademicYear());
        newDist.setState(oldDist.getState());
        newDist.setDistrict(oldDist.getDistrict());
        newDist.setCity(oldDist.getCity());
        newDist.setZone(oldDist.getZone());
        newDist.setCampus(oldDist.getCampus());
        newDist.setIssuedByType(oldDist.getIssuedByType());
        newDist.setIssuedToType(oldDist.getIssuedToType());
        newDist.setIssued_to_emp_id(oldDist.getIssued_to_emp_id());
        newDist.setAppStartNo(oldDist.getAppStartNo());
        newDist.setAppEndNo(oldDist.getAppEndNo());
        newDist.setTotalAppCount(oldDist.getTotalAppCount());
        newDist.setIssueDate(oldDist.getIssueDate());
        newDist.setIsActive(1);
        newDist.setCreated_by(oldDist.getCreated_by());
    }

    private BalanceTrack createNewBalanceTrack(int employeeId, int academicYearId, int typeId, int createdBy) {
        BalanceTrack nb = new BalanceTrack();
        nb.setEmployee(employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found.")));
        nb.setAcademicYear(academicYearRepository.findById(academicYearId).orElseThrow(() -> new RuntimeException("Academic year not found.")));
        nb.setIssuedByType(appIssuedTypeRepository.findById(typeId).orElseThrow(() -> new RuntimeException("Issued by type not found.")));
        nb.setAppAvblCnt(0);
        nb.setAppFrom(0);
        nb.setAppTo(0);
        nb.setIsActive(1);
        nb.setCreatedBy(createdBy);
        return nb;
    }
    
    
    private Distribution createRemainderDistribution(Distribution originalDist, int receiverId) {
        Distribution remainderDistribution = new Distribution();
        // Copy most fields from the original distribution
        mapExistingToNewDistribution(remainderDistribution, originalDist);
        
        // Set specific fields for the remainder
        remainderDistribution.setIssued_to_emp_id(receiverId); // Stays with the OLD receiver
        remainderDistribution.setIsActive(1);
        
        // Note: The range and count will be set by the caller (updateDgmToCampusForm)
        return remainderDistribution;
    }
}