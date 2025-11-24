package com.application.repository;

import java.util.List; 
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.StudentRelation;

@Repository
public interface StudentRelationRepository extends JpaRepository<StudentRelation, Integer>{
    
     /**
      * --- FIX: Use the Java field name 'studentRelationType' (camelCase) ---
      */
     @Query("SELECT sr FROM StudentRelation sr WHERE sr.studentRelationType = :type") // <-- Changed here
     Optional<StudentRelation> findByStudentRelationType(@Param("type") String studentRelationType);
    
    /**
     * This method is correct because it uses the Java field name 'isActive'
     */
    List<StudentRelation> findByIsActive(int isActive);

}