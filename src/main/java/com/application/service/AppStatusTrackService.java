package com.application.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.application.dto.AppStatusTrackDTO;
import com.application.dto.DashboardResponseDTO;
import com.application.dto.GenericDropdownDTO;
import com.application.dto.GraphBarDTO;
import com.application.dto.GraphResponseDTO;
import com.application.dto.MetricCardDTO;
import com.application.dto.MetricsAggregateDTO;
import com.application.entity.AcademicYear;
import com.application.repository.AcademicYearRepository;
import com.application.repository.AdminAppRepository;
import com.application.repository.AppStatusTrackRepository;
import com.application.repository.BalanceTrackRepository;
import com.application.repository.DgmRepository;
import com.application.repository.UserAppSoldRepository;

@Service
public class AppStatusTrackService {

    @Autowired
    private AppStatusTrackRepository appStatusTrackRepository;
    
    @Autowired
    private DgmRepository dgmRepository;
    @Autowired UserAppSoldService userAppSoldService;
    @Autowired private AdminAppRepository adminAppRepository;
    @Autowired private BalanceTrackRepository balanceTrackRepository;
    @Autowired private UserAppSoldRepository userAppSoldRepository;
    @Autowired private AcademicYearRepository academicYearRepository;
    

    /**
     * Gets the overall dashboard cards with year-over-year percentage change.
     */
//    @Cacheable(value = "dashboardCards")
    public List<MetricCardDTO> getDashboardCards() {
        Optional<AppStatusTrackDTO> currentStatsOptional = appStatusTrackRepository.findLatestAggregatedStats();
        AppStatusTrackDTO currentStats = currentStatsOptional.orElse(new AppStatusTrackDTO());
        return transformToMetricCards(currentStats);
    }

    /**
     * Gets dashboard cards for a single employee with year-over-year percentage change.
     */
//    @Cacheable(value = "dashboardCardsByEmployee", key = "#empId")
    public List<MetricCardDTO> getDashboardCardsByEmployee(Integer empId) {
        Optional<AppStatusTrackDTO> statsOptional = appStatusTrackRepository.findAggregatedStatsByEmployee(empId);
        AppStatusTrackDTO employeeStats = statsOptional.orElse(new AppStatusTrackDTO());
        return transformToMetricCards(employeeStats);
    }

    /**
     * PRIVATE HELPER: Converts stats DTO into metric cards with year-over-year percentage change.
     */
    private List<MetricCardDTO> transformToMetricCards(AppStatusTrackDTO stats) {
        List<MetricCardDTO> cards = new ArrayList<>();

        // Total Applications
        long totalThisYear = (stats.getTotalApplications() != null) ? stats.getTotalApplications() : 0L;
        long totalLastYear = (stats.getTotalApplicationsLastYear() != null) ? stats.getTotalApplicationsLastYear() : 0L;
        cards.add(new MetricCardDTO("Total Applications", (int) totalThisYear,
                calculatePercentageChange(totalThisYear, totalLastYear), "total_applications"));

        // Sold
        long soldThisYear = (stats.getAppSold() != null) ? stats.getAppSold() : 0L;
        long soldLastYear = (stats.getAppSoldLastYear() != null) ? stats.getAppSoldLastYear() : 0L;
        cards.add(new MetricCardDTO("Sold", (int) soldThisYear,
                calculatePercentageChange(soldThisYear, soldLastYear), "sold"));

        // Confirmed
        long confirmedThisYear = (stats.getAppConfirmed() != null) ? stats.getAppConfirmed() : 0L;
        long confirmedLastYear = (stats.getAppConfirmedLastYear() != null) ? stats.getAppConfirmedLastYear() : 0L;
        cards.add(new MetricCardDTO("Confirmed", (int) confirmedThisYear,
                calculatePercentageChange(confirmedThisYear, confirmedLastYear), "confirmed"));

        // Available
        long availableThisYear = (stats.getAppAvailable() != null) ? stats.getAppAvailable() : 0L;
        long availableLastYear = (stats.getAppAvailableLastYear() != null) ? stats.getAppAvailableLastYear() : 0L;
        cards.add(new MetricCardDTO("Available", (int) availableThisYear,
                calculatePercentageChange(availableThisYear, availableLastYear), "available"));

        // Issued
        long issuedThisYear = (stats.getAppIssued() != null) ? stats.getAppIssued() : 0L;
        long issuedLastYear = (stats.getAppIssuedLastYear() != null) ? stats.getAppIssuedLastYear() : 0L;
        cards.add(new MetricCardDTO("Issued", (int) issuedThisYear,
                calculatePercentageChange(issuedThisYear, issuedLastYear), "issued"));

        // Damaged
        long damagedThisYear = (stats.getAppDamaged() != null) ? stats.getAppDamaged() : 0L;
        long damagedLastYear = (stats.getAppDamagedLastYear() != null) ? stats.getAppDamagedLastYear() : 0L;
        cards.add(new MetricCardDTO("Damaged", (int) damagedThisYear,
                calculatePercentageChange(damagedThisYear, damagedLastYear), "damaged"));

        // Unavailable
        long unavailableThisYear = (stats.getAppUnavailable() != null) ? stats.getAppUnavailable() : 0L;
        long unavailableLastYear = (stats.getAppUnavailableLastYear() != null) ? stats.getAppUnavailableLastYear() : 0L;
        cards.add(new MetricCardDTO("Unavailable", (int) unavailableThisYear,
                calculatePercentageChange(unavailableThisYear, unavailableLastYear), "unavailable"));

        return cards;
    }

    /**
     * HELPER METHOD: Calculates percentage change between a current and previous value.
     */
    private int calculatePercentageChange(long currentValue, long previousValue) {
        if (previousValue == 0) {
            // If previous was 0, any increase is considered 100% growth.
            return currentValue > 0 ? 100 : 0;
        }
        // Formula: ((current - previous) / previous) * 100
        return (int) (((double) (currentValue - previousValue) / previousValue) * 100);
    }
    
    @Cacheable(value = "dgmEmployees")
    public List<GenericDropdownDTO> getAllDgmEmployees() {
        return dgmRepository.findAllDgmEmployees();
    }

    public DashboardResponseDTO getDashboardData() {

        // Fetch both parts
        List<MetricCardDTO> metrics = this.getMetricCards(); // ✅ call directly
        GraphResponseDTO graph = userAppSoldService.generateYearWiseIssuedSoldPercentage();

        // Combine them
        DashboardResponseDTO response = new DashboardResponseDTO();
        response.setMetricCards(metrics);
        response.setGraphData(graph);

        return response;
    }

    /**
     * Get dashboard cards showing totals and percentage changes
     */
    public List<MetricCardDTO> getMetricCards() {

        Integer currentYearId = appStatusTrackRepository.findLatestYearId();
        Integer previousYearId = currentYearId - 1;

        // Overall totals (for all years)
        Object[] overall = appStatusTrackRepository.getOverallTotals().get(0);
        Long overallWithPro = appStatusTrackRepository.getOverallWithPro(); // total_app where type = 4

        // Current & previous year values
        Object[] curr = appStatusTrackRepository.getTotalsByYear(currentYearId).get(0);
        Object[] prev = appStatusTrackRepository.getTotalsByYear(previousYearId).get(0);

        Long currProObj = appStatusTrackRepository.getWithProByYear(currentYearId);
        Long prevProObj = appStatusTrackRepository.getWithProByYear(previousYearId);

        // Convert safely
        int totalApp = toInt(overall[0]);
        int sold = toInt(overall[1]);
        int confirmed = toInt(overall[2]);
        int available = toInt(overall[3]);
        int issued = toInt(overall[4]);
        int damaged = toInt(overall[5]);
        int unavailable = toInt(overall[6]);
        int withProValue = toInt(overallWithPro);

        int currTotalApp = toInt(curr[0]);
        int currSold = toInt(curr[1]);
        int currConfirmed = toInt(curr[2]);
        int currAvailable = toInt(curr[3]);
        int currIssued = toInt(curr[4]);
        int currDamaged = toInt(curr[5]);
        int currUnavailable = toInt(curr[6]);
        int currPro = toInt(currProObj);

        int prevTotalApp = toInt(prev[0]);
        int prevSold = toInt(prev[1]);
        int prevConfirmed = toInt(prev[2]);
        int prevAvailable = toInt(prev[3]);
        int prevIssued = toInt(prev[4]);
        int prevDamaged = toInt(prev[5]);
        int prevUnavailable = toInt(prev[6]);
        int prevPro = toInt(prevProObj);

        // Build final list
        List<MetricCardDTO> list = new ArrayList<>();
        list.add(new MetricCardDTO("Total Applications", totalApp, clampChange(prevTotalApp, currTotalApp), "total_applications"));
        list.add(new MetricCardDTO("Sold", sold, clampChange(prevSold, currSold), "sold"));
        list.add(new MetricCardDTO("Confirmed", confirmed, clampChange(prevConfirmed, currConfirmed), "confirmed"));
        list.add(new MetricCardDTO("Available", available, clampChange(prevAvailable, currAvailable), "available"));
        list.add(new MetricCardDTO("Issued", issued, clampChange(prevIssued, currIssued), "issued"));
        list.add(new MetricCardDTO("Damaged", damaged, clampChange(prevDamaged, currDamaged), "damaged"));
        list.add(new MetricCardDTO("Unavailable", unavailable, clampChange(prevUnavailable, currUnavailable), "unavailable"));
        list.add(new MetricCardDTO("With PRO", withProValue, clampChange(prevPro, currPro), "with_pro"));

        return list;
    }
    

    // Helper conversions
    private int toInt(Object o) {
        return o == null ? 0 : ((Number) o).intValue();
    }

    private int clampChange(int prev, int curr) {
        if (prev == 0) return 100;
        double raw = ((double) (curr - prev) / prev) * 100;
        return clamp(raw);
    }

    private int clamp(double value) {
        if (value > 100) return 100;
        if (value < -100) return -100;
        return (int) Math.round(value);
    }
    //new metrcis
    
    
    
    
    
public DashboardResponseDTO getDashboardData(Integer employeeId) {
        
        // Get current year (latest year) from AppStatusTrackRepository, same as AppStatusTrackService
        Integer currentYearId = appStatusTrackRepository.findLatestYearId();
        Integer previousYearId = currentYearId - 1;
        
        // Sum total_app from AdminApp table for given employee and current academic year
        Long currentYearTotal = adminAppRepository.sumTotalAppByEmployeeAndAcademicYear(employeeId, currentYearId);
        int currTotalApplications = currentYearTotal != null ? currentYearTotal.intValue() : 0;
        
        // Sum total_app from AdminApp table for given employee and previous academic year
        Long previousYearTotal = adminAppRepository.sumTotalAppByEmployeeAndAcademicYear(employeeId, previousYearId);
        int prevTotalApplications = previousYearTotal != null ? previousYearTotal.intValue() : 0;
        
        // Calculate percentage change (same logic as AppStatusTrackService)
        int percentageChange = clampChange(prevTotalApplications, currTotalApplications);
        
        // Get metrics data from AppStatusTrack for current year
        Optional<MetricsAggregateDTO> currentYearMetrics = appStatusTrackRepository.getMetricsByEmployeeAndYear(employeeId, currentYearId);
        long currSold = 0L;
        long currConfirmed = 0L;
        long currDamaged = 0L;
        long currUnavailable = 0L;
        if (currentYearMetrics.isPresent()) {
            MetricsAggregateDTO metrics = currentYearMetrics.get();
            currSold = metrics.appSold();
            currConfirmed = metrics.appConfirmed();
            currDamaged = metrics.appDamaged();
            currUnavailable = metrics.appUnavailable();
        }
        
        // Get metrics data from AppStatusTrack for previous year
        Optional<MetricsAggregateDTO> previousYearMetrics = appStatusTrackRepository.getMetricsByEmployeeAndYear(employeeId, previousYearId);
        long prevSold = 0L;
        long prevConfirmed = 0L;
        long prevDamaged = 0L;
        long prevUnavailable = 0L;
        if (previousYearMetrics.isPresent()) {
            MetricsAggregateDTO metrics = previousYearMetrics.get();
            prevSold = metrics.appSold();
            prevConfirmed = metrics.appConfirmed();
            prevDamaged = metrics.appDamaged();
            prevUnavailable = metrics.appUnavailable();
        }
        
        // Get available data from BalanceTrack for current year
        Long currentYearAvailable = balanceTrackRepository.sumAppAvblCntByEmployeeAndAcademicYear(employeeId, currentYearId);
        int currAvailable = currentYearAvailable != null ? currentYearAvailable.intValue() : 0;
        
        // Get available data from BalanceTrack for previous year
        Long previousYearAvailable = balanceTrackRepository.sumAppAvblCntByEmployeeAndAcademicYear(employeeId, previousYearId);
        int prevAvailable = previousYearAvailable != null ? previousYearAvailable.intValue() : 0;
        
        // Calculate Issued = Total App (AdminApp) - Available (BalanceTrack)
        int currIssued = currTotalApplications - currAvailable;
        int prevIssued = prevTotalApplications - prevAvailable;
        
        // Get With PRO data from AppStatusTrack (issuedByType.appIssuedId = 4) for current year
        Long currentYearWithPro = appStatusTrackRepository.getWithProAvailableByEmployeeAndYear(employeeId, currentYearId);
        int currWithPro = currentYearWithPro != null ? currentYearWithPro.intValue() : 0;
        
        // Get With PRO data from AppStatusTrack for previous year
        Long previousYearWithPro = appStatusTrackRepository.getWithProAvailableByEmployeeAndYear(employeeId, previousYearId);
        int prevWithPro = previousYearWithPro != null ? previousYearWithPro.intValue() : 0;
        
        // Calculate percentage changes
        int soldPercentageChange = clampChange((int) prevSold, (int) currSold);
        int confirmedPercentageChange = clampChange((int) prevConfirmed, (int) currConfirmed);
        int damagedPercentageChange = clampChange((int) prevDamaged, (int) currDamaged);
        int unavailablePercentageChange = clampChange((int) prevUnavailable, (int) currUnavailable);
        int availablePercentageChange = clampChange(prevAvailable, currAvailable);
        int issuedPercentageChange = clampChange(prevIssued, currIssued);
        int withProPercentageChange = clampChange(prevWithPro, currWithPro);
 
        // Create metric cards
        List<MetricCardDTO> metricCards = new ArrayList<>();
        metricCards.add(new MetricCardDTO("Total Applications", currTotalApplications, percentageChange, "total_applications"));
        metricCards.add(new MetricCardDTO("Sold", (int) currSold, soldPercentageChange, "sold"));
        metricCards.add(new MetricCardDTO("Confirmed", (int) currConfirmed, confirmedPercentageChange, "confirmed"));
        metricCards.add(new MetricCardDTO("Available", currAvailable, availablePercentageChange, "available"));
        metricCards.add(new MetricCardDTO("Issued", currIssued, issuedPercentageChange, "issued"));
        metricCards.add(new MetricCardDTO("Damaged", (int) currDamaged, damagedPercentageChange, "damaged"));
        metricCards.add(new MetricCardDTO("Unavailable", (int) currUnavailable, unavailablePercentageChange, "unavailable"));
        metricCards.add(new MetricCardDTO("With PRO", currWithPro, withProPercentageChange, "with_pro"));
 
        // Generate graph data for previous 4 years (current year + 3 previous years)
        GraphResponseDTO graphData = generateGraphData(employeeId, currentYearId);
 
        // Create response
        DashboardResponseDTO response = new DashboardResponseDTO();
        response.setMetricCards(metricCards);
        response.setGraphData(graphData);
 
        return response;
    }
    
    
    private GraphResponseDTO generateGraphData(Integer employeeId, Integer currentYearId) {
        // Get previous 4 years (current year + 3 previous years)
        List<Integer> yearIds = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            yearIds.add(currentYearId - i);
        }
        
        // Get year-wise data from UserAppSold for this employee
        List<Object[]> rows = userAppSoldRepository.getYearWiseIssuedAndSoldByEmployee(employeeId, yearIds);
        
        // Create a map of yearId -> [issued, sold] for quick lookup
        Map<Integer, long[]> yearDataMap = new HashMap<>();
        for (Object[] row : rows) {
            Integer yearId = (Integer) row[0];
            Long issued = row[1] != null ? ((Number) row[1]).longValue() : 0L;
            Long sold = row[2] != null ? ((Number) row[2]).longValue() : 0L;
            yearDataMap.put(yearId, new long[]{issued, sold});
        }
        
        // Get AcademicYear entities for year labels
        List<AcademicYear> academicYears = academicYearRepository.findByAcdcYearIdIn(yearIds);
        Map<Integer, AcademicYear> yearMap = academicYears.stream()
            .collect(Collectors.toMap(AcademicYear::getAcdcYearId, y -> y));
        
        // Build graph bar data for all 4 years
        List<GraphBarDTO> barList = new ArrayList<>();
        for (Integer yearId : yearIds) {
            long[] data = yearDataMap.getOrDefault(yearId, new long[]{0L, 0L});
            long issued = data[0];
            long sold = data[1];
            
            AcademicYear year = yearMap.get(yearId);
            String yearLabel = year != null ? year.getAcademicYear() : "Year " + yearId;
            
            // Calculate percentages
            int issuedPercent = 100; // Always 100% as baseline
            int soldPercent = 0;
            if (issued > 0) {
                soldPercent = (int) Math.round((sold * 100.0) / issued);
            }
            
            GraphBarDTO dto = new GraphBarDTO();
            dto.setYear(yearLabel);
            dto.setIssuedPercent(issuedPercent);
            dto.setSoldPercent(soldPercent);
            dto.setIssuedCount((int) issued);
            dto.setSoldCount((int) sold);
            
            barList.add(dto);
        }
        
        GraphResponseDTO response = new GraphResponseDTO();
        response.setGraphBarData(barList);
        
        return response;
    }
 

}