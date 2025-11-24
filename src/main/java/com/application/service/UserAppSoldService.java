//package com.application.service;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.application.dto.UserAppSoldDTO;
//import com.application.dto.ZonePerformanceDTO;
//import com.application.entity.UserAppSold;
//import com.application.repository.UserAppSoldRepository;
//
//@Service
//public class UserAppSoldService {
//
//    @Autowired
//    private UserAppSoldRepository userAppSoldRepository;
//
//    // Define constants for entity types to make the code more readable
//    private static final int ENTITY_TYPE_ZONE = 2;
//    private static final int ENTITY_TYPE_DGM = 3;
//    private static final int ENTITY_TYPE_CAMPUS = 4;
//
//    // This private helper method handles the conversion logic for any performance data
//    private List<ZonePerformanceDTO> getPerformanceData(List<Object[]> rawData) {
//        return rawData.stream()
//                .limit(4) // Limit to the top/bottom 4
//                .map(result -> {
//                    ZonePerformanceDTO dto = new ZonePerformanceDTO();
//                    
//                    // result[0] is now the zone_name (String) from the query
//                    dto.setZoneName((String) result[0]);
//                    
//                    // result[1] is still the performance percentage
//                    dto.setPerformancePercentage(((BigDecimal) result[1]).doubleValue());
//                    
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }
//
//    // ... The rest of your service methods (getTopRatedZones, etc.) remain unchanged ...
//    
//    // --- Zones ---
//    public List<ZonePerformanceDTO> getTopRatedZones() {
//        List<Object[]> rawData = userAppSoldRepository.findTopPerformersByEntityType(ENTITY_TYPE_ZONE);
//        return getPerformanceData(rawData);
//    }
//
//    public List<ZonePerformanceDTO> getDropRatedZones() {
//        List<Object[]> rawData = userAppSoldRepository.findWorstPerformersByEntityType(ENTITY_TYPE_ZONE);
//        return getPerformanceData(rawData);
//    }
//    // --- DGMs ---
//    public List<ZonePerformanceDTO> getTopRatedDgms() {
//        List<Object[]> rawData = userAppSoldRepository.findTopPerformersByEntityType(ENTITY_TYPE_DGM);
//        return getPerformanceData(rawData);
//    }
//
//    public List<ZonePerformanceDTO> getDropRatedDgms() {
//        List<Object[]> rawData = userAppSoldRepository.findWorstPerformersByEntityType(ENTITY_TYPE_DGM);
//        return getPerformanceData(rawData);
//    }
//
//    // --- Campuses ---
//    public List<ZonePerformanceDTO> getTopRatedCampus() {
//        List<Object[]> rawData = userAppSoldRepository.findTopPerformersByEntityType(ENTITY_TYPE_CAMPUS);
//        return getPerformanceData(rawData);
//    }
//    
//    public List<ZonePerformanceDTO> getDropRatedCampus() {
//        List<Object[]> rawData = userAppSoldRepository.findWorstPerformersByEntityType(ENTITY_TYPE_CAMPUS);
//        return getPerformanceData(rawData);
//    }
//
//    // --- Original methods remain unchanged ---
//    public List<UserAppSoldDTO> getAnalyticsByEntityId(Integer entityId) {
//        List<UserAppSold> analyticsData = userAppSoldRepository.findByEntityId(entityId);
//        return analyticsData.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }
//
//    private UserAppSoldDTO convertToDto(UserAppSold userAppSold) {
//        UserAppSoldDTO dto = new UserAppSoldDTO();
//        dto.setEmpId(userAppSold.getEmpId());
//        dto.setEntityId(userAppSold.getEntityId());
//        dto.setAcdcYearId(userAppSold.getAcdcYearId());
//        dto.setTotalAppCount(userAppSold.getTotalAppCount());
//        dto.setSold(userAppSold.getSold());
//        return dto;
//    }
//}

package com.application.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.application.dto.FullGraphResponseDTO;
import com.application.dto.GraphBarDTO;
import com.application.dto.GraphDataDTO;
import com.application.dto.GraphResponseDTO;
import com.application.dto.PerformanceDTO;
import com.application.dto.RateItemDTO;
import com.application.dto.RateResponseDTO;
import com.application.dto.RateSectionDTO;
import com.application.dto.UserAppSoldDTO;
import com.application.entity.AcademicYear;
import com.application.entity.UserAppSold;
import com.application.repository.AcademicYearRepository;
import com.application.repository.UserAppSoldRepository;

@Service
public class UserAppSoldService {

    @Autowired
    private UserAppSoldRepository userAppSoldRepository;
    @Autowired
    private AcademicYearRepository academicYearRepository;

    // --- OVERALL ZONE RANKINGS ---
//    @Cacheable(value = "topRatedZones")
    public List<PerformanceDTO> getTopRatedZones() {
        Pageable topFour = PageRequest.of(0, 4);
        List<Object[]> rawData = userAppSoldRepository.findTopRatedZones(topFour);
        return mapToPerformanceDto(rawData);
    }

//    @Cacheable(value = "dropRatedZones")
    public List<PerformanceDTO> getDropRatedZones() {
        Pageable leastFour = PageRequest.of(0, 4);
        List<Object[]> rawData = userAppSoldRepository.findDropRatedZones(leastFour);
        return mapToPerformanceDto(rawData);
    }

    // --- OVERALL CAMPUS RANKINGS ---
//    @Cacheable(value = "topRatedCampus", key = "#employeeId")
    public List<PerformanceDTO> getTopRatedCampusesForEmployee(Integer employeeId) {
        Pageable topFour = PageRequest.of(0, 4);
        List<Object[]> rawData = userAppSoldRepository.findTopRatedCampusesByManager(employeeId, topFour);
        return mapToPerformanceDto(rawData);
    }

//    @Cacheable(value = "dropRatedCampus", key = "#employeeId")
    public List<PerformanceDTO> getDropRatedCampusesForEmployee(Integer employeeId) {
        Pageable leastFour = PageRequest.of(0, 4);
        List<Object[]> rawData = userAppSoldRepository.findDropRatedCampusesByManager(employeeId, leastFour);
        return mapToPerformanceDto(rawData);
    }

    // --- DGM RANKINGS FILTERED BY ZONE EMPLOYEE ID ---
//    @Cacheable(value = "topRatedDgms", key = "#employeeId")
    public List<PerformanceDTO> getTopRatedDgmsForEmployee(Integer employeeId) {
        Pageable topFour = PageRequest.of(0, 4);
        List<Object[]> rawData = userAppSoldRepository.findTopRatedDgmsByManager(employeeId, topFour);
        return mapToPerformanceDto(rawData);
    }

//    @Cacheable(value = "dropRatedDgms", key = "#employeeId")
    public List<PerformanceDTO> getDropRatedDgmsForEmployee(Integer employeeId) {
        Pageable leastFour = PageRequest.of(0, 4);
        List<Object[]> rawData = userAppSoldRepository.findDropRatedDgmsByManager(employeeId, leastFour);
        return mapToPerformanceDto(rawData);
    }

    // --- HELPER AND ANALYTICS METHODS ---
//    @Cacheable(value = "analyticsByEntity", key = "#entityId")
    public List<UserAppSoldDTO> getAnalyticsByEntityId(Integer entityId) {
        List<UserAppSold> analyticsData = userAppSoldRepository.findByEntityId(entityId);
        return analyticsData.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserAppSoldDTO convertToDto(UserAppSold userAppSold) {
        UserAppSoldDTO dto = new UserAppSoldDTO();
        dto.setEmpId(userAppSold.getEmpId());
        dto.setEntityId(userAppSold.getEntityId());
        dto.setAcdcYearId(userAppSold.getAcdcYearId());
        dto.setTotalAppCount(userAppSold.getTotalAppCount());
        dto.setSold(userAppSold.getSold());
        return dto;
    }

    private List<PerformanceDTO> mapToPerformanceDto(List<Object[]> rawData) {
        return rawData.stream()
                .map(result -> {
                    PerformanceDTO dto = new PerformanceDTO();
                    dto.setName((String) result[0]);
                    dto.setPerformancePercentage(((Number) result[1]).doubleValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    
//    public List<FullGraphResponseDTO> getAllGraphs() {
//        List<FullGraphResponseDTO> result = new ArrayList<>();
//
//        // 1. Zone wise (entityId = 2)
//        result.add(buildGraph("Zone wise graph", "DISTRIBUTE_ZONE", 2));
//
//        // 2. DGM wise (entityId = 3)
//        result.add(buildGraph("DGM wise graph", "DISTRIBUTE_DGM", 3));
//
//        // 3. Campus wise (entityId = 4)
//        result.add(buildGraph("Campus wise graph", "DISTRIBUTE_CAMPUS", 4));
//
//        return result;
//    }
//
//    // Common method to build each graph
//    private FullGraphResponseDTO buildGraph(String title, String permissionKey, int entityId) {
//
//        List<Object[]> rawList = userAppSoldRepository.getYearWiseIssuedAndSoldByEntity(entityId);
//
//        List<GraphBarDTO> barData = new ArrayList<>();
//
//        // Build graphBarData with issued=100 and sold=(sold/issued)*100
//        for (Object[] row : rawList) {
//            int year = (Integer) row[0];
//            int issuedRaw = ((Long) row[1]).intValue();   // totalAppCount
//            int soldRaw = ((Long) row[2]).intValue();
//
//            String academicYear = year + "-" + (year + 1);
//
//            // issued is always 100%
//            int issuedPercent = 100;
//
//            // soldPercent = performance
//            int soldPercent = 0;
//            if (issuedRaw > 0) {
//                soldPercent = (int) Math.round(((double) soldRaw / issuedRaw) * 100);
//            }
//
//            barData.add(new GraphBarDTO(academicYear, issuedPercent, soldPercent));
//        }
//
//        // graphData (compare last 2 years sold and issued)
//        double issuedChange = 0;
//        double soldChange = 0;
//
//        if (barData.size() >= 2) {
//            GraphBarDTO prev = barData.get(barData.size() - 2);
//            GraphBarDTO last = barData.get(barData.size() - 1);
//
//            issuedChange = calculatePercentChange(prev.getIssued(), last.getIssued());
//            soldChange = calculatePercentChange(prev.getSold(), last.getSold());
//        }
//
//        List<GraphDataDTO> summaryData = List.of(
//                new GraphDataDTO("Issued", issuedChange),
//                new GraphDataDTO("Sold", soldChange)
//        );
//
//        FullGraphResponseDTO dto = new FullGraphResponseDTO();
//        dto.setTitle(title);
//        dto.setPermissionKey(permissionKey);
//        dto.setGraphData(summaryData);
//        dto.setGraphBarData(barData);
//
//        return dto;
//    }
//
//
//    private double calculatePercentChange(int previous, int current) {
//        if (previous == 0) {
//            return 0;
//        }
//        return ((double) (current - previous) / previous) * 100;
//    }
//    
//    
    
    public List<RateResponseDTO> getAllRateData() {
        List<RateResponseDTO> result = new ArrayList<>();

        // Zone
        result.add(buildResponse(
                "zone",
                "DISTRIBUTE_ZONE",
                "Application Drop Rate Zone Wise",
                "Top Rated Zones",
                userAppSoldRepository.getZoneWiseRates()
        ));

        // DGM
        result.add(buildResponse(
                "dgm",
                "DISTRIBUTE_DGM",
                "Application Drop Rate DGM Wise",
                "Top Rated DGMs",
                userAppSoldRepository.getDgmWiseRates()
        ));

        // Campus
        result.add(buildResponse(
                "campus",
                "DISTRIBUTE_CAMPUS",
                "Application Drop Rate Campus Wise",
                "Top Rated Campuses",
                userAppSoldRepository.getCampusWiseRates()
        ));

        return result;
    }

    private RateResponseDTO buildResponse(
            String type,
            String permission,
            String dropTitle,
            String topTitle,
            List<Object[]> raw
    ) {

        List<RateItemDTO> items = new ArrayList<>();

        for (Object[] row : raw) {
            String name = (String) row[0];
            long issued = (Long) row[1];
            long sold = (Long) row[2];

            double percent = 0;

            if (issued > 0) {
                percent = ((double) sold / issued) * 100;
            }

            items.add(new RateItemDTO(name, percent));
        }

        List<RateItemDTO> topRated = items.stream()
                .sorted((a, b) -> Double.compare(b.getRate(), a.getRate()))
                .limit(4)
                .toList();

        List<RateItemDTO> dropRated = items.stream()
                .sorted(Comparator.comparingDouble(RateItemDTO::getRate))
                .limit(4)
                .toList();

        return new RateResponseDTO(
                type,
                permission,
                new RateSectionDTO(dropTitle, dropRated),
                new RateSectionDTO(topTitle, topRated)
        );
    }
    
    public GraphResponseDTO generateYearWiseIssuedSoldPercentage() {

        List<Object[]> rows = userAppSoldRepository.getYearWiseIssuedAndSold();
        // rows → [acdcYearId, SUM(totalAppCount), SUM(sold)]

        Map<Integer, AcademicYear> yearMap = academicYearRepository.findAll()
            .stream()
            .collect(Collectors.toMap(AcademicYear::getAcdcYearId, y -> y));

        List<GraphBarDTO> barList = new ArrayList<>();

        for (Object[] row : rows) {
            Integer yearId = (Integer) row[0];
            Long issued = row[1] != null ? ((Number) row[1]).longValue() : 0L;
            Long sold = row[2] != null ? ((Number) row[2]).longValue() : 0L;

            AcademicYear y = yearMap.get(yearId);
            String yearLabel = y != null ? y.getAcademicYear() : "Unknown Year";

            // Calculate sold percentage relative to issued
            int issuedPercent = 100;
            int soldPercent = 0;

            if (issued > 0) {
                soldPercent = (int) Math.round((sold.doubleValue() / issued.doubleValue()) * 100);
            }

            // ✅ Include both percentage and actual count in the DTO
            GraphBarDTO dto = new GraphBarDTO();
            dto.setYear(yearLabel);
            dto.setIssuedPercent(issuedPercent);
            dto.setSoldPercent(soldPercent);
            dto.setIssuedCount(issued.intValue());
            dto.setSoldCount(sold.intValue());

            barList.add(dto);
        }

        GraphResponseDTO response = new GraphResponseDTO();
        response.setGraphBarData(barList);

        return response;
    }


}

 
 