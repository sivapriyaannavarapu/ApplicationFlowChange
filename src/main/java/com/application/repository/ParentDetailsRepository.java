package com.application.repository;

import java.util.List;
import java.util.Optional; // --- ADD ---
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.application.entity.ParentDetails;
import com.application.entity.StudentAcademicDetails;

@Repository
public interface ParentDetailsRepository extends JpaRepository<ParentDetails, Integer>{

     // This method finds ALL parents for a student
     List<ParentDetails> findByStudentAcademicDetails(StudentAcademicDetails academicDetails);

     /**
      * --- ADD THIS METHOD ---
      * Finds a specific parent record for a student based on the relation type ID.
      * Follows the path: studentAcademicDetails -> studentRelation -> studentRelationId
      */
     Optional<ParentDetails> findByStudentAcademicDetailsAndStudentRelationStudentRelationId(
         StudentAcademicDetails academicDetails,
         int studentRelationId
     );
}