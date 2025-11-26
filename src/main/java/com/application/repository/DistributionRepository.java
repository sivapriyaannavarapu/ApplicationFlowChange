package com.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.dto.AppDistributionDTO;
import com.application.dto.ApplicationStartEndDto;
import com.application.entity.Distribution;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Integer> {

	@Query(value = "SELECT MAX(d.app_end_no) FROM sce_application.sce_app_distrubution d WHERE d.state_id = :stateId AND d.created_by = :userId AND d.acdc_year_id = :academicYearId AND d.is_active = 1", nativeQuery = true)
	Integer findMaxAppEndNo(@Param("stateId") int stateId, @Param("userId") int userId,
			@Param("academicYearId") int academicYearId);

	@Query(value = "SELECT * FROM sce_application.sce_app_distrubution d WHERE d.created_by = :empId AND d.is_active = 1", nativeQuery = true)
	List<Distribution> findByCreatedBy(@Param("empId") int empId);

	@Query(value = "SELECT * FROM sce_application.sce_app_distrubution d WHERE d.created_by=:empId AND d.issued_to_type_id=:issuedToTypeId AND d.is_active=1",nativeQuery=true)
	List<Distribution> findByCreatedByAndIssuedToType(@Param("empId") int empId,
			@Param("issuedToTypeId") int issuedToTypeId);

	@Query("SELECT MAX(d.appEndNo) FROM Distribution d WHERE d.issued_to_emp_id = :empId AND d.academicYear.acdcYearId = :acdYearId AND d.isActive = 1")
	Optional<Integer> findMaxAppEndNoByIssuedToEmpIdAndAcademicYearId(@Param("empId") int empId,
			@Param("acdYearId") int acdYearId);

	@Query("SELECT MIN(d.appStartNo) FROM Distribution d WHERE d.issued_to_emp_id = :empId AND d.academicYear.acdcYearId = :acdYearId AND d.isActive = 1")
	Optional<Integer> findMinAppStartNoByIssuedToEmpIdAndAcademicYearId(@Param("empId") int empId,
			@Param("acdYearId") int acdYearId);

	@Query("SELECT SUM(d.totalAppCount) FROM Distribution d WHERE d.issued_to_emp_id = :employeeId AND d.academicYear.acdcYearId = :academicYearId AND d.isActive = 1")
	Optional<Integer> sumTotalAppCountByIssuedToEmpId(@Param("employeeId") int employeeId,
			@Param("academicYearId") int academicYearId);

	@Query("SELECT SUM(d.totalAppCount) FROM Distribution d WHERE d.created_by = :employeeId AND d.academicYear.acdcYearId = :academicYearId AND d.isActive = 1")
	Optional<Integer> sumTotalAppCountByCreatedBy(@Param("employeeId") int employeeId,
			@Param("academicYearId") int academicYearId);

	@Query("SELECT d FROM Distribution d WHERE d.academicYear.acdcYearId = :academicYearId AND d.appStartNo <= :endNo AND d.appEndNo >= :startNo AND d.isActive = 1")
	List<Distribution> findOverlappingDistributions(@Param("academicYearId") int academicYearId,
			@Param("startNo") int startNo, @Param("endNo") int endNo);

	@Query("SELECT d FROM Distribution d WHERE :admissionNo >= d.appStartNo AND :admissionNo <= d.appEndNo AND d.issuedToType.appIssuedId = 4 AND d.isActive = 1")
	Optional<Distribution> findProDistributionForAdmissionNumber(@Param("admissionNo") long admissionNo);

	@Query("SELECT MAX(d.appEndNo) FROM Distribution d WHERE d.created_by = :employeeId AND d.academicYear.acdcYearId = :academicYearId AND d.isActive = 1")
	Optional<Integer> findMaxAppEndNoByCreatedByAndAcademicYearId(@Param("employeeId") int employeeId,
			@Param("academicYearId") int academicYearId);

	@Query("SELECT new com.application.dto.AppDistributionDTO(d.appStartNo, d.appEndNo) FROM Distribution d WHERE d.issued_to_emp_id = :issuedToEmpId AND d.academicYear.acdcYearId = :academicYearId AND d.isActive = 1")
	Optional<AppDistributionDTO> findActiveAppRangeByEmployeeAndAcademicYear(@Param("issuedToEmpId") int issuedToEmpId,
			@Param("academicYearId") int academicYearId);

	@Query("SELECT new com.application.dto.AppDistributionDTO(d.appStartNo, d.appEndNo) FROM Distribution d WHERE d.issued_to_emp_id = :issuedToEmpId AND d.academicYear.acdcYearId = :academicYearId AND d.isActive = 1 AND (:cityId IS NULL OR d.city.id = :cityId)")
	Optional<AppDistributionDTO> findActiveAppRangeByEmployeeAndAcademicYear(@Param("issuedToEmpId") int issuedToEmpId,
			@Param("academicYearId") int academicYearId, @Param("cityId") Integer cityId);

	@Query("SELECT MAX(d.appEndNo) + 1 FROM Distribution d WHERE d.academicYear.acdcYearId = :academicYearId AND d.state.stateId = :stateId AND d.issuedByType.appIssuedId = 1 AND d.isActive = 1")
	Optional<Integer> findNextStartNumberByStateAndIssuerType(@Param("academicYearId") int academicYearId,
			@Param("stateId") int stateId);

	@Query("SELECT MAX(d.appEndNo) + 1 FROM Distribution d WHERE d.academicYear.acdcYearId = :academicYearId AND d.state.stateId = :stateId AND d.issuedByType.appIssuedId IN (1, 2, 3) AND d.isActive = 1")
	Optional<Integer> findMaxAppEndNoByAcademicYearAndState(@Param("academicYearId") int academicYearId,
			@Param("stateId") int stateId);

	@Query("SELECT d FROM Distribution d WHERE :admissionNo BETWEEN d.appStartNo AND d.appEndNo AND d.issuedToType.appIssuedId = :issuedToTypeId AND d.isActive = 1")
	List<Distribution> findByAdmissionNoRangeAndIssuedToType(@Param("admissionNo") int admissionNo,
			@Param("issuedToTypeId") int issuedToTypeId);

	@Query("SELECT d FROM Distribution d WHERE :applicationNo BETWEEN d.appStartNo AND d.appEndNo AND d.isActive = 1")
	Optional<Distribution> findActiveByApplicationNoInRange(@Param("applicationNo") int applicationNo);

	// In DistributionRepository.java
	// Needs @Param("amount") float amount added to the method signature
	@Query("SELECT new com.application.dto.AppDistributionDTO(d.appStartNo, d.appEndNo) " + "FROM Distribution d "
			+ "WHERE d.issuedToEmployee.id = :empId " + "AND d.academicYear.id = :academicYearId "
			+ "AND d.amount = :amount " + // NEW FILTER: amount
			"AND d.isActive = 1")
	Optional<AppDistributionDTO> findActiveAppRange(@Param("empId") int empId,
			@Param("academicYearId") int academicYearId, @Param("amount") float amount // NEW PARAMETER
	);

	@Query("SELECT new com.application.dto.AppDistributionDTO(d.appStartNo, d.appEndNo) " + "FROM Distribution d "
			+ "WHERE d.issuedToEmployee.id = :empId " + "  AND d.academicYear.id = :academicYearId "
			+ "  AND d.amount = :amount " + "  AND d.isActive = 1 " + "ORDER BY d.appStartNo")
	List<AppDistributionDTO> findByIssuedToEmployeeAndYearAndAmount(@Param("empId") int empId,
			@Param("academicYearId") int academicYearId, @Param("amount") float amount);

	// For admin: distributions created by admin (if needed)
	@Query("SELECT new com.application.dto.AppDistributionDTO(d.appStartNo, d.appEndNo) " + "FROM Distribution d "
			+ "WHERE d.createdByEmployee.emp_id = :adminEmpId " + "  AND d.academicYear.id = :academicYearId "
			+ "  AND d.amount = :amount " + "  AND d.isActive = 1 " + "ORDER BY d.appStartNo")
	List<AppDistributionDTO> findByCreatedByAndYearAndAmount(@Param("adminEmpId") int adminEmpId,
			@Param("academicYearId") int academicYearId, @Param("amount") float amount);
	
	@Query("SELECT SUM(d.totalAppCount) FROM Distribution d WHERE d.created_by = :empId AND d.academicYear.acdcYearId = :yearId AND d.amount = :amount AND d.isActive = 1")
	Optional<Integer> sumTotalAppCountByCreatedByAndAmount(@Param("empId") int empId, @Param("yearId") int yearId, @Param("amount") Float amount);
	
	@Query("SELECT d FROM Distribution d WHERE d.issued_to_emp_id = :empId AND d.academicYear.acdcYearId = :yearId AND d.amount = :amount AND d.isActive = 1 ORDER BY d.appStartNo ASC")
	List<Distribution> findActiveByIssuedToEmpIdAndAmountOrderByStart(
	    @Param("empId") int empId, 
	    @Param("yearId") int yearId, 
	    @Param("amount") Float amount
	);
	
	@Query("SELECT d.appDistributionId FROM Distribution d WHERE " +
	           "d.issued_to_emp_id = :empId " +
	           "AND d.appStartNo = :start " +
	           "AND d.appEndNo = :end " +
	           "AND d.amount = :amount " +
	           "AND d.isActive = 1")
	    Optional<Integer> findIdByEmpAndRange(
	            @Param("empId") int empId, 
	            @Param("start") int start, 
	            @Param("end") int end,
	            @Param("amount") Double amount
	    );

	    // 2. GET DISTRIBUTION ID FOR PRO
	    // Finds the active transaction ID for a specific PRO and Range
	    @Query("SELECT d.appDistributionId FROM Distribution d WHERE " +
	           "d.issued_to_pro_id = :proId " +
	           "AND d.appStartNo = :start " +
	           "AND d.appEndNo = :end " +
	           "AND d.amount = :amount " +
	           "AND d.isActive = 1")
	    Optional<Integer> findIdByProAndRange(
	            @Param("proId") int proId, 
	            @Param("start") int start, 
	            @Param("end") int end,
	            @Param("amount") Double amount
	    );
	    
	    @Query("SELECT SUM(d.totalAppCount) FROM Distribution d WHERE " +
	            "d.issued_to_pro_id = :proId " +
	            "AND d.academicYear.acdcYearId = :yearId " +
	            // "AND d.amount = :amount " +  <--- COMMENTED OUT
	            "AND d.isActive = 1")
	     Optional<Integer> sumTotalAppCountByIssuedToProIdAndAmount(
	             @Param("proId") int proId, 
	             @Param("yearId") int yearId, 
	             @Param("amount") Float amount // Keep param to avoid changing Service code, but don't use it
	     );
	    
	    @Query("SELECT MIN(d.appStartNo) FROM Distribution d WHERE d.issued_to_pro_id = :proId AND d.academicYear.acdcYearId = :yearId AND d.isActive = 1")
		Optional<Integer> findMinAppStartNoByIssuedToProId(@Param("proId") int proId, @Param("yearId") int yearId);

		// 3. Find Max End Number for PRO
		@Query("SELECT MAX(d.appEndNo) FROM Distribution d WHERE d.issued_to_pro_id = :proId AND d.academicYear.acdcYearId = :yearId AND d.isActive = 1")
		Optional<Integer> findMaxAppEndNoByIssuedToProId(@Param("proId") int proId, @Param("yearId") int yearId);

		@Query("SELECT d FROM Distribution d WHERE d.issued_to_emp_id = :empId AND d.academicYear.acdcYearId = :yearId AND d.isActive = 1 ORDER BY d.appStartNo ASC")
	    List<Distribution> findActiveHoldingsForEmp(@Param("empId") Integer empId, @Param("yearId") Integer yearId);
		

}
