package com.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.StudentAcademicDetails;
import com.application.entity.StudentPersonalDetails;

@Repository
public interface StudentPersonalDetailsRepository extends JpaRepository<StudentPersonalDetails, Integer> {

    Optional<StudentPersonalDetails> findByStudentAcademicDetails(StudentAcademicDetails academic);

    @Query("SELECT p FROM StudentPersonalDetails p WHERE p.studentAcademicDetails.stud_adms_id = :studAdmsId")
    Optional<StudentPersonalDetails> findByStudentAcademicDetailsStudAdmsId(@Param("studAdmsId") Integer studAdmsId);
    
    
   
    
}