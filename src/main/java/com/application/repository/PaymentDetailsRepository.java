package com.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.PaymentDetails;
import com.application.entity.StudentAcademicDetails;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Integer>{
	
	Optional<PaymentDetails> findByStudentAcademicDetails(StudentAcademicDetails studentAcademicDetails);
}
