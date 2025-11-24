package com.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.dto.AppFromDTO;
import com.application.entity.AcademicYear;
import com.application.entity.BalanceTrack;

@Repository
public interface BalanceTrackRepository extends JpaRepository<BalanceTrack, Integer> {

    @Query("SELECT b FROM BalanceTrack b WHERE b.academicYear.acdcYearId = :academicYearId AND b.employee.id = :employeeId AND b.isActive = 1")
    Optional<BalanceTrack> findBalanceTrack(@Param("academicYearId") int academicYearId,
                                            @Param("employeeId") int employeeId);

    @Query("SELECT b FROM BalanceTrack b WHERE b.academicYear.acdcYearId = :academicYearId AND b.employee.id = :employeeId AND b.isActive = 1")
    List<BalanceTrack> findAppNumberRanges(@Param("academicYearId") int academicYearId,
                                           @Param("employeeId") int employeeId);

    @Query("SELECT b FROM BalanceTrack b WHERE b.createdBy = :createdBy AND b.academicYear = :academicYear AND b.isActive = 1")
    Optional<BalanceTrack> findByCreatedByAndAcademicYear(@Param("createdBy") int createdBy,
                                                          @Param("academicYear") AcademicYear academicYear);

//    @Query("SELECT new com.application.dto.AppFromDTO(b.appFrom, b.appBalanceTrkId) FROM BalanceTrack b WHERE b.employee.id = :employeeId AND b.academicYear.id = :academicYearId AND b.isActive = 1")
//    Optional<AppFromDTO> getAppFromByEmployeeAndAcademicYear(@Param("employeeId") int employeeId,
//                                                             @Param("academicYearId") int academicYearId);

    @Query("SELECT bt FROM BalanceTrack bt WHERE :appNo BETWEEN bt.appFrom AND bt.appTo AND bt.isActive = 1 AND bt.issuedByType.appIssuedId = 4")
    Optional<BalanceTrack> findActiveBalanceTrackByAppNoRange(@Param("appNo") Long appNo);
    
    @Query("SELECT DISTINCT b.amount FROM BalanceTrack b WHERE b.employee.id = :empId AND b.academicYear.id = :academicYearId AND b.isActive = 1")
    List<Float> findAmountsByEmpIdAndAcademicYear(
        @Param("empId") int empId,
        @Param("academicYearId") int academicYearId // NEW PARAMETER
    );
    
 // In BalanceTrackRepository.java
 // New method name reflecting all filters
    @Query("SELECT new com.application.dto.AppFromDTO(b.appFrom, b.appBalanceTrkId, b.appAvblCnt) " + // UPDATED PROJECTION
    	       "FROM BalanceTrack b " +
    	       "WHERE b.employee.id = :empId " + 
    	       "AND b.academicYear.id = :academicYearId " +
    	       "AND b.amount = :amount " + 
    	       "AND b.isActive = 1")
    	Optional<AppFromDTO> getAppFromByEmployeeAndAcademicYearAndAmount(
    	    @Param("empId") int empId,
    	    @Param("academicYearId") int academicYearId,
    	    @Param("amount") float amount 
    	);
    
    @Query("SELECT new com.application.dto.AppFromDTO(b.appFrom, b.appBalanceTrkId, b.appAvblCnt) " +
            "FROM BalanceTrack b " +
            "WHERE b.employee.id = :empId " +
            "  AND b.academicYear.id = :academicYearId " +
            "  AND b.amount = :amount " +
            "  AND b.isActive = 1 " +
            "ORDER BY b.appBalanceTrkId DESC")
     List<AppFromDTO> findLatestByEmployeeYearAndAmount(
         @Param("empId") int empId,
         @Param("academicYearId") int academicYearId,
         @Param("amount") float amount
     );

     // For created_by matching (admin checking created distribution)
     @Query("SELECT new com.application.dto.AppFromDTO(b.appFrom, b.appBalanceTrkId, b.appAvblCnt) " +
            "FROM BalanceTrack b " +
            "WHERE b.createdBy = :createdBy " +
            "  AND b.academicYear.id = :academicYearId " +
            "  AND b.amount = :amount " +
            "  AND b.isActive = 1 " +
            "ORDER BY b.appBalanceTrkId DESC")
     List<AppFromDTO> findLatestByCreatedByAndYearAndAmount(
         @Param("createdBy") int createdBy,
         @Param("academicYearId") int academicYearId,
         @Param("amount") float amount
     );
     
     @Query("""
    		    SELECT new com.application.dto.AppFromDTO(b.appFrom, b.appBalanceTrkId, b.appAvblCnt)
    		    FROM BalanceTrack b
    		    WHERE b.employee.emp_id = :empId
    		      AND b.academicYear.acdcYearId = :yearId
    		      AND b.amount = :amount
    		      AND b.isActive = 1
    		    ORDER BY b.appBalanceTrkId DESC
    		""")
    		List<AppFromDTO> findBTForEmployee(int empId, int yearId, float amount);

     @Query("""
    		    SELECT new com.application.dto.AppFromDTO(b.appFrom, b.appBalanceTrkId, b.appAvblCnt)
    		    FROM BalanceTrack b
    		    WHERE b.createdBy = :empId
    		      AND b.academicYear.acdcYearId = :yearId
    		      AND b.amount = :amount
    		      AND b.isActive = 1
    		    ORDER BY b.appBalanceTrkId DESC
    		""")
    		List<AppFromDTO> findBTForAdmin(int empId, int yearId, float amount);

     @Query("""
    	        SELECT new com.application.dto.AppFromDTO(
    	            b.appFrom,
    	            b.appBalanceTrkId,
    	            b.appAvblCnt
    	        )
    	        FROM BalanceTrack b
    	        WHERE b.createdBy = :createdBy
    	          AND b.academicYear.acdcYearId = :yearId
    	          AND b.amount = :amount
    	          AND b.isActive = 1
    	        ORDER BY b.appBalanceTrkId DESC
    	    """)
    	    List<AppFromDTO> findByCreatedByYearAndAmount(
    	            @Param("createdBy") int createdBy,
    	            @Param("yearId") int yearId,
    	            @Param("amount") float amount
    	    );
     
     @Query("""
    		    SELECT new com.application.dto.AppFromDTO(
    		        b.appFrom,
    		        b.appBalanceTrkId,
    		        b.appAvblCnt
    		    )
    		    FROM BalanceTrack b
    		    WHERE b.createdBy = :creatorId
    		      AND b.academicYear.acdcYearId = :yearId
    		      AND b.amount = :amount
    		      AND b.isActive = 1
    		      AND b.appFrom BETWEEN :blockStart AND :blockEnd
    		    ORDER BY b.appBalanceTrkId DESC
    		""")
    		List<AppFromDTO> findLatestForBlock(
    		        @Param("creatorId") int creatorId,
    		        @Param("yearId") int yearId,
    		        @Param("amount") float amount,
    		        @Param("blockStart") int blockStart,
    		        @Param("blockEnd") int blockEnd
    		);

}
