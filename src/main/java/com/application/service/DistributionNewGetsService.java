//package com.application.service;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.application.dto.AppDistributionDTO;
//import com.application.dto.AppFromDTO;
//import com.application.dto.AppRangeDTO;
//import com.application.dto.LocationAutoFillDTO;
//import com.application.dto.RangeResponseDTO;
//import com.application.entity.AdminApp;
//import com.application.entity.Campus;
//import com.application.entity.City;
//import com.application.entity.Dgm;
//import com.application.entity.District;
//import com.application.repository.AdminAppRepository;
//import com.application.repository.BalanceTrackRepository;
//import com.application.repository.DgmRepository;
//import com.application.repository.DistributionRepository;
//
//
//@Service
//public class DistributionNewGetsService {
//	
//	
//	@Autowired private AdminAppRepository adminAppRepository;
//	@Autowired private BalanceTrackRepository balanceTrackRepository;
//	@Autowired private DistributionRepository distributionRepository;
//	@Autowired private DgmRepository dgmRepository;
//	
//	//for amounts
//	public List<Double> getApplicationFees(int empId, int academicYearId) { // UPDATED SIGNATURE
//	    
//	    // 1. Check AdminApp table first (UPDATED CALL)
//	    List<Integer> adminFees = adminAppRepository.findAmountsByEmpIdAndAcademicYear(empId, academicYearId);
//
//	    // 2. If AdminApp has data, convert to Double and return
//	    if (adminFees != null && !adminFees.isEmpty()) {
//	        return adminFees.stream()
//	                .map(Double::valueOf)
//	                .collect(Collectors.toList());
//	    }
//
//	    // 3. If AdminApp is empty, check BalanceTrack table (UPDATED CALL)
//	    List<Float> balanceFees = balanceTrackRepository.findAmountsByEmpIdAndAcademicYear(empId, academicYearId);
//
//	    if (balanceFees != null && !balanceFees.isEmpty()) {
//	        return balanceFees.stream()
//	                .map(Double::valueOf)
//	                .collect(Collectors.toList());
//	    }
//
//	    // 4. If both are empty, return an empty list
//	    return Collections.emptyList();
//	}
//	
//	//for from,start,range
////	
////	public AppRangeDTO getAppRange(int empId, int academicYearId, float amount) {
////	    
////	    // --- 1. PRIMARY CHECK (Distributed & Tracked) ---
////	    // ... (distDTO retrieval remains the same)
////	    
////	    AppDistributionDTO distDTO = distributionRepository
////	            .findActiveAppRange(empId, academicYearId, amount)
////	            .orElse(null);
////
////	    // UPDATED CALL for AppFromDTO with new field
////	    AppFromDTO fromDTO = balanceTrackRepository
////	            .getAppFromByEmployeeAndAcademicYearAndAmount(empId, academicYearId, amount)
////	            .orElse(null);
////
////	    // If ANY primary data is found, merge and return.
////	    if (distDTO != null || fromDTO != null) {
////	        
////	        Integer appStartNo = distDTO != null ? distDTO.getAppStartNo() : null;
////	        Integer appEndNo = distDTO != null ? distDTO.getAppEndNo() : null;
////	        
////	        Integer appFrom = fromDTO != null ? fromDTO.getAppFrom() : null;
////	        Integer appBalanceTrkId = fromDTO != null ? fromDTO.getAppBalanceTrkId() : null;
////	        
////	        // NEW: Get count from BalanceTrack (fromDTO)
////	        Integer appCount = fromDTO != null ? fromDTO.getAppAvblCnt() : null; 
////
////	        // UPDATED DTO RETURN
////	        return new AppRangeDTO(appStartNo, appEndNo, appFrom, appBalanceTrkId, appCount);
////	    }
////
////	    // --- 2. FALLBACK CHECK (AdminApp/Untracked Block) ---
////	    
////	    // The AdminAppRepository query now handles setting appCount and appBalanceTrkId correctly
////	    Optional<AppRangeDTO> adminAppRange = adminAppRepository
////	                                            .findDefaultAppRangeDto(empId, academicYearId, amount);
////
////	    if (adminAppRange.isPresent()) {
////	        return adminAppRange.get();
////	    }
////	    
////	    // --- 3. FINAL FALLBACK: No Data Found ---
////	    return null; 
////	}
//	
//	
//	//autopopulate city and district
//	
//	 public LocationAutoFillDTO getAutoPopulateData(int empId, String category) {
//
//	        // 1️⃣ Only apply logic when category = "school"
//	        if (!"school".equalsIgnoreCase(category)) {
//	            return null;  
//	        }
//
//	        // 2️⃣ Get active DGM record for employee
//	        Dgm dgm = dgmRepository
//	                .findActiveDgm(empId, 1)
//	                .orElse(null);
//
//	        if (dgm == null) {
//	            return null;
//	        }
//
//	        // 3️⃣ DISTRICT (direct from Dgm table)
//	        District district = dgm.getDistrict();
//
//	        Integer districtId   = district != null ? district.getDistrictId() : null;
//	        String districtName  = district != null ? district.getDistrictName() : null;
//
//	        // 4️⃣ CITY (via Campus)
//	        Campus campus = dgm.getCampus();
//	        City city = (campus != null) ? campus.getCity() : null;
//
//	        Integer cityId   = city != null ? city.getCityId() : null;
//	        String cityName  = city != null ? city.getCityName() : null;
//
//	        // 5️⃣ Return final DTO
//	        return new LocationAutoFillDTO(cityId, cityName, districtId, districtName);
//	    }
//	 
//	 
//	 
//	 //complexlogic
//	 
////	 @Transactional(readOnly = true)
////	    public RangeResponseDTO getRangesOrSingleWithNextStart(int empId, int academicYearId, float amount) {
////
////	        // 1) Get distributions for this emp (issued_to_emp_id)
////	        List<AppDistributionDTO> distributions =
////	                distributionRepository.findByIssuedToEmployeeAndYearAndAmount(empId, academicYearId, amount);
////
////	        if (distributions.isEmpty()) {
////	            // Treat as admin (fallback to AdminApp)
////	            Optional<AppRangeDTO> adminRangeOpt = adminAppRepository.findDefaultAppRange(empId, academicYearId, amount);
////
////	            if (adminRangeOpt.isPresent()) {
////	                AppRangeDTO adminRange = adminRangeOpt.get();
////
////	                // Check if admin has BalanceTrack entries (use employee id)
////	                List<AppFromDTO> adminBTList = balanceTrackRepository.findLatestByEmployeeYearAndAmount(empId, academicYearId, amount);
////
////	                if (!adminBTList.isEmpty()) {
////	                    AppFromDTO bt = adminBTList.get(0);
////	                    // override appFrom and appCount from BalanceTrack
////	                    adminRange.setAppFrom(bt.getAppFrom());
////	                    adminRange.setAppBalanceTrkId(bt.getAppBalanceTrkId());
////	                    adminRange.setAppCount(bt.getAppAvblCnt());
////	                }
////	                // Return single range with next start (admin)
////	                return new RangeResponseDTO(Collections.emptyList(), adminRange);
////	            }
////
////	            // No adminapp found either -> nothing to return
////	            return new RangeResponseDTO(Collections.emptyList(), null);
////	        }
////
////	        // 2) Merge contiguous / overlapping ranges -> Option A behavior
////	        List<AppDistributionDTO> merged = mergeRanges(distributions);
////
////	        if (merged.size() == 1) {
////	            // Single merged block — compute next start using BalanceTrack for this employee
////	            AppDistributionDTO block = merged.get(0);
////	            AppRangeDTO range = computeAppRangeForBlock(empId, academicYearId, amount, block.getAppStartNo(), block.getAppEndNo());
////	            return new RangeResponseDTO(Collections.singletonList(block), range);
////	        } else {
////	            // Multiple blocks -> return only mergedRanges, frontend will pick one and call second API
////	            return new RangeResponseDTO(merged, null);
////	        }
////	    }
////
////	    /**
////	     * API 2 — After frontend selects a block, compute next start within that selected block.
////	     */
////	    @Transactional(readOnly = true)
////	    public AppRangeDTO getNextStartForSelectedBlock(int empId, int academicYearId, float amount, int selectedStart, int selectedEnd) {
////
////	        // compute for this specific block
////	        return computeAppRangeForBlock(empId, academicYearId, amount, selectedStart, selectedEnd);
////	    }
////
////	    /* ------------------ helper methods ------------------ */
////
////	    private AppRangeDTO computeAppRangeForBlock(int empId, int academicYearId, float amount, int blockStart, int blockEnd) {
////
////	        // Default appFrom = blockStart, appCount = blockEnd - blockStart + 1, btId = null
////	        Integer appFrom = blockStart;
////	        Integer appCount = blockEnd - blockStart + 1;
////	        Integer btId = null;
////
////	        // Find latest BalanceTrack for this employee (employee holds apps)
////	        List<AppFromDTO> btList = balanceTrackRepository.findLatestByEmployeeYearAndAmount(empId, academicYearId, amount);
////
////	        if (!btList.isEmpty()) {
////	            AppFromDTO bt = btList.get(0);
////	            btId = bt.getAppBalanceTrkId();
////
////	            // If BT.appFrom lies within the selected block, start from BT.appFrom.
////	            // If BT.appFrom < blockStart, start from blockStart (since blockStart is the smallest allowed)
////	            // If BT.appFrom > blockEnd -> no availability in this block, return appCount=0 and appFrom=blockStart (or you can return null).
////	            int btFrom = bt.getAppFrom();
////	            int btAvail = bt.getAppAvblCnt() != null ? bt.getAppAvblCnt() : 0;
////
////	            if (btFrom > blockEnd) {
////	                // no available numbers in this selected block
////	                appFrom = blockStart;
////	                appCount = 0;
////	            } else {
////	                // choose start as the larger of btFrom and blockStart
////	                appFrom = Math.max(btFrom, blockStart);
////	                // available count clipped to the block
////	                int possible = blockEnd - appFrom + 1;
////	                appCount = Math.min(btAvail, possible);
////	            }
////	        }
////
////	        return new AppRangeDTO(blockStart, blockEnd, appFrom, btId, appCount);
////	    }
////
////	    /**
////	     * Merge contiguous or overlapping ranges.
////	     * Example: [1-50],[51-100],[200-250] -> [1-100],[200-250]
////	     */
////	    private List<AppDistributionDTO> mergeRanges(List<AppDistributionDTO> input) {
////	        if (input == null || input.isEmpty()) return Collections.emptyList();
////
////	        // copy & sort by start
////	        List<AppDistributionDTO> list = new ArrayList<>(input);
////	        list.sort(Comparator.comparingInt(AppDistributionDTO::getAppStartNo));
////
////	        List<AppDistributionDTO> merged = new ArrayList<>();
////	        int curStart = list.get(0).getAppStartNo();
////	        int curEnd = list.get(0).getAppEndNo();
////
////	        for (int i = 1; i < list.size(); i++) {
////	            AppDistributionDTO r = list.get(i);
////	            int s = r.getAppStartNo();
////	            int e = r.getAppEndNo();
////
////	            if (s <= curEnd + 1) { // overlap or adjacent -> merge
////	                curEnd = Math.max(curEnd, e);
////	            } else {
////	                merged.add(new AppDistributionDTO(curStart, curEnd));
////	                curStart = s;
////	                curEnd = e;
////	            }
////	        }
////	        // final
////	        merged.add(new AppDistributionDTO(curStart, curEnd));
////	        return merged;
////	    }
//	    
////	 @Transactional(readOnly = true)
////	    public RangeResponseDTO getRangesOrSingleWithNextStart(int empId, int academicYearId, float amount) {
////
////	        // 0) Check AdminApp first — if present, empId is considered Admin (top-level)
////	        Optional<AdminApp> adminOpt = adminAppRepository.findDefaultAppRange(empId, academicYearId, amount);
////	        if (adminOpt.isPresent()) {
////	            AdminApp adminApp = adminOpt.get();
////	            int blockStart = adminApp.getApp_from_no();
////	            int blockEnd = adminApp.getApp_to_no();
////
////	            // For admin, lookup BT by createdBy (giver)
////	            AppRangeDTO range = computeAppRangeForBlock(empId, academicYearId, amount,
////	                                                       blockStart, blockEnd,
////	                                                       /*useCreatedBy=*/ true);
////
////	            AppDistributionDTO adminBlock = new AppDistributionDTO(blockStart, blockEnd);
////	            return new RangeResponseDTO(Collections.singletonList(adminBlock), range);
////	        }
////
////	        // 1) Non-admin: check RECEIVED distributions (issued_to_emp_id = empId)
////	        List<AppDistributionDTO> received =
////	                distributionRepository.findByIssuedToEmployeeAndYearAndAmount(empId, academicYearId, amount);
////
////	        // 2) Non-admin: check GIVEN distributions (created_by = empId)
////	        List<AppDistributionDTO> given =
////	                distributionRepository.findByCreatedByAndYearAndAmount(empId, academicYearId, amount);
////
////	        List<AppDistributionDTO> blocks = null;
////	        boolean isGiver = false;
////
////	        if (received != null && !received.isEmpty()) {
////	            blocks = received;
////	            isGiver = false; // receiver context
////	        } else if (given != null && !given.isEmpty()) {
////	            blocks = given;
////	            isGiver = true; // giver context
////	        }
////
////	        if (blocks == null || blocks.isEmpty()) {
////	            // Nothing found for non-admin user
////	            return new RangeResponseDTO(Collections.emptyList(), null);
////	        }
////
////	        // Merge contiguous/overlapping ranges
////	        List<AppDistributionDTO> merged = mergeRanges(blocks);
////
////	        if (merged.size() == 1) {
////	            AppDistributionDTO block = merged.get(0);
////	            AppRangeDTO range = computeAppRangeForBlock(empId, academicYearId, amount,
////	                                                       block.getAppStartNo(), block.getAppEndNo(),
////	                                                       isGiver);
////	            return new RangeResponseDTO(Collections.singletonList(block), range);
////	        } else {
////	            // Multiple blocks -> frontend chooses one and calls the second API
////	            return new RangeResponseDTO(merged, null);
////	        }
////	    }
//
//	    /**
//	     * API 2 — After frontend selects a block, compute next start inside that selected block.
//	     * Default behavior assumes receiver context (employee lookup).
//	     */
////	    @Transactional(readOnly = true)
////	    public AppRangeDTO getNextStartForSelectedBlock(int empId, int academicYearId, float amount, int selectedStart, int selectedEnd) {
////	        // Default to receiver lookup. If frontend needs createdBy lookup (admin block), call the overloaded method.
////	        return computeAppRangeForBlock(empId, academicYearId, amount, selectedStart, selectedEnd, /*useCreatedBy=*/ false);
////	    }
////
////	    /**
////	     * Overloaded API 2 — allow explicit createdBy context (useful when frontend selects an admin block and wants BT by createdBy).
////	     */
////	    @Transactional(readOnly = true)
////	    public AppRangeDTO getNextStartForSelectedBlock(int empId, int academicYearId, float amount, int selectedStart, int selectedEnd, boolean useCreatedBy) {
////	        return computeAppRangeForBlock(empId, academicYearId, amount, selectedStart, selectedEnd, useCreatedBy);
////	    }
//
//	    /* ------------------ helper methods ------------------ */
//
//	    /**
//	     * Compute next start and available count for a block.
//	     *
//	     * @param empId       id used for lookup (if useCreatedBy=true, this is used as createdBy; otherwise as employee.id)
//	     * @param academicYearId academic year id
//	     * @param amount      amount filter
//	     * @param blockStart  block start (static)
//	     * @param blockEnd    block end (static)
//	     * @param useCreatedBy whether to lookup BalanceTrack by createdBy (giver) or by employee.id (receiver)
//	     * @return AppRangeDTO containing computed next start (appFrom), btId, and appCount (available count within block)
//	     */
////	    private AppRangeDTO computeAppRangeForBlock(int empId, int academicYearId, float amount, int blockStart, int blockEnd, boolean useCreatedBy) {
////
////	        // Default: full block available
////	        Integer appFrom = blockStart;
////	        Integer appCount = blockEnd - blockStart + 1;
////	        Integer btId = null;
////
////	        List<AppFromDTO> btList;
////	        if (useCreatedBy) {
////	            btList = balanceTrackRepository.findLatestByCreatedByAndYearAndAmount(empId, academicYearId, amount);
////	        } else {
////	            btList = balanceTrackRepository.findLatestByEmployeeYearAndAmount(empId, academicYearId, amount);
////	        }
////
////	        if (btList != null && !btList.isEmpty()) {
////	            AppFromDTO bt = btList.get(0);
////	            btId = bt.getAppBalanceTrkId();
////
////	            int btFrom = bt.getAppFrom();
////	            int btAvail = bt.getAppAvblCnt() != null ? bt.getAppAvblCnt() : 0;
////
////	            if (btFrom > blockEnd) {
////	                // no available numbers in this selected block
////	                appFrom = blockStart;
////	                appCount = 0;
////	            } else {
////	                // choose start as the larger of btFrom and blockStart
////	                appFrom = Math.max(btFrom, blockStart);
////	                int possible = blockEnd - appFrom + 1;
////	                appCount = Math.min(btAvail, possible);
////	            }
////	        } else {
////	            // No BalanceTrack found -> appFrom stays at blockStart and appCount is full block size
////	            appFrom = blockStart;
////	            appCount = blockEnd - blockStart + 1;
////	            btId = null;
////	        }
////
////	        return new AppRangeDTO(blockStart, blockEnd, appFrom, btId, appCount);
////	    }
//
//	    /**
//	     * Merge contiguous or overlapping ranges.
//	     * Example: [1-50],[51-100],[200-250] -> [1-100],[200-250]
//	     */
//	    private List<AppDistributionDTO> mergeRanges(List<AppDistributionDTO> input) {
//	        if (input == null || input.isEmpty()) return Collections.emptyList();
//
//	        // copy & sort by start
//	        List<AppDistributionDTO> list = new ArrayList<>(input);
//	        list.sort(Comparator.comparingInt(AppDistributionDTO::getAppStartNo));
//
//	        List<AppDistributionDTO> merged = new ArrayList<>();
//	        int curStart = list.get(0).getAppStartNo();
//	        int curEnd = list.get(0).getAppEndNo();
//
//	        for (int i = 1; i < list.size(); i++) {
//	            AppDistributionDTO r = list.get(i);
//	            int s = r.getAppStartNo();
//	            int e = r.getAppEndNo();
//
//	            if (s <= curEnd + 1) { // overlap or adjacent -> merge
//	                curEnd = Math.max(curEnd, e);
//	            } else {
//	                merged.add(new AppDistributionDTO(curStart, curEnd));
//	                curStart = s;
//	                curEnd = e;
//	            }
//	        }
//	        // final
//	        merged.add(new AppDistributionDTO(curStart, curEnd));
//	        return merged;
//	    }
//
//	    
//	
//}
