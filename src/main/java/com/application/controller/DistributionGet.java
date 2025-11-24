package com.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.dto.AppDistributionDTO;
import com.application.dto.AppFromDTO;
import com.application.dto.AppNumberRangeDTO;
import com.application.dto.AppRangeDTO;
import com.application.dto.ApplicationRangeInfoDTO;
import com.application.dto.ApplicationStartEndDto;
import com.application.dto.EmployeeApplicationsDTO;
import com.application.dto.EmployeesDto;
import com.application.dto.GenericDropdownDTO;
import com.application.dto.NextAppNumberDTO;
import com.application.entity.AcademicYear;
import com.application.entity.Campus;
import com.application.entity.City;
import com.application.entity.District;
import com.application.entity.Employee;
import com.application.entity.State;
import com.application.entity.Zone;
import com.application.repository.EmployeeRepository;
import com.application.repository.SchoolDetailsRepository;
import com.application.service.CampusService;
import com.application.service.DgmService;
import com.application.service.ZoneService;

@RestController
@RequestMapping("/distribution/gets")
//@CrossOrigin(origins = "*")
public class DistributionGet {

    private final SchoolDetailsRepository schoolDetailsRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CampusService campusService;
	
	@Autowired
	private ZoneService distributionService;
	
	@Autowired
	private DgmService service;

    DistributionGet(SchoolDetailsRepository schoolDetailsRepository) {
        this.schoolDetailsRepository = schoolDetailsRepository;
    }
	
	@GetMapping("/academic-years")//used/c
	public ResponseEntity<List<AcademicYear>> getAcademicYears() {
		return ResponseEntity.ok(distributionService.getAllAcademicYears());
	}

	@GetMapping("/states")//used/c
	public ResponseEntity<List<State>> getStates() {
		return ResponseEntity.ok(distributionService.getAllStates());
	}

	@GetMapping("/city/{stateId}")//used/c
	public ResponseEntity<List<City>> getCitiesByState(@PathVariable int stateId) {
		return ResponseEntity.ok(distributionService.getCitiesByState(stateId));
	}

	@GetMapping("/zones/{cityId}")//used/c
	public ResponseEntity<List<Zone>> getZonesByCity(@PathVariable int cityId) {
		return ResponseEntity.ok(distributionService.getZonesByCity(cityId));
	}

	@GetMapping("/employees")//used
	public ResponseEntity<List<Employee>> getEmployees() {
		return ResponseEntity.ok(distributionService.getIssuableToEmployees());
	}
	
	@GetMapping("/next-app-number")//used
	public ResponseEntity<String> getNextAppNumber(@RequestParam int academicYearId, @RequestParam int stateId,
			@RequestParam int userId) {

		String nextAppNumber = distributionService.getNextApplicationNumber(academicYearId, stateId, userId);
		return ResponseEntity.ok(nextAppNumber);
	}
	
	@GetMapping("/app-number-from-to")
    public ResponseEntity<ApplicationStartEndDto> getAppNumberRanges(
        @RequestParam int academicYearId,
        @RequestParam int stateId,
        @RequestParam int createdBy) {
        
        try {
            ApplicationStartEndDto ranges = distributionService.getAppNumberRanges(academicYearId, stateId);
            return ResponseEntity.ok(ranges);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
	
	@GetMapping("/app-end-number")//used
	public ResponseEntity<Integer> getAppEndNumber(@RequestParam int stateId, @RequestParam int userId) {
	    Integer endNo = distributionService.getAppEndNoForUser(stateId, userId);
	    
	    if (endNo != null) {
	        // If the number is found, return it
	        return ResponseEntity.ok(endNo);
	    } else {
	        // If no record is found, return a 404 Not Found error
	        return ResponseEntity.notFound().build();
	    }
	}
	
	 @GetMapping("/{empId}/mobile")
	    public String getMobileByEmpId(@PathVariable int empId) {
	        return employeeRepository.findMobileNoByEmpId(empId);
	    }
	   // GET /api/zonal-accountants/zone/1/employees
	    @GetMapping("/zone/{zoneId}/employees")//used/
	    public List<EmployeesDto> getEmployeesByZone(@PathVariable int zoneId) {
	        return distributionService.getEmployeesByZone(zoneId);
	    }
	    
	 
	    
	    @Autowired
	    private DgmService applicationService;
	 
	    @GetMapping("/cities")//used/c
	    public List<GenericDropdownDTO> getCities() {
	        return applicationService.getAllCities();
	    }
	 
	    @GetMapping("/campus/{zoneId}")//used/c
	    public List<GenericDropdownDTO> getCampusesByZone(@PathVariable int zoneId) {
	        return applicationService.getCampusesByZoneId(zoneId);
	    }
	    
	    @GetMapping("/campusesforzonal_accountant/{empId}")//used/c
	    public List<GenericDropdownDTO> getCampusesByEmployee(@PathVariable int empId) {
	        return applicationService.getCampusesByEmployeeId(empId);
	    }
	    
	    @GetMapping("/campusesfordgm/{empId}")//used
	    public List<GenericDropdownDTO> getActiveCampusesByEmpId(@PathVariable Integer empId) {
	        return applicationService.getActiveCampusesByEmpId(empId);
	    }
	    
	    @GetMapping("/dgmforzonal_accountant/{empId}")//used
	    public List<GenericDropdownDTO> getActiveCampusesByEmployee(@PathVariable int empId) {
	        return applicationService.getActiveCampusesByEmployeeId(empId);
	    }
	    
	    @GetMapping("/issued-to")//used
	    public List<GenericDropdownDTO> getIssuedToTypes() {
	        return applicationService.getAllIssuedToTypes();
	    }
	 
	    @GetMapping("/app-number-ranges")
	    public List<AppNumberRangeDTO> getAppNumberRanges(@RequestParam("academicYearId") int academicYearId,
	                                                      @RequestParam("employeeId") int employeeId) {
	        return applicationService.getAvailableAppNumberRanges(academicYearId, employeeId);
	    }
	    
	    @GetMapping("/mobile-no/{empId}")//used/n
	    public ResponseEntity<String> getMobileNo(@PathVariable int empId) {
	        String mobileNumber = applicationService.getMobileNumberByEmpId(empId);
	        if (mobileNumber != null) {
	            return ResponseEntity.ok(mobileNumber);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    @Autowired
	    private CampusService dgmService;
	   
	  
	    @GetMapping("/districts/{stateId}")//used
	    public List<GenericDropdownDTO> getDistrictsByState(@PathVariable int stateId) {
	        return dgmService.getDistrictsByStateId(stateId);
	    }
	 
	    @GetMapping("/cities/{districtId}")//used/
	    public List<GenericDropdownDTO> getCitiesByDistrict(@PathVariable int districtId) {
	        return dgmService.getCitiesByDistrictId(districtId);
	    }
	    
	    @GetMapping("/campuses/{cityId}")//used/
	    public List<GenericDropdownDTO> getCampusesByCity(@PathVariable int cityId) {
	        return dgmService.getCampusesByCityId(cityId);
	    }
	    
	    @GetMapping("/campaign-areas")//used
	    public List<GenericDropdownDTO> getAllCampaignAreas() {
	        return dgmService.getAllCampaignAreas();
	    }
	    
//	    @GetMapping("/pro/{campusId}")
//	    public List<GenericDropdownDTO> getProsByCampus(@PathVariable int campusId) {
//	        return dgmService.getProsByCampusId(campusId);
//	    }
//	   
	    @GetMapping("/pros/{campusId}")//used/c
	    public ResponseEntity<List<GenericDropdownDTO>> getEmployeeDropdown(
	            @PathVariable int campusId) {

	        // 1. Call the service layer method to execute the JPQL query
	        List<GenericDropdownDTO> employees = dgmService.getEmployeeDropdownByCampus(campusId);

	        // 2. Return the list with an HTTP 200 OK status
	        if (employees.isEmpty()) {
	            // Optional: Return 204 No Content or an empty list if no results
	            return ResponseEntity.ok(employees);
	        }
	        return ResponseEntity.ok(employees);
	    }
	    
	    @GetMapping("/getalldistricts")//used/
	    public List<District> getAllDistricts()
	    {
	    	return campusService.getAllDistricts();
	    }
	    
	    @GetMapping("/zones")
		public List<Zone> fetchAll(){
			return applicationService.findAllZones();
		}
	    
	    @GetMapping("/Campus")
		public List<Campus> fetchAllCampus(){
			return applicationService.fetchAllCampuses();
		}
	    
	    @GetMapping("/{campaignId}/campus")
	    public ResponseEntity<List<GenericDropdownDTO>> getCampusByCampaign(@PathVariable int campaignId) {
	        List<GenericDropdownDTO> campus = campusService.getCampusByCampaignId(campaignId);
	        return ResponseEntity.ok(campus);
	    }
	    
	    @GetMapping("/getarea/{cityId}")
	    public ResponseEntity<List<GenericDropdownDTO>> getCampaignsByCity(@PathVariable int cityId) {
	        List<GenericDropdownDTO> campaigns = campusService.getCampaignsByCityId(cityId);
	        return ResponseEntity.ok(campaigns);
	    }
	    
	    @GetMapping("/employee/{employeeId}/applications/{academicYearId}")
	    public EmployeeApplicationsDTO getEmployeeApplications(
	            @PathVariable int employeeId,
	            @PathVariable int academicYearId) {
	        return service.getEmployeeAvailableApplications(academicYearId, employeeId);
	    }
	    
	    @GetMapping("/available-start-end")
	    public ResponseEntity<?> getAppRange(
	            @RequestParam("empId") int issuedToEmpId,
	            @RequestParam("yearId") int academicYearId) {

	        Optional<AppDistributionDTO> range = service.getActiveAppRange(issuedToEmpId, academicYearId);

	        if (range.isPresent()) {
	            return ResponseEntity.ok(range.get());
	        } else {
	            // Return 404 Not Found if no active range is found for the parameters
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
//	    @GetMapping("/app-from")
//	    public ResponseEntity<?> getAppFrom(
//	            @RequestParam("empId") int employeeId,
//	            @RequestParam("yearId") int academicYearId) {
//
//	        Optional<AppFromDTO> appFrom = service.getAppFromByEmployeeAndYear(employeeId, academicYearId);
//
//	        if (appFrom.isPresent()) {
//	            return ResponseEntity.ok(appFrom.get());
//	        } else {
//	            // Return 404 Not Found if no record matches the parameters
//	            return ResponseEntity.notFound().build();
//	        }
//	    }
	    
//	    @GetMapping("/range")//used/n
//	    public ResponseEntity<AppRangeDTO> getAppRanges(
//	            @RequestParam int empId,
//	            @RequestParam int academicYearId) {
//
//	        AppRangeDTO appRange = applicationService.getAppRange(empId, academicYearId);
//
//	        if (appRange == null) {
//	            return ResponseEntity.notFound().build();
//	        }
//
//	        return ResponseEntity.ok(appRange);
//	    }
	    
//	    
//	    @GetMapping("/range/withcityid")
//	    public ResponseEntity<AppRangeDTO> getAppRanges(
//	            @RequestParam int empId,
//	            @RequestParam int academicYearId,
//	            @RequestParam(required = false) Integer cityId) { // Added cityId
//
//	        AppRangeDTO appRange = applicationService.getAppRange(empId, academicYearId, cityId); // Pass cityId
//
//	        if (appRange == null) {
//	            return ResponseEntity.notFound().build();
//	        }
//
//	        return ResponseEntity.ok(appRange);
//	    }
	    @GetMapping("/next-app-number/top-issuer")
	    public ResponseEntity<NextAppNumberDTO> getNextApplicationNumber(
	            @RequestParam("academicYearId") int academicYearId,
	            @RequestParam("stateId") int stateId) {
	        
	        NextAppNumberDTO result = distributionService.getNextApplicationNumberForTopIssuer(
	            academicYearId, 
	            stateId
	        );
	        
	        return ResponseEntity.ok(result);
	    }
	    
	    @GetMapping("/get-range")
	    public ResponseEntity<ApplicationRangeInfoDTO> getApplicationNumberInfo(
	            @RequestParam("academicYearId") int academicYearId,
	            @RequestParam("stateId") int stateId) {
	        
	        ApplicationRangeInfoDTO result = distributionService.getApplicationNumberInfo(
	            academicYearId, 
	            stateId
	        );
	        
	        return ResponseEntity.ok(result);
	    }
	    
	    @GetMapping("/range-info")//used/n
	    public ResponseEntity<ApplicationRangeInfoDTO> getApplicationRangeInformation(
	            @RequestParam int academicYearId,
	            @RequestParam(required = false) Integer stateId, // StateId is now optional
	            @RequestParam(required = false) Integer cityId) { // CityId is now optional
	        
	        // Pass both to the service method
	        ApplicationRangeInfoDTO rangeInfo = distributionService.getApplicationNumberInfo(academicYearId, stateId, cityId);
	        return ResponseEntity.ok(rangeInfo);
	    }
	    
	    @GetMapping("/dgm/{campusId}")//used/c
	    public ResponseEntity<List<GenericDropdownDTO>> getActiveDgmEmployeesByCampus(
	            @PathVariable int campusId) { // Use @PathVariable

	        // Call the service method to fetch the data
	        List<GenericDropdownDTO> dgmEmployees = applicationService.getDgmEmployeesForCampus(campusId);

	        if (dgmEmployees.isEmpty()) {
	            // Return 404 Not Found if no employees are found
	            return ResponseEntity.notFound().build();
	        }

	        // Return the list with HTTP 200 OK
	        return ResponseEntity.ok(dgmEmployees);
	    }
}
