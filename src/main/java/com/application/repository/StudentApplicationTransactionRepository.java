package com.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.Distribution;
import com.application.entity.PaymentDetails;
import com.application.entity.StudentApplicationTransaction;

@Repository
public interface StudentApplicationTransactionRepository extends JpaRepository<StudentApplicationTransaction, Integer>{

	// In StudentApplicationTransactionRepository.java
	Optional<StudentApplicationTransaction> findByPaymentDetails(PaymentDetails paymentDetails);

}
