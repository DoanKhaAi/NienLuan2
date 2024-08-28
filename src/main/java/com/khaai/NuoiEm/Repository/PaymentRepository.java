package com.khaai.NuoiEm.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.khaai.NuoiEm.Entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>{
	Page<Payment> findAllByOrderByPaymentIDDesc(Pageable pageable);

	Page<Payment> findByUser_UsernameContaining(String username, Pageable pageable);
	
	@Query("SELECT p FROM Payment p WHERE p.user.userID = :userID ORDER BY p.paymentID DESC")
    List<Payment> findByUserIdOrderByIdDesc(@Param("userID") Integer userId);
}
