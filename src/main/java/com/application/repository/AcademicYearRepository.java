package com.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.AcademicYear;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer>{
	
	 Optional<AcademicYear> findByAcademicYear(String academicYear);
	 List<AcademicYear> findByAcdcYearIdIn(List<Integer> acdcYearIds);
	 List<AcademicYear> findByYearIn(List<Integer> years);
	 
	 Optional<AcademicYear> findByYear(Integer year);
	 
	 
	 
}