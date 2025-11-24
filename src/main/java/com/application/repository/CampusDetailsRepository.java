package com.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.CampusDetails;

@Repository
public interface CampusDetailsRepository extends JpaRepository<CampusDetails, Integer>{
	
	 @Query("SELECT c FROM CampusDetails c " +
	           "WHERE c.campus.campusId = :campusId " +
	           "AND c.academicYear.acdcYearId = :academicYearId")
	    Optional<CampusDetails> findByCampusAndAcademicYear(
	        @Param("campusId") int campusId,
	        @Param("academicYearId") int academicYearId
	    );
	 
	 Optional<CampusDetails> findByCampusCampusIdAndAcademicYearAcdcYearId(int campusId, int academicYearId);
	 

}
