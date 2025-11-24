package com.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.ApplicationCoupon;
import com.application.entity.Employee;
import com.application.entity.EmployeeCoupon;

@Repository
public interface EmployeeCouponRepository extends JpaRepository<EmployeeCoupon, Integer>{
	
	@Query("SELECT ec FROM EmployeeCoupon ec WHERE ec.employee = :employee AND ec.applicationCoupon = :applicationCoupon AND ec.is_used = :isUsed")
    Optional<EmployeeCoupon> findByEmployeeAndApplicationCouponAndIsUsed
    (@Param("employee") Employee employee, @Param("applicationCoupon") 
    ApplicationCoupon applicationCoupon, @Param("isUsed") int isUsed);
}
