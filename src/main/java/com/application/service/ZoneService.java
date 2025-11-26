//package com.application.service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.application.dto.ApplicationRangeInfoDTO;
//import com.application.dto.ApplicationStartEndDto;
//import com.application.dto.DistributionRequestDTO;
//import com.application.dto.EmployeesDto;
//import com.application.dto.NextAppNumberDTO;
//import com.application.entity.AcademicYear;
//import com.application.entity.BalanceTrack;
//import com.application.entity.City;
//import com.application.entity.Distribution;
//import com.application.entity.Employee;
//import com.application.entity.State;
//import com.application.entity.StateApp;
//import com.application.entity.ZonalAccountant;
//import com.application.entity.Zone;
//import com.application.repository.AcademicYearRepository;
//import com.application.repository.AppIssuedTypeRepository;
//import com.application.repository.BalanceTrackRepository;
//import com.application.repository.CityRepository;
//import com.application.repository.DistributionRepository;
//import com.application.repository.EmployeeRepository;
//import com.application.repository.StateAppRepository;
//import com.application.repository.StateRepository;
//import com.application.repository.ZonalAccountantRepository;
//import com.application.repository.ZoneRepository;
//
//import lombok.NonNull;
//
//@Service
//public class ZoneService {
//
//	private final AcademicYearRepository academicYearRepository;
//	private final StateRepository stateRepository;
//	private final CityRepository cityRepository;
//	private final ZoneRepository zoneRepository;
//	private final AppIssuedTypeRepository appIssuedTypeRepository;
//	private final EmployeeRepository employeeRepository;
//	private final StateAppRepository stateAppRepository;
//	private final BalanceTrackRepository balanceTrackRepository;
//	private final DistributionRepository distributionRepository;
//	private final ZonalAccountantRepository zonalAccountantRepository;
//
//	@Autowired
//	public ZoneService(AcademicYearRepository academicYearRepository, StateRepository stateRepository,
//			CityRepository cityRepository, ZoneRepository zoneRepository,
//			AppIssuedTypeRepository appIssuedTypeRepository, EmployeeRepository employeeRepository,
//			StateAppRepository stateAppRepository, BalanceTrackRepository balanceTrackRepository,
//			DistributionRepository distributionRepository, ZonalAccountantRepository zonalAccountantRepository) {
//		this.academicYearRepository = academicYearRepository;
//		this.stateRepository = stateRepository;
//		this.cityRepository = cityRepository;
//		this.zoneRepository = zoneRepository;
//		this.appIssuedTypeRepository = appIssuedTypeRepository;
//		this.employeeRepository = employeeRepository;
//		this.stateAppRepository = stateAppRepository;
//		this.balanceTrackRepository = balanceTrackRepository;
//		this.distributionRepository = distributionRepository;
//		this.zonalAccountantRepository = zonalAccountantRepository;
//	}
//
//	// --- Dropdown/Helper Methods with Caching ---
//    @Cacheable("academicYears")
//	public List<AcademicYear> getAllAcademicYears() {
//		return academicYearRepository.findAll();
//	}
//
//    @Cacheable("states")
//	public List<State> getAllStates() {
//		// Assumption: State entity has a field named 'is_active' or similar.
//		return stateRepository.findByStatus(1);
//	}
//
//    @Cacheable(cacheNames = "citiesByState", key = "#stateId")
//	public List<City> getCitiesByState(int stateId) {
//	    final int ACTIVE_STATUS = 1;
//	    return cityRepository.findByDistrictStateStateIdAndStatus(stateId, ACTIVE_STATUS);
//	}
//    @Cacheable(cacheNames = "zonesByCity", key = "#cityId")
//	public List<Zone> getZonesByCity(int cityId) {
//		return zoneRepository.findByCityCityId(cityId);
//	}
//
////    @Cacheable("issuableEmployees")
//	public List<Employee> getIssuableToEmployees() {
//		return employeeRepository.findAll();
//	}
//
////    @Cacheable(cacheNames = "appEndNo", key = "{#stateId, #userId}")
//	public Integer getAppEndNoForUser(int stateId, int userId) {
//		return stateAppRepository.findAppEndNoByStateAndUser(stateId, userId);
//	}
//
////    @Cacheable(cacheNames = "employeesByZone", key = "#zoneId")
////	public List<Employee> getEmployeesByZone(int zoneId) {
////		return zonalAccountantRepository.findByZoneZoneId(zoneId).stream().map(ZonalAccountant::getEmployee)
////				.collect(Collectors.toList());
////	}
//	
//  @Cacheable(cacheNames = "employeesByZone", key = "#zoneId")
//	@Transactional(readOnly = true)  // Recommended for lazy loading
//    public List<EmployeesDto> getEmployeesByZone(int zoneId) {
//        // Fetch only active zonal accountants (ZonalAccountant.isActive == 1)
//        List<ZonalAccountant> activeAccountants = zonalAccountantRepository
//            .findByZoneZoneIdAndIsActive(zoneId, 1);
//        
//        // Map and filter: Only include if Employee.isActive == 1
//        return activeAccountants.stream()
//            .map(this::mapToEmployeeDto)
//            .filter(Objects::nonNull)  // Skip null/inactive employees
//            .collect(Collectors.toList());
//    }
//
//    // Helper: Maps only if employee is active (filters but doesn't include in DTO)
//    private EmployeesDto mapToEmployeeDto(ZonalAccountant accountant) {
//        var employee = accountant.getEmployee();
//        // Filter: Skip if employee null or inactive
//        if (employee == null || employee.getIsActive() == null || employee.getIsActive() != 1) {
//            return null;
//        }
//        // Map without isActive
//        return new EmployeesDto(
//            employee.getEmp_id(),
//            employee.getFirst_name(),
//            employee.getLast_name(),
//            employee.getPrimary_mobile_no()
//        );
//    }
//	
//
////    @Cacheable(cacheNames = "nextAppNumber", key = "{#academicYearId, #stateId, #userId}")
//	public String getNextApplicationNumber(int academicYearId, int stateId, int userId) {
//		Integer lastAppNumber = distributionRepository.findMaxAppEndNo(stateId, userId, academicYearId);
//		if (lastAppNumber != null)
//			return String.valueOf(lastAppNumber + 1);
//		return stateAppRepository.findStartNumber(stateId, userId, academicYearId)
//				.map(sa -> String.valueOf(sa.getApp_start_no())).orElse("ERROR_NO_RANGE_ASSIGNED");
//	}
//
//	// @Cacheable(cacheNames = "appNumberRanges", key = "{#academicYearId, #stateId,
//	// #createdBy}")
//	public ApplicationStartEndDto getAppNumberRanges(int academicYearId, int stateId) {
//		return stateAppRepository.findAppNumberRanges(academicYearId, stateId)
//				.orElseThrow(() -> new RuntimeException("App ranges not found."));
//	}
//
//	public NextAppNumberDTO getNextApplicationNumberForTopIssuer(int academicYearId, int stateId) {
//
//		// 1. Try to find MAX(appEndNo) + 1 from active Distribution records
//		// (issuedByType = 1)
//		Optional<Integer> nextNumberOptional = distributionRepository
//				.findNextStartNumberByStateAndIssuerType(academicYearId, stateId);
//
//		Integer nextNumber;
//
//		if (nextNumberOptional.isPresent()) {
//			// 2. If records exist, use the calculated MAX(appEndNo) + 1
//			nextNumber = nextNumberOptional.get();
//		} else {
//			// 3. If NO records exist, fall back to the starting number from the StateApp
//			// table
//			// We use createdBy/stateId to find the assigned range for the top-level user.
//			ApplicationStartEndDto stateAppRange = getAppNumberRanges(academicYearId, stateId);
//
//			// Assuming ApplicationStartEndDto has a method/field named getAppFrom()
//			nextNumber = stateAppRange.getAppFrom();
//		}
//
//		return new NextAppNumberDTO(nextNumber);
//	}
//
////Assuming this method is in a service like ApplicationNumberService
//
//	public ApplicationRangeInfoDTO getApplicationNumberInfo(int academicYearId, int stateId) {
//
//		// 1. Get the overall assigned range (AppFrom and AppTo) from StateApp (required
//		// fallback data)
//		// This uses your original getAppNumberRanges logic.
//		ApplicationStartEndDto stateAppRange = stateAppRepository.findAppNumberRanges(academicYearId, stateId)
//				.orElseThrow(() -> new RuntimeException("Overall App ranges not found in StateApp table."));
//
//		Integer overallAppFrom = stateAppRange.getAppFrom();
//		Integer overallAppTo = stateAppRange.getAppTo();
//
//		// 2. Try to find MAX(appEndNo) + 1 from active Distribution records
//		// (issuedByType = 1)
//		Optional<Integer> nextNumberOptional = distributionRepository
//				.findNextStartNumberByStateAndIssuerType(academicYearId, stateId);
//
//		Integer nextAvailableNumber;
//
//		if (nextNumberOptional.isPresent()) {
//			// 3. If records exist, use the calculated MAX(appEndNo) + 1
//			nextAvailableNumber = nextNumberOptional.get();
//		} else {
//			// 4. If NO distribution records exist, the next available number is the overall
//			// starting number.
//			nextAvailableNumber = overallAppFrom;
//		}
//
//		// 5. Package and return the three pieces of data in the new DTO
//		return new ApplicationRangeInfoDTO(nextAvailableNumber, overallAppFrom, overallAppTo);
//	}
//	
//	 public ApplicationRangeInfoDTO getApplicationNumberInfo(
//		        int academicYearId, 
//		        Integer stateId, // Optional, passed as null if not provided
//		        Integer cityId   // Optional, passed as null if not provided
//		    ) {
//		        
//		        // 1. VALIDATION AND RESOLUTION: Determine the finalStateId
//		        if (stateId == null && cityId == null) {
//		            throw new IllegalArgumentException("Either stateId or cityId must be provided.");
//		        }
//
//		        int finalStateId;
//		        if (cityId != null) {
//		            // Traverse City -> District -> State
//		            City city = cityRepository.findById(cityId)
//		                    .orElseThrow(() -> new RuntimeException("City not found for ID: " + cityId));
//		            finalStateId = city.getDistrict().getState().getStateId();
//		        } else { 
//		            // cityId is null, use the provided stateId
//		            finalStateId = stateId;
//		        }
//		        
//		        // 2. Get the overall assigned range (AppFrom and AppTo) from StateApp
//		        ApplicationStartEndDto stateAppRange = stateAppRepository.findRangeByAcademicYearAndState(academicYearId, finalStateId)
//		                .orElseThrow(() -> new RuntimeException("Overall App ranges not found in StateApp table for State ID: " + finalStateId));
//
//		        Integer overallAppFrom = stateAppRange.getAppFrom();
//		        Integer overallAppTo = stateAppRange.getAppTo();
//
//		        // 3. Find MAX(appEndNo) + 1 from active Distribution records
//		        Optional<Integer> nextNumberOptional = distributionRepository
//		                .findMaxAppEndNoByAcademicYearAndState(academicYearId, finalStateId);
//
//		        Integer nextAvailableNumber;
//
//		        if (nextNumberOptional.isPresent()) {
//		            nextAvailableNumber = nextNumberOptional.get();
//		        } else {
//		            nextAvailableNumber = overallAppFrom;
//		        }
//
//		        // 4. Return the result
//		        return new ApplicationRangeInfoDTO(nextAvailableNumber, overallAppFrom, overallAppTo);
//		    }
//	// ----------------------------------------
//	// --- CORE METHODS ---
//	// ----------------------------------------
//
//	@Transactional
////    @CacheEvict(value = {"nextAppNumber", "appEndNo", "balanceTrack"}, allEntries = true)
//	public void saveDistribution(@NonNull DistributionRequestDTO request) {
//	    validateEmployeeExists(request.getCreatedBy(), "Issuer");
//	    validateEmployeeExists(request.getIssuedToEmpId(), "Receiver");
//
//	    List<Distribution> overlappingDists = distributionRepository.findOverlappingDistributions(
//	            request.getAcademicYearId(), request.getAppStartNo(), request.getAppEndNo());
//
//	    if (!overlappingDists.isEmpty()) {
//	        handleOverlappingDistributions(overlappingDists, request);
//	    }
//
//	    Distribution newDistribution = new Distribution();
//	    mapDtoToDistribution(newDistribution, request);
//
//	    Distribution savedDist = distributionRepository.save(newDistribution);
//	    if (savedDist == null || savedDist.getAppDistributionId() == null) {
//	        throw new RuntimeException("Failed to save distribution record — aborting balance recalculation.");
//	    }
//
//	    recalculateBalanceForEmployee(request.getCreatedBy(), request.getAcademicYearId(), request.getStateId(),
//	            request.getIssuedByTypeId(), request.getCreatedBy());
//	    recalculateBalanceForEmployee(request.getIssuedToEmpId(), request.getAcademicYearId(), request.getStateId(),
//	            request.getIssuedToTypeId(), request.getCreatedBy());
//	}
//
//	/**
//	 * Revised method to ensure NO UPDATE (no modification) of existing distribution
//	 * records. All changes result in inactivation of the old record and insertion
//	 * of new record(s).
//	 */
//	@Transactional
////    @CacheEvict(value = {"nextAppNumber", "appEndNo", "balanceTrack"}, allEntries = true)
//	public void updateDistribution(int distributionId, @NonNull DistributionRequestDTO request) {
//		validateEmployeeExists(request.getCreatedBy(), "Issuer");
//		validateEmployeeExists(request.getIssuedToEmpId(), "New Receiver");
//
//		Distribution existingDistribution = distributionRepository.findById(distributionId)
//				.orElseThrow(() -> new RuntimeException("Distribution record not found: " + distributionId));
//
//		int oldReceiverId = existingDistribution.getIssued_to_emp_id();
//		int oldStart = existingDistribution.getAppStartNo();
//		int oldEnd = existingDistribution.getAppEndNo();
//
//		// Determine if the *range* or *recipient* is changing
//		boolean isRecipientChanging = oldReceiverId != request.getIssuedToEmpId();
//		boolean isRangeChanging = request.getAppStartNo() != oldStart || request.getAppEndNo() != oldEnd;
//
//		// --- Core Action: Inactivation and Insert ---
//
//		// 1. Inactivate the original record (PRESERVING its range fields for history)
//		// This is done for ALL updates (range change, recipient change, or just
//		// metadata change)
//		existingDistribution.setIsActive(0);
//		distributionRepository.save(existingDistribution);
//
//		// 2. Insert new record for the requested range (the primary action of this
//		// update)
//		Distribution newRequestedDistribution = new Distribution();
//		mapDtoToDistribution(newRequestedDistribution, request);
//		distributionRepository.save(newRequestedDistribution);
//
//		// 3. Handle the remainder: if the *new requested range* is smaller than the
//		// *original range*
//		// This only applies if the range was reduced or shifted.
//		if (isRangeChanging) {
//
//			// Handle the part of the OLD range that is BEFORE the NEW range, if any
//			if (oldStart < request.getAppStartNo()) {
//				// The remainder starts at oldStart and ends at newStart - 1
//				Distribution beforeRemainder = createRemainderDistribution(existingDistribution, oldReceiverId);
//				beforeRemainder.setAppStartNo(oldStart);
//				beforeRemainder.setAppEndNo(request.getAppStartNo() - 1);
//				beforeRemainder.setTotalAppCount(beforeRemainder.getAppEndNo() - beforeRemainder.getAppStartNo() + 1);
//				distributionRepository.save(beforeRemainder);
//			}
//
//			// Handle the part of the OLD range that is AFTER the NEW range, if any
//			if (oldEnd > request.getAppEndNo()) {
//				// The remainder starts immediately after the new requested range ends.
//				Distribution afterRemainder = createRemainderDistribution(existingDistribution, oldReceiverId);
//				afterRemainder.setAppStartNo(request.getAppEndNo() + 1);
//				afterRemainder.setAppEndNo(oldEnd);
//				afterRemainder.setTotalAppCount(afterRemainder.getAppEndNo() - afterRemainder.getAppStartNo() + 1);
//				distributionRepository.save(afterRemainder);
//			}
//		}
//
//		// --- Balance Recalculation ---
//
//		int academicYearId = existingDistribution.getAcademicYear().getAcdcYearId();
//		int stateId = existingDistribution.getState().getStateId();
//
//		// Recalculate balance for the issuer (CreatedBy)
//		recalculateBalanceForEmployee(request.getCreatedBy(), academicYearId, stateId, request.getIssuedByTypeId(),
//				request.getCreatedBy());
//
//		// Recalculate balance for the new receiver
//		recalculateBalanceForEmployee(request.getIssuedToEmpId(), academicYearId, stateId, request.getIssuedToTypeId(),
//				request.getCreatedBy());
//
//		// Recalculate balance for the old receiver, if the recipient changed OR if the
//		// range was adjusted
//		if (isRecipientChanging || isRangeChanging) {
//			balanceTrackRepository.findBalanceTrack(academicYearId, oldReceiverId).ifPresent(oldBalance -> {
//				int oldReceiverTypeId = oldBalance.getIssuedByType().getAppIssuedId();
//				recalculateBalanceForEmployee(oldReceiverId, academicYearId, stateId, oldReceiverTypeId,
//						request.getCreatedBy());
//			});
//		}
//	}
//
//	// ----------------------------------------
//	// --- PRIVATE HELPER METHODS ---
//	// ----------------------------------------
//
//	private void handleOverlappingDistributions(List<Distribution> overlappingDists, DistributionRequestDTO request) {
//		for (Distribution oldDist : overlappingDists) {
//			int oldReceiverId = oldDist.getIssued_to_emp_id();
//
//			// Do nothing if the overlap is with the same person we are issuing to.
//			if (oldReceiverId == request.getIssuedToEmpId())
//				continue;
//
//			// **Step 1: Get the boundaries of the non-overlapping segments**
//			int oldStart = oldDist.getAppStartNo();
//			int oldEnd = oldDist.getAppEndNo();
//			int newStart = request.getAppStartNo();
//			int newEnd = request.getAppEndNo();
//
//			// **Step 2: Deactivate the entire original record**
//			oldDist.setIsActive(0);
//			distributionRepository.save(oldDist);
//
//			// **Step 3: Create a new record for the non-overlapping part BEFORE the new
//			// range (if any)**
//			if (oldStart < newStart) {
//				Distribution beforeSplit = new Distribution();
//				mapDtoToDistribution(beforeSplit, createDtoFromDistribution(oldDist)); // Copy historical data
//
//				beforeSplit.setAppStartNo(oldStart);
//				beforeSplit.setAppEndNo(newStart - 1);
//				beforeSplit.setTotalAppCount(beforeSplit.getAppEndNo() - beforeSplit.getAppStartNo() + 1);
//				beforeSplit.setIsActive(1); // New segment is active
//				beforeSplit.setIssued_to_emp_id(oldReceiverId); // Stays with old receiver
//				distributionRepository.save(beforeSplit);
//			}
//
//			// **Step 4: Create a new record for the non-overlapping part AFTER the new
//			// range (if any)**
//			if (oldEnd > newEnd) {
//				Distribution afterSplit = new Distribution();
//				mapDtoToDistribution(afterSplit, createDtoFromDistribution(oldDist)); // Copy historical data
//
//				afterSplit.setAppStartNo(newEnd + 1);
//				afterSplit.setAppEndNo(oldEnd);
//				afterSplit.setTotalAppCount(afterSplit.getAppEndNo() - afterSplit.getAppStartNo() + 1);
//				afterSplit.setIsActive(1); // New segment is active
//				afterSplit.setIssued_to_emp_id(oldReceiverId); // Stays with old receiver
//				distributionRepository.save(afterSplit);
//			}
//
//			// Recalculate balance for the employee who lost applications
//			recalculateBalanceForEmployee(oldReceiverId, request.getAcademicYearId(), oldDist.getState().getStateId(),
//					oldDist.getIssuedToType().getAppIssuedId(), request.getCreatedBy());
//		}
//	}
//
//	private void recalculateBalanceForEmployee(int employeeId, int academicYearId, int stateId, int typeId,
//			int createdBy) {
//		
//		if (typeId == 1 || employeeId == createdBy) {
//            // You can log it if you want:
//            System.out.println("Skipping balance recalculation for Admin/self-issued record. Employee ID: " + employeeId);
//            return;
//        }
//		BalanceTrack balance = balanceTrackRepository.findBalanceTrack(academicYearId, employeeId)
//				.orElseGet(() -> createNewBalanceTrack(employeeId, academicYearId, typeId, createdBy));
//
//		Optional<StateApp> initialState = stateAppRepository.findStartNumber(stateId, employeeId, academicYearId);
//
//		if (initialState.isPresent()) {
//			StateApp stateApp = initialState.get();
//			// Sum of applications issued *by* this employee
//			int totalDistributed = distributionRepository.sumTotalAppCountByCreatedBy(employeeId, academicYearId)
//					.orElse(0);
//			balance.setAppFrom(stateApp.getApp_start_no());
//			balance.setAppTo(stateApp.getApp_end_no());
//			// Available = Total Assigned to this user - Total Issued by this user
//			balance.setAppAvblCnt(stateApp.getTotal_no_of_app() - totalDistributed);
//		} else {
//			// Sum of applications received *by* this employee
//			Integer totalReceived = distributionRepository.sumTotalAppCountByIssuedToEmpId(employeeId, academicYearId)
//					.orElse(0);
//			if (totalReceived > 0) {
//				Integer minAppNo = distributionRepository
//						.findMinAppStartNoByIssuedToEmpIdAndAcademicYearId(employeeId, academicYearId).orElse(0);
//				Integer maxAppNo = distributionRepository
//						.findMaxAppEndNoByIssuedToEmpIdAndAcademicYearId(employeeId, academicYearId).orElse(0);
//				balance.setAppFrom(minAppNo);
//				balance.setAppTo(maxAppNo);
//			} else {
//				balance.setAppFrom(0);
//				balance.setAppTo(0);
//			}
//			// Available count = Total received by this user (since they are not a top-level
//			// issuer)
//			balance.setAppAvblCnt(totalReceived);
//		}
//		balanceTrackRepository.save(balance);
//	}
//
//	/**
//	 * Helper to create a new active Distribution record based on an existing one
//	 * but setting the new IssuedToEmpId (the old receiver) and setting IsActive=1.
//	 */
//	private Distribution createRemainderDistribution(Distribution originalDist, int receiverId) {
//		Distribution remainderDistribution = new Distribution();
//		// Copy most fields from the original distribution
//		mapDtoToDistribution(remainderDistribution, createDtoFromDistribution(originalDist));
//
//		// Set specific fields for the remainder
//		remainderDistribution.setIssued_to_emp_id(receiverId); // Stays with the OLD receiver
//		remainderDistribution.setIsActive(1);
//
//		// Note: The range and count will be set by the caller (updateDistribution)
//		return remainderDistribution;
//	}
//
//	private void mapDtoToDistribution(Distribution d, DistributionRequestDTO req) {
//		d.setAcademicYear(academicYearRepository.findById(req.getAcademicYearId()).orElseThrow());
//		d.setState(stateRepository.findById(req.getStateId()).orElseThrow());
//		d.setZone(zoneRepository.findById(req.getZoneId()).orElseThrow());
//		d.setIssuedByType(appIssuedTypeRepository.findById(req.getIssuedByTypeId()).orElseThrow());
//		d.setIssuedToType(appIssuedTypeRepository.findById(req.getIssuedToTypeId()).orElseThrow());
//		City city = cityRepository.findById(req.getCityId()).orElseThrow();
//		d.setCity(city);
//		d.setDistrict(city.getDistrict());
//		d.setIssued_to_emp_id(req.getIssuedToEmpId());
//		d.setCreated_by(req.getCreatedBy());
//		d.setAppStartNo(req.getAppStartNo());
//		d.setAppEndNo(req.getAppEndNo());
//		d.setTotalAppCount(req.getRange());
//		d.setIsActive(1);
//		d.setIssueDate(req.getIssueDate() != null ? req.getIssueDate() : LocalDate.now());
//	}
//
//	private DistributionRequestDTO createDtoFromDistribution(Distribution dist) {
//		DistributionRequestDTO dto = new DistributionRequestDTO();
//		dto.setAcademicYearId(dist.getAcademicYear().getAcdcYearId());
//		dto.setStateId(dist.getState().getStateId());
//		dto.setCityId(dist.getCity().getCityId());
//		dto.setZoneId(dist.getZone().getZoneId());
//		dto.setIssuedByTypeId(dist.getIssuedByType().getAppIssuedId());
//		dto.setIssuedToTypeId(dist.getIssuedToType().getAppIssuedId());
//		dto.setIssuedToEmpId(dist.getIssued_to_emp_id());
//		dto.setAppStartNo(dist.getAppStartNo());
//		dto.setAppEndNo(dist.getAppEndNo());
//		dto.setRange(dist.getTotalAppCount());
//		dto.setIssueDate(dist.getIssueDate());
//		dto.setCreatedBy(dist.getCreated_by());
//		return dto;
//	}
//
//	private BalanceTrack createNewBalanceTrack(int employeeId, int academicYearId, int typeId, int createdBy) {
//		BalanceTrack nb = new BalanceTrack();
//		nb.setEmployee(employeeRepository.findById(employeeId).orElseThrow());
//		nb.setAcademicYear(academicYearRepository.findById(academicYearId).orElseThrow());
//		nb.setIssuedByType(appIssuedTypeRepository.findById(typeId).orElseThrow());
//		nb.setAppAvblCnt(0);
//		nb.setAppFrom(0);
//		nb.setAppTo(0);
//		nb.setIsActive(1);
//		nb.setCreatedBy(createdBy);
//		return nb;
//	}
//
//	private void validateEmployeeExists(int employeeId, String role) {
//		if (employeeId <= 0 || !employeeRepository.existsById(employeeId)) {
//			throw new IllegalArgumentException(role + " employee not found or invalid ID: " + employeeId);
//		}
//	}
//}


package com.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.dto.AppSeriesDTO;
import com.application.dto.ApplicationRangeInfoDTO;
import com.application.dto.ApplicationStartEndDto;
import com.application.dto.DistributionRequestDTO;
import com.application.dto.EmployeesDto;
import com.application.dto.NextAppNumberDTO;
import com.application.entity.AcademicYear;
import com.application.entity.AdminApp;
import com.application.entity.BalanceTrack;
import com.application.entity.City;
import com.application.entity.Distribution;
import com.application.entity.Employee;
import com.application.entity.State;
import com.application.entity.ZonalAccountant;
import com.application.entity.Zone;
import com.application.repository.AcademicYearRepository;
import com.application.repository.AdminAppRepository;
import com.application.repository.AppIssuedTypeRepository;
import com.application.repository.BalanceTrackRepository;
import com.application.repository.CampusProViewRepository;
import com.application.repository.CityRepository;
import com.application.repository.DistributionRepository;
import com.application.repository.EmployeeRepository;
import com.application.repository.StateAppRepository;
import com.application.repository.StateRepository;
import com.application.repository.ZonalAccountantRepository;
import com.application.repository.ZoneRepository;

import lombok.NonNull;

@Service
public class ZoneService {

	private final AcademicYearRepository academicYearRepository;
	private final StateRepository stateRepository;
	private final CityRepository cityRepository;
	private final ZoneRepository zoneRepository;
	private final AppIssuedTypeRepository appIssuedTypeRepository;
	private final EmployeeRepository employeeRepository;
	private final StateAppRepository stateAppRepository;
	private final BalanceTrackRepository balanceTrackRepository;
	private final DistributionRepository distributionRepository;
	private final ZonalAccountantRepository zonalAccountantRepository;
//	@Autowired
//	private CampusProViewRepository campusProViewRepository;
	@Autowired
    private AdminAppRepository adminAppRepository;
	@Autowired
	private CampusProViewRepository campusProViewRepository;
	

	public ZoneService(AcademicYearRepository academicYearRepository, StateRepository stateRepository,
			CityRepository cityRepository, ZoneRepository zoneRepository,
			AppIssuedTypeRepository appIssuedTypeRepository, EmployeeRepository employeeRepository,
			StateAppRepository stateAppRepository, BalanceTrackRepository balanceTrackRepository,
			DistributionRepository distributionRepository, ZonalAccountantRepository zonalAccountantRepository) {
		this.academicYearRepository = academicYearRepository;
		this.stateRepository = stateRepository;
		this.cityRepository = cityRepository;
		this.zoneRepository = zoneRepository;
		this.appIssuedTypeRepository = appIssuedTypeRepository;
		this.employeeRepository = employeeRepository;
		this.stateAppRepository = stateAppRepository;
		this.balanceTrackRepository = balanceTrackRepository;
		this.distributionRepository = distributionRepository;
		this.zonalAccountantRepository = zonalAccountantRepository;
	}

	// --- Dropdown/Helper Methods with Caching ---
	@Cacheable("academicYears")
	public List<AcademicYear> getAllAcademicYears() {
		return academicYearRepository.findAll();
	}

	@Cacheable("states")
	public List<State> getAllStates() {
		// Assumption: State entity has a field named 'is_active' or similar.
		return stateRepository.findByStatus(1);
	}

	@Cacheable(cacheNames = "citiesByState", key = "#stateId")
	public List<City> getCitiesByState(int stateId) {
		final int ACTIVE_STATUS = 1;
		return cityRepository.findByDistrictStateStateIdAndStatus(stateId, ACTIVE_STATUS);
	}

	@Cacheable(cacheNames = "zonesByCity", key = "#cityId")
	public List<Zone> getZonesByCity(int cityId) {
		return zoneRepository.findByCityCityId(cityId);
	}

//    @Cacheable("issuableEmployees")
	public List<Employee> getIssuableToEmployees() {
		return employeeRepository.findAll();
	}

//    @Cacheable(cacheNames = "appEndNo", key = "{#stateId, #userId}")
	public Integer getAppEndNoForUser(int stateId, int userId) {
		return stateAppRepository.findAppEndNoByStateAndUser(stateId, userId);
	}

//    @Cacheable(cacheNames = "employeesByZone", key = "#zoneId")
//	public List<Employee> getEmployeesByZone(int zoneId) {
//		return zonalAccountantRepository.findByZoneZoneId(zoneId).stream().map(ZonalAccountant::getEmployee)
//				.collect(Collectors.toList());
//	}

	@Cacheable(cacheNames = "employeesByZone", key = "#zoneId")
	@Transactional(readOnly = true) // Recommended for lazy loading
	public List<EmployeesDto> getEmployeesByZone(int zoneId) {
		// Fetch only active zonal accountants (ZonalAccountant.isActive == 1)
		List<ZonalAccountant> activeAccountants = zonalAccountantRepository.findByZoneZoneIdAndIsActive(zoneId, 1);

		// Map and filter: Only include if Employee.isActive == 1
		return activeAccountants.stream().map(this::mapToEmployeeDto).filter(Objects::nonNull) // Skip null/inactive
																								// employees
				.collect(Collectors.toList());
	}

	// Helper: Maps only if employee is active (filters but doesn't include in DTO)
	private EmployeesDto mapToEmployeeDto(ZonalAccountant accountant) {
		var employee = accountant.getEmployee();
		// Filter: Skip if employee null or inactive
		if (employee == null || employee.getIsActive() == null || employee.getIsActive() != 1) {
			return null;
		}
		// Map without isActive
		return new EmployeesDto(employee.getEmp_id(), employee.getFirst_name(), employee.getLast_name(),
				employee.getPrimary_mobile_no());
	}

//    @Cacheable(cacheNames = "nextAppNumber", key = "{#academicYearId, #stateId, #userId}")
	public String getNextApplicationNumber(int academicYearId, int stateId, int userId) {
		Integer lastAppNumber = distributionRepository.findMaxAppEndNo(stateId, userId, academicYearId);
		if (lastAppNumber != null)
			return String.valueOf(lastAppNumber + 1);
		return stateAppRepository.findStartNumber(stateId, userId, academicYearId)
				.map(sa -> String.valueOf(sa.getApp_start_no())).orElse("ERROR_NO_RANGE_ASSIGNED");
	}

	// @Cacheable(cacheNames = "appNumberRanges", key = "{#academicYearId, #stateId,
	// #createdBy}")
	public ApplicationStartEndDto getAppNumberRanges(int academicYearId, int stateId) {
		return stateAppRepository.findAppNumberRanges(academicYearId, stateId)
				.orElseThrow(() -> new RuntimeException("App ranges not found."));
	}

	
	public NextAppNumberDTO getNextApplicationNumberForTopIssuer(int academicYearId, int stateId) {

		// 1. Try to find MAX(appEndNo) + 1 from active Distribution records
		// (issuedByType = 1)
		Optional<Integer> nextNumberOptional = distributionRepository
				.findNextStartNumberByStateAndIssuerType(academicYearId, stateId);

		Integer nextNumber;

		if (nextNumberOptional.isPresent()) {
			// 2. If records exist, use the calculated MAX(appEndNo) + 1
			nextNumber = nextNumberOptional.get();
		} else {
			// 3. If NO records exist, fall back to the starting number from the StateApp
			// table
			// We use createdBy/stateId to find the assigned range for the top-level user.
			ApplicationStartEndDto stateAppRange = getAppNumberRanges(academicYearId, stateId);

			// Assuming ApplicationStartEndDto has a method/field named getAppFrom()
			nextNumber = stateAppRange.getAppFrom();
		}

		return new NextAppNumberDTO(nextNumber);
	}

	public ApplicationRangeInfoDTO getApplicationNumberInfo(int academicYearId, int stateId) {

		// 1. Get the overall assigned range (AppFrom and AppTo) from StateApp (required
		// fallback data)
		// This uses your original getAppNumberRanges logic.
		ApplicationStartEndDto stateAppRange = stateAppRepository.findAppNumberRanges(academicYearId, stateId)
				.orElseThrow(() -> new RuntimeException("Overall App ranges not found in StateApp table."));

		Integer overallAppFrom = stateAppRange.getAppFrom();
		Integer overallAppTo = stateAppRange.getAppTo();

		// 2. Try to find MAX(appEndNo) + 1 from active Distribution records
		// (issuedByType = 1)
		Optional<Integer> nextNumberOptional = distributionRepository
				.findNextStartNumberByStateAndIssuerType(academicYearId, stateId);

		Integer nextAvailableNumber;

		if (nextNumberOptional.isPresent()) {
			// 3. If records exist, use the calculated MAX(appEndNo) + 1
			nextAvailableNumber = nextNumberOptional.get();
		} else {
			// 4. If NO distribution records exist, the next available number is the overall
			// starting number.
			nextAvailableNumber = overallAppFrom;
		}

		// 5. Package and return the three pieces of data in the new DTO
		return new ApplicationRangeInfoDTO(nextAvailableNumber, overallAppFrom, overallAppTo);
	}

	public ApplicationRangeInfoDTO getApplicationNumberInfo(int academicYearId, Integer stateId, // Optional, passed as
																									// null if not
																									// provided
			Integer cityId // Optional, passed as null if not provided
	) {

		// 1. VALIDATION AND RESOLUTION: Determine the finalStateId
		if (stateId == null && cityId == null) {
			throw new IllegalArgumentException("Either stateId or cityId must be provided.");
		}

		int finalStateId;
		if (cityId != null) {
			// Traverse City -> District -> State
			City city = cityRepository.findById(cityId)
					.orElseThrow(() -> new RuntimeException("City not found for ID: " + cityId));
			finalStateId = city.getDistrict().getState().getStateId();
		} else {
			// cityId is null, use the provided stateId
			finalStateId = stateId;
		}

		// 2. Get the overall assigned range (AppFrom and AppTo) from StateApp
		ApplicationStartEndDto stateAppRange = stateAppRepository
				.findRangeByAcademicYearAndState(academicYearId, finalStateId).orElseThrow(() -> new RuntimeException(
						"Overall App ranges not found in StateApp table for State ID: " + finalStateId));

		Integer overallAppFrom = stateAppRange.getAppFrom();
		Integer overallAppTo = stateAppRange.getAppTo();

		// 3. Find MAX(appEndNo) + 1 from active Distribution records
		Optional<Integer> nextNumberOptional = distributionRepository
				.findMaxAppEndNoByAcademicYearAndState(academicYearId, finalStateId);

		Integer nextAvailableNumber;

		if (nextNumberOptional.isPresent()) {
			nextAvailableNumber = nextNumberOptional.get();
		} else {
			nextAvailableNumber = overallAppFrom;
		}

		// 4. Return the result
		return new ApplicationRangeInfoDTO(nextAvailableNumber, overallAppFrom, overallAppTo);
	}
	// ----------------------------------------
	// --- CORE METHODS ---
	// ----------------------------------------

	@Transactional
	public void saveDistribution(@NonNull DistributionRequestDTO request) {

		// -------------------------------------------------------
		// STEP 0: Basic Validation (Issuer) & Overlap Check
		// -------------------------------------------------------
		validateEmployeeExists(request.getCreatedBy(), "Issuer");

		List<Distribution> overlappingDists = distributionRepository.findOverlappingDistributions(
				request.getAcademicYearId(), request.getAppStartNo(), request.getAppEndNo());

		if (!overlappingDists.isEmpty()) {
			handleOverlappingDistributions(overlappingDists, request);
		}

		// -------------------------------------------------------
		// STEP 1: CHECK RECEIVER (Lookup ZonalAccountant & Check Active)
		// -------------------------------------------------------
		// The frontend sends the 'zone_acct_id' in the issuedToId field
		ZonalAccountant receiver = zonalAccountantRepository.findByEmployeeEmpId(request.getIssuedToEmpId())
		        .orElseThrow(() -> new RuntimeException("Receiver not found for Employee ID: " + request.getIssuedToEmpId()));

		// Optional: You can log a warning if the Zone doesn't match what the UI sent
		if (receiver.getZone().getZoneId() != request.getZoneId()) {
		    System.out.println("WARNING: UI sent Zone " + request.getZoneId() + " but User " + request.getIssuedToEmpId() + " is actually in Zone " + receiver.getZone().getZoneId());
		}

		if (receiver.getIsActive() != 1) {
			throw new RuntimeException("Transaction Failed: The selected Receiver is Inactive.");
		}

		// -------------------------------------------------------
		// STEP 2: SAVE TRANSACTION (Create Distribution Record)
		// -------------------------------------------------------
		Distribution newDistribution = new Distribution();
		mapDtoToDistribution(newDistribution, request); // Helper to map basic fields (State, Zone, Dates, etc.)

		// LOGIC: Determine where to save the ID (Employee Column vs PRO Column)
		if (receiver.getEmployee() != null) {
			// It's an Employee (e.g., DGM)
			newDistribution.setIssued_to_emp_id(receiver.getEmployee().getEmp_id());
			newDistribution.setIssued_to_pro_id(null);
		} else if (receiver.getCampus() != null) {
			// It's a PRO (Branch)
			newDistribution.setIssued_to_pro_id(receiver.getCampus().getCampusId());
			newDistribution.setIssued_to_emp_id(null);
		} else {
			throw new RuntimeException("Invalid Receiver: No Employee or Campus linked to this Zonal Accountant.");
		}

		// Set Fee/Amount
		newDistribution.setAmount(request.getApplication_Amount());

		// Save to DB
		Distribution savedDist = distributionRepository.saveAndFlush(newDistribution);
		// -------------------------------------------------------
		// STEP 3: UPDATE INVENTORY (BalanceTrack)
		// -------------------------------------------------------

		// A. Update Issuer's Balance (The Admin/User sending the apps)
//		recalculateBalanceForEmployee(request.getCreatedBy(), request.getAcademicYearId(), request.getStateId(),
//	            request.getIssuedByTypeId(), request.getCreatedBy(), request.getApplication_Amount());
		
		// Correct
		recalculateBalanceForEmployee(
		    savedDist.getIssued_to_emp_id(),
		    request.getAcademicYearId(), 
		    request.getStateId(), 
		    request.getIssuedToTypeId(), 
		    request.getCreatedBy(),          
		    request.getApplication_Amount()
		);

	    // B. Update Receiver's Balance
		if (savedDist.getIssued_to_emp_id() != null) {
            addStockToReceiver(
                savedDist.getIssued_to_emp_id(), 
                request.getAcademicYearId(), 
                request.getIssuedToTypeId(), 
                request.getCreatedBy(), 
                request.getApplication_Amount(),
                request.getAppStartNo(),  // Pass Start
                request.getAppEndNo(),    // Pass End
                request.getRange()        // Pass Count
            );
       }
	}

	// ... (Your existing helpers: mapDtoToDistribution,
	// recalculateBalanceForEmployee, etc.) ...

	/**
	 * Revised method to ensure NO UPDATE (no modification) of existing distribution
	 * records. All changes result in inactivation of the old record and insertion
	 * of new record(s).
	 */

	@Transactional
    public void updateDistribution(int distributionId, @NonNull DistributionRequestDTO request) {
        
        // 1. Fetch Existing
        validateEmployeeExists(request.getCreatedBy(), "Issuer");
        Distribution existingDist = distributionRepository.findById(distributionId)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        Float originalAmount = existingDist.getAmount(); // Keep amount!
        
        // 2. Resolve New Receiver (Zone Service always targets Employee column)
        // (Assuming you updated your Repo to use findByEmployeeEmpId)
        ZonalAccountant newReceiver = zonalAccountantRepository.findByEmployeeEmpId(request.getIssuedToEmpId())
                .orElseThrow(() -> new RuntimeException("New Receiver not found"));

        // Determine Target ID (Logic: always grab the emp_id, even for campuses)
        Integer newTargetId;
        if (newReceiver.getEmployee() != null) {
            newTargetId = newReceiver.getEmployee().getEmp_id();
        } else {
             newTargetId = campusProViewRepository.findEmployeeIdsByCampusId(newReceiver.getCampus().getCampusId())
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("No valid ID found for Campus"));
        }

        // 3. Inactivate Old
        existingDist.setIsActive(0);
        distributionRepository.saveAndFlush(existingDist);

        // 4. Create New
        Distribution newDist = new Distribution();
        mapDtoToDistribution(newDist, request);
        
        newDist.setIssued_to_emp_id(newTargetId);
        newDist.setIssued_to_pro_id(null); // Zone service keeps this null
        newDist.setAmount(originalAmount); // Preserve Amount

        distributionRepository.saveAndFlush(newDist);

        // 5. Handle Remainders
        int oldStart = (int) existingDist.getAppStartNo();
        int oldEnd = (int) existingDist.getAppEndNo();
        
        if (oldStart != request.getAppStartNo() || oldEnd != request.getAppEndNo()) {
            if (oldStart < request.getAppStartNo()) {
                createAndSaveRemainder(existingDist, oldStart, request.getAppStartNo() - 1);
            }
            if (oldEnd > request.getAppEndNo()) {
                createAndSaveRemainder(existingDist, request.getAppEndNo() + 1, oldEnd);
            }
        }

        // 6. Recalculate Balances
        int acYear = existingDist.getAcademicYear().getAcdcYearId();
        int stateId = existingDist.getState().getStateId();

        // A. Issuer
        recalculateBalanceForEmployee(request.getCreatedBy(), acYear, stateId, 
                request.getIssuedByTypeId(), request.getCreatedBy(), originalAmount);

        // B. New Receiver
        recalculateBalanceForEmployee(newTargetId, acYear, stateId, 
                request.getIssuedToTypeId(), request.getCreatedBy(), originalAmount);

        // C. Old Receiver (If changed)
        Integer oldId = existingDist.getIssued_to_emp_id();
        if (oldId != null && (!Objects.equals(oldId, newTargetId) || (oldStart != request.getAppStartNo() || oldEnd != request.getAppEndNo()))) {
             recalculateBalanceForEmployee(oldId, acYear, stateId, 
                     existingDist.getIssuedToType().getAppIssuedId(), request.getCreatedBy(), originalAmount);
        }
    }

	private void createAndSaveRemainder(Distribution originalDist, int start, int end) {
		Distribution remainder = new Distribution();

		// Copy standard fields
		remainder.setAcademicYear(originalDist.getAcademicYear());
		remainder.setState(originalDist.getState());
		remainder.setCity(originalDist.getCity());
		remainder.setZone(originalDist.getZone());
		remainder.setDistrict(originalDist.getDistrict());
		remainder.setIssuedByType(originalDist.getIssuedByType());
		remainder.setIssuedToType(originalDist.getIssuedToType());
		remainder.setCreated_by(originalDist.getCreated_by());
		remainder.setIssueDate(originalDist.getIssueDate() != null ? originalDist.getIssueDate() : LocalDateTime.now());	
		remainder.setAmount(originalDist.getAmount());
		remainder.setIssued_to_emp_id(originalDist.getIssued_to_emp_id());
		remainder.setIssued_to_pro_id(originalDist.getIssued_to_pro_id());

		// Set New Range
		remainder.setAppStartNo(start);
		remainder.setAppEndNo(end);
		remainder.setTotalAppCount((end - start) + 1);
		remainder.setIsActive(1); // Active

		distributionRepository.saveAndFlush(remainder);
	}

	// ----------------------------------------
	// --- PRIVATE HELPER METHODS ---
	// ----------------------------------------

	private void handleOverlappingDistributions(List<Distribution> overlappingDists, DistributionRequestDTO request) {
        int reqStart = request.getAppStartNo();
        int reqEnd = request.getAppEndNo();

        for (Distribution oldDist : overlappingDists) {
            
            // 1. Identify the "Old Holder" (The Victim)
            Integer oldHolderId;
            boolean isPro = false;

            if (oldDist.getIssued_to_pro_id() != null) {
                oldHolderId = oldDist.getIssued_to_pro_id();
                isPro = true;
            } 
            else if (oldDist.getIssued_to_emp_id() != null) {
                oldHolderId = oldDist.getIssued_to_emp_id();
                isPro = false;
            } 
            else {
                continue; 
            }

            // [DELETED] The check that skipped self-updates is gone.
            // We process EVERY overlap to ensure the old record is deactivated.

            // 2. Inactivate OLD (Soft Delete)
            oldDist.setIsActive(0);
            
            // CRITICAL: Flush so the database knows this is now 0
            distributionRepository.saveAndFlush(oldDist); 

            int oldStart = oldDist.getAppStartNo();
            int oldEnd = oldDist.getAppEndNo();

            // 3. Create "Before" Split (Remainder)
            if (oldStart < reqStart) {
                createAndSaveRemainder(oldDist, oldStart, reqStart - 1);
            }

            // 4. Create "After" Split (Remainder)
            if (oldEnd > reqEnd) {
                createAndSaveRemainder(oldDist, reqEnd + 1, oldEnd);
            }

            // 5. Recalculate Balance for the OLD HOLDER (The Victim)
            // We use the Old Distribution's metadata (State, Type, Amount)
            int acYear = request.getAcademicYearId();
            int stateId = oldDist.getState().getStateId();
            int typeId = oldDist.getIssuedToType().getAppIssuedId();
            int modifierId = request.getCreatedBy(); 
            Float amount = oldDist.getAmount(); // Keep Original Amount

                recalculateBalanceForEmployee(oldHolderId, acYear, stateId, typeId, modifierId, amount);
        }
    }

private void recalculateBalanceForEmployee(int employeeId, int academicYearId, int stateId, int typeId, int createdBy, Float amount) {
        
        // 1. CHECK: Is this a CO/Admin? (Check Master Table)
        // We convert Float to Float for the repo call
        Optional<AdminApp> adminApp = adminAppRepository.findByEmpAndYearAndAmount(
                employeeId, academicYearId, amount);

        if (adminApp.isPresent()) {
            // --- CASE A: CO / ADMIN (The Source) ---
            // Logic: Master Allocation - Total Distributed
            
            AdminApp master = adminApp.get();
            
            // Admins usually have 1 giant balance row, so we fetch/create just one.
            List<BalanceTrack> balances = balanceTrackRepository.findActiveBalancesByEmpAndAmount(
                    academicYearId, employeeId, amount);
            
            BalanceTrack balance;
            if (balances.isEmpty()) {
                balance = createNewBalanceTrack(employeeId, academicYearId, typeId, createdBy);
                balance.setAmount(amount);
            } else {
                balance = balances.get(0); // Use the existing one
            }

            // Calculate Total Distributed by Admin
            int totalDistributed = distributionRepository.sumTotalAppCountByCreatedByAndAmount(
                    employeeId, academicYearId, amount).orElse(0);
            
            // Update Logic
            balance.setAppFrom(master.getAppFromNo()); // Start at Master Start
            balance.setAppTo(master.getAppToNo());
            balance.setAppAvblCnt(master.getTotalApp() - totalDistributed);
            
            balanceTrackRepository.save(balance);
        
        } else {
            // --- CASE B: ZONE & DGM (The Intermediaries) ---
            // They do NOT have a master table. They rely purely on what they HOLD.
            // We call the helper to rebuild their balance rows to match their holdings.
            
            rebuildBalancesFromDistributions(employeeId, academicYearId, typeId, createdBy, amount);
        }
    }



private void rebuildBalancesFromDistributions(int empId, int acYearId, int typeId, int createdBy, Float amount) {
    
    // 1. Get ALL Active Distributions currently HELD by this user
    // (Ordered by Start Number so it looks nice)
    List<Distribution> holdings = distributionRepository.findActiveByIssuedToEmpIdAndAmountOrderByStart(
            empId, acYearId, amount);

    // 2. Get CURRENT Active Balance Rows for this amount
    List<BalanceTrack> currentBalances = balanceTrackRepository.findActiveBalancesByEmpAndAmount(
            acYearId, empId, amount);

    // 3. STRATEGY: Soft Delete OLD rows, Insert NEW rows
    // This effectively "Refreshes" the balance to match reality.
    
    // A. Mark old rows as inactive
    for (BalanceTrack b : currentBalances) {
        b.setIsActive(0);
        balanceTrackRepository.save(b);
    }
    
    // B. Create new rows for every active distribution held
    for (Distribution dist : holdings) {
        BalanceTrack nb = createNewBalanceTrack(empId, acYearId, typeId, createdBy);
        
        nb.setAmount(amount);
        nb.setAppFrom((int) dist.getAppStartNo());
        nb.setAppTo((int) dist.getAppEndNo());
        nb.setAppAvblCnt(dist.getTotalAppCount());
        
        balanceTrackRepository.save(nb);
    }
}
	
	private Distribution createRemainderDistribution(Distribution originalDist, int receiverId) {
		Distribution remainderDistribution = new Distribution();
		// Copy most fields from the original distribution
		mapDtoToDistribution(remainderDistribution, createDtoFromDistribution(originalDist));

		// Set specific fields for the remainder
		remainderDistribution.setIssued_to_emp_id(receiverId); // Stays with the OLD receiver
		remainderDistribution.setIsActive(1);

		// Note: The range and count will be set by the caller (updateDistribution)
		return remainderDistribution;
	}

	private void mapDtoToDistribution(Distribution d, DistributionRequestDTO req) {
		d.setAcademicYear(academicYearRepository.findById(req.getAcademicYearId()).orElseThrow());
		d.setState(stateRepository.findById(req.getStateId()).orElseThrow());
		d.setZone(zoneRepository.findById(req.getZoneId()).orElseThrow());
		d.setIssuedByType(appIssuedTypeRepository.findById(req.getIssuedByTypeId()).orElseThrow());
		d.setIssuedToType(appIssuedTypeRepository.findById(req.getIssuedToTypeId()).orElseThrow());
		City city = cityRepository.findById(req.getCityId()).orElseThrow();
		d.setCity(city);
		d.setDistrict(city.getDistrict());
		d.setAmount(req.getApplication_Amount());
		d.setIssueDate(LocalDateTime.now());
		d.setIssued_to_emp_id(req.getIssuedToEmpId());
		d.setCreated_by(req.getCreatedBy());
		d.setAppStartNo(req.getAppStartNo());
		d.setAppEndNo(req.getAppEndNo());
		d.setTotalAppCount(req.getRange());
		d.setIsActive(1);
	}

	private DistributionRequestDTO createDtoFromDistribution(Distribution dist) {
		DistributionRequestDTO dto = new DistributionRequestDTO();
		dto.setAcademicYearId(dist.getAcademicYear().getAcdcYearId());
		dto.setStateId(dist.getState().getStateId());
		dto.setCityId(dist.getCity().getCityId());
		dto.setZoneId(dist.getZone().getZoneId());
		dto.setIssuedByTypeId(dist.getIssuedByType().getAppIssuedId());
		dto.setIssuedToTypeId(dist.getIssuedToType().getAppIssuedId());
		dto.setIssuedToEmpId(dist.getIssued_to_emp_id());
		dto.setApplication_Amount(dist.getAmount());
		dto.setAppStartNo(dist.getAppStartNo());
		dto.setAppEndNo(dist.getAppEndNo());
		dto.setRange(dist.getTotalAppCount());
//		dto.setIssueDate(dist.getIssueDate());
		dto.setCreatedBy(dist.getCreated_by());
		return dto;
	}

	private BalanceTrack createNewBalanceTrack(int employeeId, int academicYearId, int typeId, int createdBy) {
        BalanceTrack nb = new BalanceTrack();
        nb.setEmployee(employeeRepository.findById(employeeId).orElseThrow());
        nb.setAcademicYear(academicYearRepository.findById(academicYearId).orElseThrow());
        nb.setIssuedByType(appIssuedTypeRepository.findById(typeId).orElseThrow());
        
        nb.setIssuedToProId(null); // Strict Validation: It's an Employee
        nb.setAppAvblCnt(0);
        nb.setIsActive(1);
        nb.setCreatedBy(createdBy);
        return nb;
    }

	private void validateEmployeeExists(int employeeId, String role) {
		if (employeeId <= 0 || !employeeRepository.existsById(employeeId)) {
			throw new IllegalArgumentException(role + " employee not found or invalid ID: " + employeeId);
		}
	}
	
	// This is SPECIFICALLY for adding new stock to a Receiver (Zone/DGM)
    private void addStockToReceiver(int employeeId, int academicYearId, int typeId, int createdBy, Float amount, int newStart, int newEnd, int newCount) {
        
        // 1. Calculate the "Target End" (The number immediately before the new batch)
        int targetEnd = newStart - 1;

        // 2. Check if we can MERGE with an existing row
        Optional<BalanceTrack> mergeableRow = balanceTrackRepository.findMergeableRowForEmployee(
                academicYearId, employeeId, amount, targetEnd);

        if (mergeableRow.isPresent()) {
            // SCENARIO: CONTIGUOUS (1-50 exists, adding 51-100)
            BalanceTrack existing = mergeableRow.get();
            
            // Update the existing row
            existing.setAppTo(newEnd); // Extend the range (50 -> 100)
            existing.setAppAvblCnt(existing.getAppAvblCnt() + newCount); // Add count
            
            balanceTrackRepository.save(existing);
        } 
        else {
            // SCENARIO: DISTURBED / GAP (1-50 exists, adding 101-150)
            // Create a BRAND NEW row
            BalanceTrack newRow = createNewBalanceTrack(employeeId, academicYearId, typeId, createdBy);
            newRow.setEmployee(employeeRepository.findById(employeeId).orElseThrow());
            newRow.setIssuedToProId(null);
            newRow.setAmount(amount);
            newRow.setIsActive(1);
            
            // Set the specific range for this packet
            newRow.setAppFrom(newStart);
            newRow.setAppTo(newEnd);
            newRow.setAppAvblCnt(newCount);
            
            balanceTrackRepository.save(newRow);
        }
    }
}