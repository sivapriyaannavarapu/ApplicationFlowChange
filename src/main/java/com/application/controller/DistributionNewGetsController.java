//package com.application.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.application.dto.AppRangeDTO;
//import com.application.dto.LocationAutoFillDTO;
//import com.application.dto.RangeResponseDTO;
//import com.application.service.DistributionNewGetsService;
//
//@RestController
//@RequestMapping("/distribution/newgets")
//public class DistributionNewGetsController {
//
//    @Autowired
//    private DistributionNewGetsService distributionNewGetsService;
//
////    @GetMapping("/getallamounts/{empId}/{academicYearId}") // UPDATED PATH
////    public ResponseEntity<List<Double>> getFeeDropdown(
////        @PathVariable int empId,
////        @PathVariable int academicYearId // NEW PATH VARIABLE
////    ) {
////        List<Double> fees = distributionNewGetsService.getApplicationFees(empId, academicYearId);
////        return ResponseEntity.ok(fees);
////    }
//    
////    @GetMapping("/ranges/{empId}/{academicYearId}")
////    public ResponseEntity<AppRangeDTO> getAppRangeData(
////        @PathVariable int empId,
////        @PathVariable int academicYearId,
////        @RequestParam float amount // New request parameter for amount
////    ) {
////        AppRangeDTO range = distributionNewGetsService.getAppRange(empId, academicYearId, amount);
////        
////        if (range == null) {
////            return ResponseEntity.notFound().build();
////        }
////        return ResponseEntity.ok(range);
////    }
//    
//    
//    @GetMapping("/district_city_autopopulate/{empId}/{category}")
//    public ResponseEntity<LocationAutoFillDTO> autoFill(
//            @PathVariable int empId,
//            @PathVariable String category) {
//
//        LocationAutoFillDTO dto = distributionNewGetsService.getAutoPopulateData(empId, category);
//
//        return ResponseEntity.ok(dto);
//    }
//    
//    
////    @GetMapping("/{empId}/{academicYearId}")
////    public ResponseEntity<RangeResponseDTO> getRanges(
////            @PathVariable int empId,
////            @PathVariable int academicYearId,
////            @RequestParam("amount") float amount
////    ) {
////        RangeResponseDTO resp = distributionNewGetsService.getRangesOrSingleWithNextStart(empId, academicYearId, amount);
////        if ((resp.getMergedRanges() == null || resp.getMergedRanges().isEmpty()) && resp.getSelectedRange() == null) {
////            return ResponseEntity.notFound().build();
////        }
////        return ResponseEntity.ok(resp);
////    }
//
//    /**
//     * 2) After frontend selects a specific merged block, call this to get the next start inside selected block.
//     */
////    @GetMapping("/{empId}/{academicYearId}/next")
////    public ResponseEntity<AppRangeDTO> getNextForSelectedBlock(
////            @PathVariable int empId,
////            @PathVariable int academicYearId,
////            @RequestParam("amount") float amount,
////            @RequestParam("start") int selectedStart,
////            @RequestParam("end") int selectedEnd
////    ) {
////        AppRangeDTO dto = distributionNewGetsService.getNextStartForSelectedBlock(empId, academicYearId, amount, selectedStart, selectedEnd);
////        if (dto == null) {
////            return ResponseEntity.notFound().build();
////        }
////        return ResponseEntity.ok(dto);
////    }
//}