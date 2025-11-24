package com.application.repository;

import com.application.entity.UserAppRangeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserAppRangeViewRepository extends JpaRepository<UserAppRangeView, Integer> {
    
    @Query("SELECT v FROM UserAppRangeView v WHERE v.emp_id = :empId AND v.acdc_year_id = :acdcYearId")
    Optional<UserAppRangeView> findByEmployeeAndYear(
        @Param("empId") int empId, 
        @Param("acdcYearId") int acdcYearId
    );
}