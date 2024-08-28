package com.khaai.NuoiEm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.khaai.NuoiEm.Entities.MonthYear;
import com.khaai.NuoiEm.Entities.MyUser;
import com.khaai.NuoiEm.Entities.Payment;
import com.khaai.NuoiEm.Repository.MonthYearRepository;
import com.khaai.NuoiEm.Repository.MyUserRepository;
import com.khaai.NuoiEm.Repository.PaymentRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	@Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MyUserRepository userRepository;

    @Autowired
    private MonthYearRepository monthYearRepository;

    public void createPaymentsForCurrentMonth() {

        MonthYear currentMonthYear = monthYearRepository.findTopByOrderByMonthYearIDDesc();

        if (currentMonthYear == null) {
            throw new RuntimeException("MonthYear record not found.");
        }

        List<MyUser> users = userRepository.findAllWithChildren();  

        for (MyUser user : users) {
        	Integer money=user.getChildren().size();
            Payment payment = new Payment(user, currentMonthYear, money*200000, 0);
            paymentRepository.save(payment);
        }
    }
    
    @Scheduled(cron = "0 0 0 5 * ?")
    public void createPaymentsOnFifthDayOfMonth() {
    	createPaymentsForCurrentMonth();
    }
    
	
	public Page<Payment> searchPaymentsByUsername(String username, int page, int size) {
       // PageRequest pageable = PageRequest.of(page, size);
		 Pageable pageable = PageRequest.of(page, size, Sort.by("paymentID").descending());
        if (username != null && !username.isEmpty()) {
            return paymentRepository.findByUser_UsernameContaining(username, pageable);
        } else {
            return paymentRepository.findAllByOrderByPaymentIDDesc(pageable);
        }
    }
	
	
	@Transactional
	public void save(Payment payment) {
		Payment payment_update = paymentRepository.findById(payment.getPaymentID())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán"));
		payment_update.setReceivedMoney(payment.getReceivedMoney());
		paymentRepository.save(payment_update);
	}
	
	public Payment get(Integer id){
		Optional<Payment> result =paymentRepository.findById(id);
		return result.get();
	}
	
	public Payment getLatestPaymentByUserId(Integer userId) {
        List<Payment> payments = paymentRepository.findByUserIdOrderByIdDesc(userId);
        return payments.isEmpty() ? null : payments.get(0);
    }
	
}
