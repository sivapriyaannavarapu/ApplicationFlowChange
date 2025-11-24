//package com.application.service;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.application.dto.AppNumberRangeDTO;
//import com.application.dto.EmployeeApplicationsDTO;
//import com.application.dto.FormSubmissionDTO;
//import com.application.dto.GenericDropdownDTO;
//import com.application.entity.BalanceTrack;
//import com.application.entity.Campus;
//import com.application.entity.Distribution;
//import com.application.entity.Employee;
//import com.application.entity.Zone;
//import com.application.repository.AcademicYearRepository;
//import com.application.repository.AppIssuedTypeRepository;
//import com.application.repository.BalanceTrackRepository;
//import com.application.repository.CampusRepository;
//import com.application.repository.CityRepository;
//import com.application.repository.DistributionRepository;
//import com.application.repository.EmployeeRepository;
//import com.application.repository.ZoneRepository;
//
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
// 
//@Service
//@RequiredArgsConstructor
//public class DgmService {
//    private final AcademicYearRepository academicYearRepository;
//    private final CityRepository cityRepository;
//    private final ZoneRepository zoneRepository;
//    private final CampusRepository campusRepository;
//    private final AppIssuedTypeRepository appIssuedTypeRepository;
//    private final DistributionRepository distributionRepository;
//    private final EmployeeRepository employeeRepository;
//    private final BalanceTrackRepository balanceTrackRepository;
// 
//    public List<GenericDropdownDTO> getAllAcademicYears() {
//        return academicYearRepository.findAll().stream()
//                .map(year -> new GenericDropdownDTO(year.getAcdcYearId(), year.getAcademicYear()))
//                .collect(Collectors.toList());
//    }
// 
//    public List<GenericDropdownDTO> getAllCities() {
//        return cityRepository.findAll().stream()
//                .map(city -> new GenericDropdownDTO(city.getCityId(), city.getCityName()))
//                .collect(Collectors.toList());
//    }
//    
//    public List<Zone> findAllZones(){
//        return zoneRepository.findAll();
//    }
//    
//    public List<Campus> fetchAllCampuses(){
//        return campusRepository.findAll();
//    }
// 
//    public List<GenericDropdownDTO> getZonesByCityId(int cityId) {
//        return zoneRepository.findByCityCityId(cityId).stream()
//                .map(zone -> new GenericDropdownDTO(zone.getZoneId(), zone.getZoneName()))
//                .collect(Collectors.toList());
//    }
// 
//    public List<GenericDropdownDTO> getCampusesByZoneId(int zoneId) {
//        return campusRepository.findByZoneZoneId(zoneId).stream()
//                .map(campus -> new GenericDropdownDTO(campus.getCampusId(), campus.getCampusName()))
//                .collect(Collectors.toList());
//    }
// 
//    public List<GenericDropdownDTO> getAllIssuedToTypes() {
//        return appIssuedTypeRepository.findAll().stream()
//                .map(type -> new GenericDropdownDTO(type.getAppIssuedId(), type.getTypeName()))
//                .collect(Collectors.toList());
//    }
// 
//    public List<AppNumberRangeDTO> getAvailableAppNumberRanges(int academicYearId, int employeeId) {
//        return balanceTrackRepository.findAppNumberRanges(academicYearId, employeeId).stream()
//                .map(range -> new AppNumberRangeDTO(range.getAppBalanceTrkId(), range.getAppFrom(), range.getAppTo()))
//                .collect(Collectors.toList());
//    }
// 
//    public String getMobileNumberByEmpId(int empId) {
//        return employeeRepository.findMobileNoByEmpId(empId);
//    }
//    
//    private int getIssuedTypeByUserId(int userId) {
//        // TODO: Implement logic to find the issuer's type ID based on their role
//        // For now, hardcoding '2' for Zonal Officer as an example
//        return 2;
//    }
//    
//    @Transactional
//    public void submitForm(FormSubmissionDTO formDto) {
//        int issuerUserId = formDto.getUserId(); // This is the Zonal Officer
//        int receiverEmpId = formDto.getDgmEmployeeId(); // This is the DGM
//        int issuedById = getIssuedTypeByUserId(issuerUserId); // Get issuer's role ID
//        int appNoFrom = Integer.parseInt(formDto.getApplicationNoFrom());
//        int appNoTo = Integer.parseInt(formDto.getApplicationNoTo());
// 
//        // --- Part 1: Update the Zonal Officer's (Issuer's) Balance ---
//        BalanceTrack issuerBalance = balanceTrackRepository.findById(formDto.getSelectedBalanceTrackId())
//                .orElseThrow(() -> new RuntimeException("The selected application range for the issuer was not found."));
// 
//        // Validate the submitted range is within the issuer's selected balance track
//        if (appNoFrom < issuerBalance.getAppFrom() || appNoTo > issuerBalance.getAppTo()) {
//            throw new IllegalStateException("The submitted application number range is outside the issuer's available range.");
//        }
//        
//        // This simple logic assumes ranges are used sequentially from the start.
//        // A more complex system could split the BalanceTrack record into two if a middle chunk is taken.
//        issuerBalance.setAppAvblCnt(issuerBalance.getAppAvblCnt() - formDto.getRange());
//        issuerBalance.setAppFrom(appNoTo + 1);
//        balanceTrackRepository.save(issuerBalance);
// 
//        // --- Part 2: Create or Update the DGM's (Receiver's) Balance ---
//        Optional<BalanceTrack> existingReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(formDto.getAcademicYearId(), receiverEmpId);
//        BalanceTrack receiverBalance;
//        if (existingReceiverBalanceOpt.isPresent()) {
//            // If the DGM's balance exists, UPDATE it
//            receiverBalance = existingReceiverBalanceOpt.get();
//            receiverBalance.setAppAvblCnt(receiverBalance.getAppAvblCnt() + formDto.getRange());
//            receiverBalance.setAppTo(appNoTo);
//        } else {
//            // If the DGM's balance does NOT exist, CREATE it
//            receiverBalance = new BalanceTrack();
//            Employee receiverEmployee = employeeRepository.findById(receiverEmpId)
//                    .orElseThrow(() -> new RuntimeException("Receiver DGM employee not found for ID: " + receiverEmpId));
//            
//            receiverBalance.setEmployee(receiverEmployee);
//            receiverBalance.setAcademicYear(academicYearRepository.findById(formDto.getAcademicYearId()).orElse(null));
//            receiverBalance.setAppFrom(appNoFrom);
//            receiverBalance.setAppTo(appNoTo);
//            receiverBalance.setAppAvblCnt(formDto.getRange());
//            receiverBalance.setIssuedByType(appIssuedTypeRepository.findById(formDto.getIssuedToId()).orElse(null));
//            receiverBalance.setIsActive(1);
//            receiverBalance.setCreatedBy(issuerUserId);
//        }
//        balanceTrackRepository.save(receiverBalance);
// 
//        // --- Part 3: Save the Distribution Record ---
//        Distribution distribution = new Distribution();
//        
//        academicYearRepository.findById(formDto.getAcademicYearId()).ifPresent(distribution::setAcademicYear);
//        zoneRepository.findById(formDto.getZoneId()).ifPresent(distribution::setZone);
//        campusRepository.findById(formDto.getCampusId()).ifPresent(distribution::setCampus);
//        cityRepository.findById(formDto.getCityId()).ifPresent(city -> {
//            distribution.setCity(city);
//            if (city.getDistrict() != null) {
//                distribution.setDistrict(city.getDistrict());
//                if (city.getDistrict().getState() != null) {
//                    distribution.setState(city.getDistrict().getState());
//                }
//            }
//        });
//        
//        appIssuedTypeRepository.findById(issuedById).ifPresent(distribution::setIssuedByType);
//        appIssuedTypeRepository.findById(formDto.getIssuedToId()).ifPresent(distribution::setIssuedToType);
//        
//        distribution.setAppStartNo(appNoFrom);
//        distribution.setAppEndNo(appNoTo);
//        distribution.setTotalAppCount(formDto.getRange());
//        distribution.setIssueDate(LocalDate.now());
//        distribution.setIsActive(1);
//        distribution.setCreated_by(issuerUserId);
//        distribution.setIssued_to_emp_id(receiverEmpId);
//        
//        distributionRepository.save(distribution);
//    }
//    
// // ---------------------- NEW METHOD ----------------------
//    public EmployeeApplicationsDTO getEmployeeAvailableApplications(int academicYearId, int employeeId) {
//
//        List<BalanceTrack> balances = balanceTrackRepository.findAppNumberRanges(academicYearId, employeeId);
//
//        if (balances.isEmpty()) {
//            throw new RuntimeException("No available applications found for employeeId: " + employeeId);
//        }
//
//        List<Integer> availableApplications = new ArrayList<>();
//
//        for (BalanceTrack balance : balances) {
//            int from = balance.getAppFrom();
//            int to = balance.getAppTo();
//
//            for (int i = from; i <= to; i++) {
//                availableApplications.add(i);
//            }
//        }
//
//        // ✅ Pass all 3 args because @AllArgsConstructor expects them
//        return new EmployeeApplicationsDTO(employeeId, availableApplications, availableApplications.size());
//    }
//
//
//    
//    //---------------------- NEW METHOD FOR UPDATING A RECORD ----------------------
//    @Transactional
//    public void updateForm(@NonNull Integer distributionId, @NonNull FormSubmissionDTO formDto) {
//        Distribution existingDistribution = distributionRepository.findById(distributionId)
//                .orElseThrow(() -> new RuntimeException("Distribution record not found with ID: " + distributionId));
// 
//        int oldRange = existingDistribution.getTotalAppCount();
//        int oldIssuedToEmpId = existingDistribution.getIssued_to_emp_id();
//        
//        int newAppFrom = Integer.parseInt(formDto.getApplicationNoFrom());
//        int newAppTo = Integer.parseInt(formDto.getApplicationNoTo());
//        int newRange = formDto.getRange();
//        
//        int issuerUserId = formDto.getUserId();
//        int newReceiverEmpId = formDto.getDgmEmployeeId();
//        int academicYearId = formDto.getAcademicYearId();
// 
//        // Step 1: Handle balance updates for the old recipient.
//        // This is a crucial fix to avoid data inconsistency.
//        // We first find the old balance track and deduct the original range.
//        Optional<BalanceTrack> oldReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, oldIssuedToEmpId);
//        if (oldReceiverBalanceOpt.isPresent()) {
//            BalanceTrack oldReceiverBalance = oldReceiverBalanceOpt.get();
//            oldReceiverBalance.setAppAvblCnt(oldReceiverBalance.getAppAvblCnt() - oldRange);
//            // We do not need to update appTo here, as it will be handled by the update of the new record if there is a change.
//            balanceTrackRepository.save(oldReceiverBalance);
//        } else {
//            throw new RuntimeException("Old receiver's balance track not found for update.");
//        }
// 
//        // Step 2: Update the new receiver's balance.
//        Optional<BalanceTrack> newReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, newReceiverEmpId);
//        BalanceTrack newReceiverBalance;
//        if (newReceiverBalanceOpt.isPresent()) {
//            newReceiverBalance = newReceiverBalanceOpt.get();
//            newReceiverBalance.setAppAvblCnt(newReceiverBalance.getAppAvblCnt() + newRange);
//            newReceiverBalance.setAppTo(newAppTo);
//        } else {
//            newReceiverBalance = new BalanceTrack();
//            newReceiverBalance.setEmployee(employeeRepository.findById(newReceiverEmpId).orElseThrow(() -> new RuntimeException("New receiver employee not found.")));
//            newReceiverBalance.setAcademicYear(academicYearRepository.findById(academicYearId).orElseThrow(() -> new RuntimeException("Academic year not found.")));
//            newReceiverBalance.setAppFrom(newAppFrom);
//            newReceiverBalance.setAppTo(newAppTo);
//            newReceiverBalance.setAppAvblCnt(newRange);
//            newReceiverBalance.setIssuedByType(appIssuedTypeRepository.findById(formDto.getIssuedToId()).orElseThrow(() -> new RuntimeException("Issued to type not found.")));
//            newReceiverBalance.setIsActive(1);
//            newReceiverBalance.setCreatedBy(issuerUserId);
//        }
//        balanceTrackRepository.save(newReceiverBalance);
// 
//        // Step 3: Update the main distribution record with the new values.
//        existingDistribution.setAcademicYear(academicYearRepository.findById(academicYearId).orElse(null));
//        existingDistribution.setZone(zoneRepository.findById(formDto.getZoneId()).orElse(null));
//        existingDistribution.setCampus(campusRepository.findById(formDto.getCampusId()).orElse(null));
//        existingDistribution.setCity(cityRepository.findById(formDto.getCityId()).orElse(null));
//        
//        // This logic implicitly updates the state and district from the city object
//        cityRepository.findById(formDto.getCityId()).ifPresent(city -> {
//             existingDistribution.setCity(city);
//             if (city.getDistrict() != null) {
//                 existingDistribution.setDistrict(city.getDistrict());
//                 if (city.getDistrict().getState() != null) {
//                     existingDistribution.setState(city.getDistrict().getState());
//                 }
//             }
//         });
//        
//        existingDistribution.setAppStartNo(newAppFrom);
//        existingDistribution.setAppEndNo(newAppTo);
//        existingDistribution.setTotalAppCount(newRange);
//        existingDistribution.setIssueDate(LocalDate.now());
//        existingDistribution.setCreated_by(issuerUserId);
//        existingDistribution.setIssued_to_emp_id(newReceiverEmpId);
//        
//        distributionRepository.save(existingDistribution);
//    }
//}


//package com.application.service;

package com.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.dto.AppDistributionDTO;
import com.application.dto.AppFromDTO;
import com.application.dto.AppNumberRangeDTO;
import com.application.dto.AppRangeDTO;
import com.application.dto.EmployeeApplicationsDTO;
import com.application.dto.FormSubmissionDTO;
import com.application.dto.GenericDropdownDTO;
import com.application.entity.BalanceTrack;
import com.application.entity.Campus;
import com.application.entity.Dgm;
import com.application.entity.Distribution;
import com.application.entity.UserAdminView;
import com.application.entity.Zone;
import com.application.repository.AcademicYearRepository;
import com.application.repository.AppIssuedTypeRepository;
import com.application.repository.BalanceTrackRepository;
import com.application.repository.CampusRepository;
import com.application.repository.CityRepository;
import com.application.repository.DgmRepository;
import com.application.repository.DistributionRepository;
import com.application.repository.EmployeeRepository;
import com.application.repository.UserAdminViewRepository;
import com.application.repository.ZonalAccountantRepository;
import com.application.repository.ZoneRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DgmService {

    private final AcademicYearRepository academicYearRepository;
    private final CityRepository cityRepository;
    private final ZoneRepository zoneRepository;
    private final CampusRepository campusRepository;
    private final AppIssuedTypeRepository appIssuedTypeRepository;
    private final DistributionRepository distributionRepository;
    private final EmployeeRepository employeeRepository;
    private final BalanceTrackRepository balanceTrackRepository;
    private final DgmRepository dgmRepository;
    private final UserAdminViewRepository userAdminViewRepository;
    private final ZonalAccountantRepository zonalAccountantRepository;

    // --- Dropdown and Helper Methods with Caching ---
//    @Cacheable("academicYears")
    public List<GenericDropdownDTO> getAllAcademicYears() {
        return academicYearRepository.findAll().stream()
                .map(year -> new GenericDropdownDTO(year.getAcdcYearId(), year.getAcademicYear()))
                .collect(Collectors.toList());
    }

    @Cacheable("cities")
    public List<GenericDropdownDTO> getAllCities() {
        final int ACTIVE_STATUS = 1;
        
        return cityRepository.findByStatus(ACTIVE_STATUS).stream()
                .map(city -> new GenericDropdownDTO(city.getCityId(), city.getCityName()))
                .collect(Collectors.toList());
    }

//    @Cacheable("zones")
    public List<Zone> findAllZones() {
        return zoneRepository.findAll();
    }

//    @Cacheable("campuses")
    public List<Campus> fetchAllCampuses() {
        return campusRepository.findAll();
    }

//    @Cacheable(cacheNames = "zonesByCity", key = "#cityId")
    public List<GenericDropdownDTO> getZonesByCityId(int cityId) {
        return zoneRepository.findByCityCityId(cityId).stream()
                .map(zone -> new GenericDropdownDTO(zone.getZoneId(), zone.getZoneName()))
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "campusesByZone", key = "#zoneId")
    public List<GenericDropdownDTO> getCampusesByZoneId(int zoneId) {
        // Call the new repository method
        return campusRepository.findActiveCampusesByZoneId(zoneId).stream()
                // .distinct() might be useful here to ensure unique campuses if a campus is linked to multiple active zonal accountants in the same zone
                .distinct() 
                .map(campus -> new GenericDropdownDTO(campus.getCampusId(), campus.getCampusName()))
                .collect(Collectors.toList());
    }
    
  @Cacheable(cacheNames = "campusforzonalaccountant", key = "#empId")
    public List<GenericDropdownDTO> getCampusesByEmployeeId(int empId) {
    	List<Integer> zoneIds = zonalAccountantRepository.findZoneIdByEmployeeId(empId);
        if (zoneIds == null || zoneIds.isEmpty()) {
            return Collections.emptyList();
        }

        return zoneIds.stream()
                .flatMap(zoneId -> getCampusesByZoneId(zoneId).stream())
                .distinct()
                .collect(Collectors.toList());
    }
    
    public List<GenericDropdownDTO> getActiveCampusesByEmpId(Integer empId) {
        List<Dgm> dgms = dgmRepository.findByEmpId(empId);

        // Map to GenericDropdownDTO — filtering only active campuses
        return dgms.stream()
                .filter(d -> d.getCampus() != null && d.getCampus().getIsActive() != null && d.getCampus().getIsActive() == 1)
                .map(d -> new GenericDropdownDTO(
                        d.getCampus().getCampusId(),
                        d.getCampus().getCampusName()
                ))
                .collect(Collectors.toList());
    }
    
    public List<GenericDropdownDTO> getActiveCampusesByEmployeeId(int empId) {
        List<Integer> zoneIds = zonalAccountantRepository.findZoneIdByEmployeeId(empId);
        if (zoneIds == null || zoneIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Step 2: Get campus IDs for all zones
        List<Integer> campusIds = dgmRepository.findCampusIdsByZoneIds(zoneIds);
        if (campusIds == null || campusIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Step 3: Get only active campuses
        return campusRepository.findActiveCampusesByIds(campusIds);
    }

    
    

//    @Cacheable("issuedToTypes")
    public List<GenericDropdownDTO> getAllIssuedToTypes() {
        return appIssuedTypeRepository.findAll().stream()
                .map(type -> new GenericDropdownDTO(type.getAppIssuedId(), type.getTypeName()))
                .collect(Collectors.toList());
    }

//    @Cacheable(cacheNames = "availableAppNumberRanges", key = "{#academicYearId, #employeeId}")
    public List<AppNumberRangeDTO> getAvailableAppNumberRanges(int academicYearId, int employeeId) {
        return balanceTrackRepository.findAppNumberRanges(academicYearId, employeeId).stream()
                .map(range -> new AppNumberRangeDTO(range.getAppBalanceTrkId(), range.getAppFrom(), range.getAppTo()))
                .collect(Collectors.toList());
    }

//    @Cacheable(cacheNames = "employeeApplications", key = "{#academicYearId, #employeeId}")
    public EmployeeApplicationsDTO getEmployeeAvailableApplications(int academicYearId, int employeeId) {
        List<BalanceTrack> balances = balanceTrackRepository.findAppNumberRanges(academicYearId, employeeId);
        if (balances.isEmpty()) {
            throw new RuntimeException("No available applications found for employeeId: " + employeeId);
        }
        List<Integer> availableApplications = new ArrayList<>();
        // NOTE: This implementation for availableApplications is highly inefficient for large ranges.
        // It should ideally be calculated based on the difference between total received and total distributed.
        for (BalanceTrack balance : balances) {
            for (int i = balance.getAppFrom(); i <= balance.getAppTo(); i++) {
                availableApplications.add(i);
            }
        }
        return new EmployeeApplicationsDTO(employeeId, availableApplications, availableApplications.size());
    }

    @Cacheable(value = "mobileNumberByEmpId", key = "#empId")
    public String getMobileNumberByEmpId(int empId) {
        return employeeRepository.findMobileNoByEmpId(empId);
    }
    
    
  @Cacheable(cacheNames = "getDgmforCampus", key = "#campusId")
    public List<GenericDropdownDTO> getDgmEmployeesForCampus(int campusId) {
        // 1. Find the Campus by ID
        Optional<Campus> campusOptional = campusRepository.findById(campusId);

        if (campusOptional.isEmpty()) {
            return Collections.emptyList(); 
        }

        // 2. Get the Zone ID from the Campus
        Campus campus = campusOptional.get();
        int zoneId = campus.getZone().getZoneId(); 

        // 3. Find distinct DGM employees for that Zone, checking isActive = 1
        // Calling the updated repository method:
        return dgmRepository.findDistinctActiveEmployeesByZoneId(zoneId); 
    }
    
    public Optional<AppDistributionDTO> getActiveAppRange(int issuedToEmpId, int academicYearId) {
        // Pass '1' explicitly for the isActive condition
        return distributionRepository.findActiveAppRangeByEmployeeAndAcademicYear(
            issuedToEmpId, 
            academicYearId
        );
    }
    
    
    
//    public Optional<AppFromDTO> getAppFromByEmployeeAndYear(int employeeId, int academicYearId) {
//        return balanceTrackRepository.getAppFromByEmployeeAndAcademicYear(employeeId, academicYearId);
//        
//        // OR if using the @Query method:
//        // return balanceTrackRepository.getAppFromByEmployeeAndAcademicYear(employeeId, academicYearId);
//    }
    
//  @Cacheable(cacheNames = "getAppRange", key = "{#academicYearId, #employeeId}")
//    public AppRangeDTO getAppRange(int empId, int academicYearId) {
//        // Fetch distribution data
//        AppDistributionDTO distDTO = distributionRepository
//                .findActiveAppRangeByEmployeeAndAcademicYear(empId, academicYearId)
//                .orElse(null);
//
//        // Fetch balance track data (now returns AppFromDTO with the ID)
//        AppFromDTO fromDTO = balanceTrackRepository
//                .getAppFromByEmployeeAndAcademicYear(empId, academicYearId)
//                .orElse(null);
//
//        if (distDTO == null && fromDTO == null) {
//            return null; 
//        }
//
//        // Merge results into a single DTO
//        Integer appStartNo = distDTO != null ? distDTO.getAppStartNo() : null;
//        Integer appEndNo = distDTO != null ? distDTO.getAppEndNo() : null;
//        
//        // Extract fields from the updated AppFromDTO
//        Integer appFrom = fromDTO != null ? fromDTO.getAppFrom() : null;
//        Integer appBalanceTrkId = fromDTO != null ? fromDTO.getAppBalanceTrkId() : null; // Extracted new ID
//
//        // Use the updated AppRangeDTO constructor
//        return new AppRangeDTO(appStartNo, appEndNo, appFrom, appBalanceTrkId);
//    }
    
//    public AppRangeDTO getAppRange(int empId, int academicYearId, Integer cityId) { // Updated signature
//        // Fetch distribution data
//        AppDistributionDTO distDTO = distributionRepository
//                .findActiveAppRangeByEmployeeAndAcademicYear(empId, academicYearId, cityId) // Pass cityId
//                .orElse(null);
//
//        // Fetch balance track data (now returns AppFromDTO with the ID)
//        AppFromDTO fromDTO = balanceTrackRepository
//                .getAppFromByEmployeeAndAcademicYear(empId, academicYearId)
//                .orElse(null);
//
//        if (distDTO == null && fromDTO == null) {
//            return null; 
//        }
//
//        // Merge results into a single DTO
//        Integer appStartNo = distDTO != null ? distDTO.getAppStartNo() : null;
//        Integer appEndNo = distDTO != null ? distDTO.getAppEndNo() : null;
//        
//        // Extract fields from the updated AppFromDTO
//        Integer appFrom = fromDTO != null ? fromDTO.getAppFrom() : null;
//        Integer appBalanceTrkId = fromDTO != null ? fromDTO.getAppBalanceTrkId() : null; // Extracted new ID
//
//        // Use the updated AppRangeDTO constructor
//        return new AppRangeDTO(appStartNo, appEndNo, appFrom, appBalanceTrkId);
//    }
//    

    private int getIssuedTypeByUserId(int userId) {
    	List<UserAdminView> userRoles = userAdminViewRepository.findByEmpIdUsingQuery(userId);

        if (userRoles.isEmpty()) {
            throw new RuntimeException("Issuer details not found for employee ID: " + userId + ". Cannot determine role.");
        }
        
        // 2. Define the priority mapping and find the highest one (lowest ID)
        int highestPriorityTypeId = Integer.MAX_VALUE;
        String highestPriorityRoleName = null;

        for (UserAdminView userView : userRoles) {
            String roleName = userView.getRole_name();
            String normalizedRoleName = roleName.toUpperCase().trim();
            
            int currentTypeId = switch (normalizedRoleName) {
                case "ADMIN" -> 1;
                case "ZONAL ACCOUNTANT" -> 2;
                case "DGM" -> 3;
                default -> -1; // Ignore unsupported roles for distribution purposes
            };

            // 3. Select the role with the lowest Type ID (1 is highest priority)
            if (currentTypeId != -1 && currentTypeId < highestPriorityTypeId) {
                highestPriorityTypeId = currentTypeId;
                highestPriorityRoleName = roleName; // For logging/debugging if needed
            }
        }

        // 4. Final check and return
        if (highestPriorityTypeId == Integer.MAX_VALUE) {
            // This means the user had roles, but none of them were Admin, Zonal Accountant, or DGM.
            throw new IllegalArgumentException("Employee ID: " + userId + " holds roles, but none are authorized to issue applications.");
        }
        
        // Return the ID of the highest priority role found (1, 2, or 3)
        return highestPriorityTypeId;
    }

    // ----------------------------------------
    // --- CORE FORM SUBMISSION METHODS with Cache Eviction ---
    // ----------------------------------------

    @Transactional
//    @CacheEvict(value = {
//            "availableAppNumberRanges",
//            "employeeApplications",
//            "nextAppNumber",
//            "balanceTrack"
//    }, allEntries = true)
    public void submitForm(@NonNull FormSubmissionDTO formDto) {
        int issuerUserId = formDto.getUserId();
        int receiverEmpId = formDto.getDgmEmployeeId();
        int issuedById = getIssuedTypeByUserId(issuerUserId);

        // Create and save the new distribution
        Distribution distribution = new Distribution();
        mapDtoToDistribution(distribution, formDto, issuedById);
        distributionRepository.save(distribution);

        // Recalculate balances
        // Recalculate for the ISSUER (who loses applications)
        recalculateBalanceForEmployee(issuerUserId, formDto.getAcademicYearId(), issuedById, issuerUserId);
        
        // Recalculate for the RECEIVER (who gains applications)
        recalculateBalanceForEmployee(receiverEmpId, formDto.getAcademicYearId(), formDto.getIssuedToId(), issuerUserId);
    }
    /**
     * Revised method to implement the inactivation-and-insert pattern for updates.
     */
    @Transactional
//    @CacheEvict(value = {
//            "availableAppNumberRanges",
//            "employeeApplications",
//            "nextAppNumber",
//            "balanceTrack"
//    }, allEntries = true)
    public void updateForm(@NonNull Integer distributionId, @NonNull FormSubmissionDTO formDto) {
        Distribution existingDistribution = distributionRepository.findById(distributionId)
                .orElseThrow(() -> new RuntimeException("Distribution record not found with ID: " + distributionId));

        int oldReceiverId = existingDistribution.getIssued_to_emp_id();
        int issuerId = formDto.getUserId();
        int academicYearId = formDto.getAcademicYearId();

        int oldStart = existingDistribution.getAppStartNo();
        int oldEnd = existingDistribution.getAppEndNo();
        int newStart = Integer.parseInt(formDto.getApplicationNoFrom());
        int newEnd = Integer.parseInt(formDto.getApplicationNoTo());

        boolean isRecipientChanging = oldReceiverId != formDto.getDgmEmployeeId();
        boolean isRangeChanging = oldStart != newStart || oldEnd != newEnd;

        // 1. Inactivate the original record
        existingDistribution.setIsActive(0);
        distributionRepository.save(existingDistribution);

        // 2. Insert new record for the requested range
        Distribution newRequestedDistribution = new Distribution();
        mapDtoToDistribution(newRequestedDistribution, formDto, getIssuedTypeByUserId(issuerId));
        distributionRepository.save(newRequestedDistribution);

        // 3. Handle the remainder (only if the range changed)
        if (isRangeChanging) {
            // Handle the part of the OLD range that is BEFORE the NEW range, if any
            if (oldStart < newStart) {
                Distribution beforeRemainder = createRemainderDistribution(existingDistribution, oldReceiverId);
                beforeRemainder.setAppStartNo(oldStart);
                beforeRemainder.setAppEndNo(newStart - 1);
                beforeRemainder.setTotalAppCount(beforeRemainder.getAppEndNo() - beforeRemainder.getAppStartNo() + 1);
                distributionRepository.save(beforeRemainder);
            }
            
            // Handle the part of the OLD range that is AFTER the NEW range, if any
            if (oldEnd > newEnd) {
                Distribution afterRemainder = createRemainderDistribution(existingDistribution, oldReceiverId);
                afterRemainder.setAppStartNo(newEnd + 1);
                afterRemainder.setAppEndNo(oldEnd);
                afterRemainder.setTotalAppCount(afterRemainder.getAppEndNo() - afterRemainder.getAppStartNo() + 1);
                distributionRepository.save(afterRemainder);
            }
        }

        // 4. Recalculate Balances
        int issuedById = getIssuedTypeByUserId(issuerId);
        recalculateBalanceForEmployee(issuerId, academicYearId, issuedById, issuerId);
        recalculateBalanceForEmployee(formDto.getDgmEmployeeId(), academicYearId, formDto.getIssuedToId(), issuerId);

        // Recalculate for the old receiver if they lost applications
        if (isRecipientChanging || isRangeChanging) {
            balanceTrackRepository.findBalanceTrack(academicYearId, oldReceiverId).ifPresent(oldBalance -> {
                int oldReceiverTypeId = oldBalance.getIssuedByType().getAppIssuedId();
                recalculateBalanceForEmployee(oldReceiverId, academicYearId, oldReceiverTypeId, issuerId);
            });
        }
    }

    // --- PRIVATE HELPER METHODS ---

    /**
     * Revised to use inactivation and insertion instead of update/delete.
     */
    private void handleOverlappingDistributions(List<Distribution> overlappingDists, FormSubmissionDTO request) {
        int newStart = Integer.parseInt(request.getApplicationNoFrom());
        int newEnd = Integer.parseInt(request.getApplicationNoTo());

        for (Distribution oldDist : overlappingDists) {
            int oldReceiverId = oldDist.getIssued_to_emp_id();
            if (oldReceiverId == request.getDgmEmployeeId()) continue;

            int oldStart = oldDist.getAppStartNo();
            int oldEnd = oldDist.getAppEndNo();
            
            // 1. Deactivate the entire original record (replaces delete/update)
            oldDist.setIsActive(0);
            distributionRepository.save(oldDist); 

            // 2. Create a new record for the non-overlapping part BEFORE the new range (if any)
            if (oldStart < newStart) {
                Distribution beforeSplit = createRemainderDistribution(oldDist, oldReceiverId);
                beforeSplit.setAppStartNo(oldStart);
                beforeSplit.setAppEndNo(newStart - 1);
                beforeSplit.setTotalAppCount(beforeSplit.getAppEndNo() - beforeSplit.getAppStartNo() + 1);
                distributionRepository.save(beforeSplit);
            }

            // 3. Create a new record for the non-overlapping part AFTER the new range (if any)
            if (oldEnd > newEnd) {
                Distribution afterSplit = createRemainderDistribution(oldDist, oldReceiverId);
                afterSplit.setAppStartNo(newEnd + 1);
                afterSplit.setAppEndNo(oldEnd);
                afterSplit.setTotalAppCount(afterSplit.getAppEndNo() - afterSplit.getAppStartNo() + 1);
                distributionRepository.save(afterSplit);
            }
            
            // 4. Recalculate balance for the employee who lost applications
            recalculateBalanceForEmployee(oldReceiverId, request.getAcademicYearId(), oldDist.getIssuedToType().getAppIssuedId(), request.getUserId());
        }
    }

 // REVISED recalculateBalanceForEmployee in DgmService
    private void recalculateBalanceForEmployee(int employeeId, int academicYearId, int typeId, int createdBy) {
    	
    	if (typeId == 1 || employeeId == createdBy) {
            // You can log it if you want:
            System.out.println("Skipping balance recalculation for Admin/self-issued record. Employee ID: " + employeeId);
            return;
        }
        
        // Finds existing BalanceTrack or creates a new one (AppBalanceTrkId will be 0 or null before save)
        BalanceTrack balance = balanceTrackRepository.findBalanceTrack(academicYearId, employeeId)
                .orElseGet(() -> createNewBalanceTrack(employeeId, academicYearId, typeId, createdBy));

        // 1. Total Applications Received by this employee (all active distributions issued TO them)
        Integer totalReceived = distributionRepository.sumTotalAppCountByIssuedToEmpId(employeeId, academicYearId).orElse(0);

        // 2. Total Applications Distributed/Issued BY this employee (all active distributions created BY them)
        Integer totalDistributed = distributionRepository.sumTotalAppCountByCreatedBy(employeeId, academicYearId).orElse(0);

        // 3. Available balance is Received - Distributed
        int availableCount = totalReceived - totalDistributed;

        // 4. Calculate current min/max range of received
        // These queries return the actual min/max number from the Distribution table
        Integer minReceived = distributionRepository.findMinAppStartNoByIssuedToEmpIdAndAcademicYearId(employeeId, academicYearId).orElse(0);
        Integer maxReceived = distributionRepository.findMaxAppEndNoByIssuedToEmpIdAndAcademicYearId(employeeId, academicYearId).orElse(0);

        // 5. Find the maximum end number the employee has distributed (issued BY them)
        Integer maxDistributed = distributionRepository.findMaxAppEndNoByCreatedByAndAcademicYearId(employeeId, academicYearId).orElse(0);

        // 6. Adjust the available range (this logic is executed for both existing and new records)
        if (maxDistributed != null && maxDistributed >= minReceived && maxDistributed < maxReceived) {
            // Employee has distributed some, so start is max distributed + 1
            balance.setAppFrom(maxDistributed + 1); 
            balance.setAppTo(maxReceived);
        } else {
            // Employee has distributed none, or all distributed were outside the current range.
            // Set the full received range.
            balance.setAppFrom(minReceived);
            balance.setAppTo(maxReceived);
        }

        balance.setAppAvblCnt(availableCount);
        // Saving the record creates a new AppBalanceTrkId if it didn't exist, and posts the correct appFrom/appTo
        balanceTrackRepository.save(balance); 
    }

    /**
     * Helper to create a new active Distribution record based on an existing one
     * but setting the new IssuedToEmpId (the old receiver) and setting IsActive=1.
     */
    private Distribution createRemainderDistribution(Distribution originalDist, int receiverId) {
        Distribution remainderDistribution = new Distribution();
        // Copy most fields from the original distribution
        mapExistingToNewDistribution(remainderDistribution, originalDist);
        
        // Set specific fields for the remainder
        remainderDistribution.setIssued_to_emp_id(receiverId); // Stays with the OLD receiver
        remainderDistribution.setIsActive(1);
        
        // Note: The range and count will be set by the caller
        return remainderDistribution;
    }

    private void mapDtoToDistribution(Distribution distribution, FormSubmissionDTO formDto, int issuedById) {
        int appNoFrom = Integer.parseInt(formDto.getApplicationNoFrom());
        int appNoTo = Integer.parseInt(formDto.getApplicationNoTo());

        academicYearRepository.findById(formDto.getAcademicYearId()).ifPresent(distribution::setAcademicYear);
        zoneRepository.findById(formDto.getZoneId()).ifPresent(distribution::setZone);
        campusRepository.findById(formDto.getCampusId()).ifPresent(distribution::setCampus);
        cityRepository.findById(formDto.getCityId()).ifPresent(city -> {
            distribution.setCity(city);
            if (city.getDistrict() != null) {
                distribution.setDistrict(city.getDistrict());
                if (city.getDistrict().getState() != null) {
                    // Assuming State is available via District
                    // NOTE: State is NOT in FormSubmissionDTO, deriving from City->District->State
                    distribution.setState(city.getDistrict().getState()); 
                }
            }
        });

        appIssuedTypeRepository.findById(issuedById).ifPresent(distribution::setIssuedByType);
        appIssuedTypeRepository.findById(formDto.getIssuedToId()).ifPresent(distribution::setIssuedToType);

        distribution.setAppStartNo(appNoFrom);
        distribution.setAppEndNo(appNoTo);
        distribution.setTotalAppCount(formDto.getRange());
        distribution.setIssueDate(LocalDate.now());
        distribution.setIsActive(1);
        distribution.setCreated_by(formDto.getUserId());
        distribution.setIssued_to_emp_id(formDto.getDgmEmployeeId());
    }

    private void mapExistingToNewDistribution(Distribution newDist, Distribution oldDist) {
        // This copies all essential metadata from the old record to the new one
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
    
}