package com.khaai.NuoiEm.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khaai.NuoiEm.Entities.Children;
import com.khaai.NuoiEm.Entities.MyUser;
import com.khaai.NuoiEm.Entities.Payment;
import com.khaai.NuoiEm.Entities.Study;
import com.khaai.NuoiEm.Service.ChildrenService;
import com.khaai.NuoiEm.Service.EmailService;
import com.khaai.NuoiEm.Service.MyUserDetailService;
import com.khaai.NuoiEm.Service.PaymentService;
import com.khaai.NuoiEm.Service.StudyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class MyUserController {
		
	@Autowired
	private MyUserDetailService service;
	
	@Autowired
	private ChildrenService serviceChildren;
	
	@Autowired
	private StudyService serviceStudy;
	
	@Autowired
	private EmailService serviceEmail;
	
	@Autowired
	private PaymentService servicePayment;
		
		
	
//ĐĂNG NHẬP, ĐĂNG XUẤT CHUNG
	
	
	@GetMapping("/register")
	public String showNewForm(Model model) {
		model.addAttribute("user", new MyUser());
		model.addAttribute("pageTitle", "Thêm người dùng mới");
		return "/authentication/register";
	}
	
	@GetMapping("/login")
	public String showLoginForm() {
		return "/authentication/login";
	}
	
	@PostMapping("/verifyOtp")
	public String verifyOtp(@RequestParam("otp") String otp,
	                        @RequestParam("generatedOtp") String generatedOtp,
	                        HttpSession session,
	                        RedirectAttributes ra, Model model) throws IOException {
		MyUser user = (MyUser) session.getAttribute("user");
	    if (otp.equals(generatedOtp)) {
	    	service.save(user);
	        session.removeAttribute("user");
	        ra.addFlashAttribute("save_success", "Tài khoản được ghi thành công");
	        return "redirect:/register";
	    } else {
	        model.addAttribute("save_error", "Mã OTP không chính xác, vui lòng thử lại.");
	        model.addAttribute("otp", generatedOtp);
	        return "/authentication/otpVerification";
	    }
	}
	
	
	@PostMapping("/check")
	public String saveUser(MyUser user, RedirectAttributes ra, Model model,
							HttpSession session, @RequestParam("imageFile")MultipartFile imageFile) throws IOException{		
        String result = service.check(user, imageFile);
        if ("Username already exists".equals(result)) {
            model.addAttribute("user", user);
            model.addAttribute("save_error", "Tên người dùng này đã tồn tại. Bạn vui lòng chọn tên đăng nhập khác nhé!");
            return "/authentication/register";   
        }      
        else if ("Password is not valid".equals(result)) {
            model.addAttribute("save_error2", "Mật khẩu phải từ 6 đến 10 ký tự, bao gồm cả chữ cái, chữ số và ký tự đặc biệt!");
            model.addAttribute("user", user);
            return "/authentication/register";   
        }
        else if ("Email is used or invalid".equals(result)) {
            model.addAttribute("save_error3", "Email không đúng hoặc đã được dùng để đăng ký!");
            model.addAttribute("user", user);
            return "/authentication/register";   
        }
        user.setImage(result);
        session.setAttribute("user", user);
        String otp = serviceEmail.generateOtp();
        model.addAttribute("otp", otp);
        serviceEmail.sendOtpEmail(user.getEmail(), otp);
        return "/authentication/otpVerification";
	}
	
	
//CONTROLLER ĐỐI VỚI ADMIN
	
	@GetMapping("/admin/dashboard")
	public String showDashboard(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
	    UserDetails loggedInUser = (UserDetails) session.getAttribute("loggedInUser");
	    String username=loggedInUser.getUsername();
	    MyUser user=service.get(username);
	    model.addAttribute("user", user);
		return ("/admin/dashboard");
	}
	
	@GetMapping("/admin/user/list")
	public String listUser(Model model,  @RequestParam(defaultValue = "0") int page,
	           @RequestParam(defaultValue = "1") int size) {
		Page<MyUser> listUsers=service.listAll(page, size);
		model.addAttribute("listUsers",listUsers);
		return "/user/admin/listUser";
	}
	
	@GetMapping("/admin/user/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
			MyUser user=service.get(id);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Chỉnh sửa tỉnh quyền người dùng "+id);
			return "/user/admin/formUser";
		
	} 
	
	@PostMapping("/admin/user/save_edit")
	public String saveEdit(MyUser user, RedirectAttributes ra) {
		service.saveEdit(user);
		ra.addFlashAttribute("message", "Thay đổi quyền người dùng thành công!");
		return "redirect:/admin/user/list";
	}
	
	
//CONTROLLER ĐỐI VỚI USER

	
	@GetMapping("/user/welcome")
	public String show(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
	    UserDetails loggedInUser = (UserDetails) session.getAttribute("loggedInUser");
	    String username=loggedInUser.getUsername();
	    MyUser user=service.get(username);
	    model.addAttribute("user", user);
		return ("/user/user/welcome");
	}
	
	@GetMapping("/profile/detail/{id}")
	public String showDetail(@PathVariable("id") Integer id, Model model) {
			MyUser user=service.get(id);
			model.addAttribute("user", user);
			return "/user/user/user_detail";	
	} 
	
	@GetMapping("/profile/edit/{id}")
	public String showUserEdit(@PathVariable("id") Integer id, Model model) {
		MyUser user=service.get(id);
		model.addAttribute("user", user);
		return "/user/user/edit_profile";
	}
	
	@PostMapping("/profile/save_user_edit")
	public String saveUserEdit(MyUser user, RedirectAttributes ra) throws IOException {
		String result= service.saveUserEdit(user);	
		if ("Email is used or invalid".equals(result)) {
			ra.addFlashAttribute("save_error", "Email không đúng hoặc đã được dùng để đăng ký!");
            return "redirect:/profile/edit/"+ user.getUserID();
        }
		ra.addFlashAttribute("message", "Thay đổi thông tin thành công!");
		return "redirect:/profile/detail/"+ user.getUserID();
	}
	
	@GetMapping("/profile/change_image/{id}")
	public String showUserChangeImage(@PathVariable("id") Integer id, Model model) {
		MyUser user=service.get(id);
		model.addAttribute("user", user);
		return "/user/user/change_image";
	}
	
	@PostMapping("/profile/save_user_change_image")
	public String saveUserChangeImage(MyUser user, RedirectAttributes ra, Model model,  @RequestParam("imageFile")MultipartFile imageFile) 
			throws IOException {
		service.saveUserEditImage(user, imageFile);	
		ra.addFlashAttribute("message", "Thay đổi thông tin thành công!");
		return "redirect:/profile/detail/"+ user.getUserID();
	}
	
	@GetMapping("/profile/change_password/{id}")
	public String showUserChangePassword(@PathVariable("id") Integer id, Model model) {
		MyUser user=service.get(id);
		model.addAttribute("user", user);
		return "/user/user/change_password";
	}
	
	@PostMapping("/profile/save_user_change_password")
	public String saveUserChangePassword(MyUser user, RedirectAttributes ra, Model model, String newpassword, String renewpassword) 
			throws IOException {
		String result=service.saveUserEditPassword(user, newpassword, renewpassword);	
		if (result.equals("Password is incorrect")){
			ra.addFlashAttribute("error", "Mật khẩu không đúng!");
			return "redirect:/profile/change_password/" +user.getUserID();
		}
		else if (result.equals("Password is not match"))
		{
			ra.addFlashAttribute("error", "Mật khẩu mới và mật khẩu nhập lại không khớp!");
			return "redirect:/profile/change_password/" +user.getUserID();
		}
		else if (result.equals("Password is not valid"))
		{
			ra.addFlashAttribute("error", "Mật khẩu phải từ 6 đến 10 ký tự, bao gồm cả chữ cái, chữ số và ký tự đặc biệt!");
			return "redirect:/profile/change_password/" +user.getUserID();
		}
		else ra.addFlashAttribute("message", "Thay đổi mật khẩu thành công!");
		return "redirect:/profile/detail/"+ user.getUserID();
	}
	
	@GetMapping("/user/children/adopted/{id}")
	public String adoptedChidren(@PathVariable("id") Integer id, RedirectAttributes ra, HttpServletRequest request) {
			HttpSession session = request.getSession();
		    UserDetails loggedInUser = (UserDetails) session.getAttribute("loggedInUser");
		    String username=loggedInUser.getUsername();
		    MyUser user=service.get(username);
			serviceChildren.saveAdopt(id, user);
			ra.addFlashAttribute("message_adopt", "Nhận nuôi Trẻ em có ID = "+id+" thành công");
		return "redirect:/user/children/adoptedInfo";
	} 
	
	@GetMapping("/user/children/adoptedInfo")
	public String adoptedChidrenInfo(RedirectAttributes ra, HttpServletRequest request, Model model) {
			HttpSession session = request.getSession();
		    UserDetails loggedInUser = (UserDetails) session.getAttribute("loggedInUser");
		    String username=loggedInUser.getUsername();
		    MyUser user=service.get(username);
		    List<Children> listChildren = user.getChildren();
		    model.addAttribute("listChildren", listChildren);
		        
		    List<Study> listStudies = serviceStudy.listAll();	    
		    Map<Integer, Study> studyMap = listStudies.stream()
		            .collect(Collectors.toMap(
		                study -> study.getChildren().getChildID(), 
		                study -> study, 
		                (existingStudy, newStudy) -> {                   
		                    String existingYear = existingStudy.getSchoolYear().getSchoolYearName();
		                    String newYear = newStudy.getSchoolYear().getSchoolYearName();
		                    return compareSchoolYears(existingYear, newYear) > 0 ? existingStudy : newStudy;
		                }
		            ));
		    model.addAttribute("studyMap", studyMap);
		return "/user/user/listAdoptedChildren";
	} 
	
	public int compareSchoolYears(String year1, String year2) {
	    int year1Start = Integer.parseInt(year1.split("-")[0]);
	    int year2Start = Integer.parseInt(year2.split("-")[0]);
	    return Integer.compare(year1Start, year2Start);
	}
	
	@GetMapping("/user/children/cancel/{id}")
	public String CancelAdoptChidren(@PathVariable("id") Integer id, RedirectAttributes ra) {
			serviceChildren.cancelAdopt(id);
			ra.addFlashAttribute("message_cancel", "Hủy nuôi Trẻ em có ID = "+id+" thành công");
		return "redirect:/user/children/adoptedInfo";
	} 
	
	@GetMapping("/user/children/payment")
	public String payMent(RedirectAttributes ra, HttpServletRequest request, Model model) {
			HttpSession session = request.getSession();
		    UserDetails loggedInUser = (UserDetails) session.getAttribute("loggedInUser");
		    String username=loggedInUser.getUsername();
		    MyUser user=service.get(username);
		    Payment payment= servicePayment.getLatestPaymentByUserId(user.getUserID());
		    model.addAttribute("user", user);
		    model.addAttribute("payment", payment);
		    
		return "/user/user/payment";
	} 

}
