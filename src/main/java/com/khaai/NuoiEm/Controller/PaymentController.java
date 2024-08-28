package com.khaai.NuoiEm.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khaai.NuoiEm.Entities.Payment;
import com.khaai.NuoiEm.Service.PaymentService;

@Controller
public class PaymentController {
	 	@Autowired
	    private PaymentService service;

	    @GetMapping("/admin/test-create-payments")
	    public String testCreatePayments() {
	        service.createPaymentsForCurrentMonth();
	        return "Payments created successfully!";
	    }
	    
	    @GetMapping("/admin/payment/list")
	    public String listPayments(@RequestParam(value = "username", required = false) String username,
					    			Model model,  @RequestParam(defaultValue = "0") int page,
						           @RequestParam(defaultValue = "10") int size) {
	    	Page<Payment> listPayments = service.searchPaymentsByUsername(username, page, size);
			model.addAttribute("listPayments",listPayments);
			model.addAttribute("username", username);
			return "/payment/listPayment";
	    }
	    
	    @GetMapping("/admin/payment/edit/{id}")
		public String showEditForm(@PathVariable("id") Integer id, Model model) {
				Payment payment=service.get(id);
				model.addAttribute("payment", payment);
				model.addAttribute("pageTitle", "Chỉnh sửa thanh toán cho người dùng: "+ payment.getUser().getUsername());
				return "/payment/formPayment";
			
		} 
	    
		@PostMapping("/admin/payment/save")
		public String savePayment(Payment payment, RedirectAttributes ra) {		
			service.save(payment);
			ra.addFlashAttribute("message", "Lưu thay đổi số tiền thành công!");
			return "redirect:/admin/payment/list";
		}
		

}
