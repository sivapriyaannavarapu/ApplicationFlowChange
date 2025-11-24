package com.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.StudentConcessionType;

@Repository
public interface StudentConcessionTypeRepository extends JpaRepository<StudentConcessionType, Integer>{

	@Query("SELECT s FROM StudentConcessionType s WHERE s.studAdmsId = :studAdmsId AND s.concessionType.concTypeId = :concTypeId")
	Optional<StudentConcessionType> findByStudAdmsIdAndConcessionTypeId(@Param("studAdmsId") int studAdmsId, @Param("concTypeId") int concessionTypeId);
	
	Optional<StudentConcessionType> findByStudAdmsIdAndAcademicYear_AcdcYearId(Integer studAdmsId, Integer acadYearId);

	@Query("SELECT s FROM StudentConcessionType s WHERE s.studAdmsId = :studAdmsId")
	List<StudentConcessionType> findByStudAdmsId(@Param("studAdmsId") Integer studAdmsId);
	
	List<StudentConcessionType> findByStudAdmsId(int studAdmsId);
	
	Optional<StudentConcessionType> findByStudAdmsIdAndConcessionType_ConcTypeId(Integer studAdmsId, Integer concTypeId);
	

}
