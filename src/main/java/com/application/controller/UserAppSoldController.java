package com.application.controller;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.dto.DashboardResponseDTO;
import com.application.dto.GraphResponseDTO;
import com.application.dto.PerformanceDTO;
import com.application.dto.RateResponseDTO;
import com.application.dto.UserAppSoldDTO;
import com.application.service.UserAppSoldService;
 
@RestController
@RequestMapping("/api/performance")
public class UserAppSoldController {
 
	@Autowired
	private UserAppSoldService userAppSoldService;
 
	// --- OVERALL ZONE ENDPOINTS ---
 
	@GetMapping("/zones/top-rated")
	public List<PerformanceDTO> getTopRatedZones() {
		return userAppSoldService.getTopRatedZones();
	}
 
	@GetMapping("/zones/drop-rated")
	public List<PerformanceDTO> getDropRatedZones() {
		return userAppSoldService.getDropRatedZones();
	}
 
	// --- OVERALL CAMPUS ENDPOINTS ---
 
	@GetMapping("/employee/{employeeId}/campus/top-rated")
	public List<PerformanceDTO> getTopRatedCampuses(@PathVariable Integer employeeId) {
		return userAppSoldService.getTopRatedCampusesForEmployee(employeeId);
	}
 
	 @GetMapping("/employee/{employeeId}/campus/drop-rated")
	    public List<PerformanceDTO> getDropRatedCampusesForEmployee(@PathVariable Integer employeeId) {
	        return userAppSoldService.getDropRatedCampusesForEmployee(employeeId);
	    }
	
	// --- NEW: DGM RANKINGS FILTERED BY EMPLOYEE ---
 
    @GetMapping("/employee/{employeeId}/dgms/top-rated")
    public List<PerformanceDTO> getTopRatedDgmsForEmployee(@PathVariable Integer employeeId) {
        return userAppSoldService.getTopRatedDgmsForEmployee(employeeId);
    }
 
    @GetMapping("/employee/{employeeId}/dgms/drop-rated")
    public List<PerformanceDTO> getDropRatedDgmsForEmployee(@PathVariable Integer employeeId) {
        return userAppSoldService.getDropRatedDgmsForEmployee(employeeId);
    }
 
	// --- DETAILED ANALYTICS ENDPOINT ---
 
	@GetMapping("/entity/{entityId}")
	public List<UserAppSoldDTO> getAnalyticsByEntityId(@PathVariable Integer entityId) {
		return userAppSoldService.getAnalyticsByEntityId(entityId);
	}
	
//	 @GetMapping("/graph")
//	 public List<FullGraphResponseDTO> getAllGraphs() {
//		    return userAppSoldService.getAllGraphs();
//		}
	 
	 @GetMapping("/top_drop_rate")//used
	    public ResponseEntity<List<RateResponseDTO>> getAllRateData() {
	        return ResponseEntity.ok(userAppSoldService.getAllRateData());
	    }
	 
	 @GetMapping("overall_graph")
	    public GraphResponseDTO getYearWiseIssuedSoldPercentages() {
	        return userAppSoldService.generateYearWiseIssuedSoldPercentage();
	    }
	 
}
