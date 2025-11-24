package com.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.dto.AppRangeDTO;
import com.application.entity.AdminApp;

@Repository
public interface AdminAppRepository extends JpaRepository<AdminApp, Integer>{
	
	
	@Query("SELECT DISTINCT a.app_amount FROM AdminApp a WHERE a.employee.id = :empId AND a.academicYear.id = :academicYearId AND a.is_active = 1")
	List<Integer> findAmountsByEmpIdAndAcademicYear(
	    @Param("empId") int empId,
	    @Param("academicYearId") int academicYearId // NEW PARAMETER
	);
	
	// New method for fallback logic
//	// In AdminAppRepository.java (Fallback Query)
	@Query("SELECT new com.application.dto.AppRangeDTO(" +
		       "a.app_from_no, " +      // appStartNo
		       "a.app_to_no, " +        // appEndNo
		       "a.app_from_no, " +      // appFrom
		       "NULL, " +               // appBalanceTrkId (NULL for creation signal)
		       "a.total_app) " +        // NEW: appCount (from AdminApp total_app)
		       "FROM AdminApp a " +
		       "WHERE a.employee.id = :empId " + 
		       "AND a.academicYear.id = :academicYearId " +
		       "AND (a.app_amount = :amount OR a.app_fee = :amount) " +
		       "AND a.is_active = 1")
		Optional<AppRangeDTO> findDefaultAppRangeDto(
		    @Param("empId") int empId,
		    @Param("academicYearId") int academicYearId,
		    @Param("amount") float amount
		);
	
	 @Query("""
		        SELECT a
		        FROM AdminApp a
		        WHERE a.employee.id = :empId
		          AND a.academicYear.id = :academicYearId
		          AND (a.app_amount = :amount OR a.app_fee = :amount)
		          AND a.is_active = 1
		        """)
		    Optional<AdminApp> findDefaultAppRange(
		        @Param("empId") int empId,
		        @Param("academicYearId") int academicYearId,
		        @Param("amount") float amount
		    );
	 
	 @Query("""
		        SELECT a FROM AdminApp a 
		        WHERE :applicationNo BETWEEN a.app_from_no AND a.app_to_no 
		          AND a.academicYear.acdcYearId = :academicYearId 
		          AND a.is_active = 1
		        """)
		    Optional<AdminApp> findActiveAdminAppByAppNoAndAcademicYear(
		            @Param("applicationNo") long applicationNo,
		            @Param("academicYearId") int academicYearId);
	

}
