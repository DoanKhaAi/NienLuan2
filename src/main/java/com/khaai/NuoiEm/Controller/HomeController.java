package com.khaai.NuoiEm.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("")
	public String showHomePage() {
		return ("/home/index");
	}
	
	@GetMapping("/index")
	public String showHome() {
		return ("/home/index");
	}
	
	@GetMapping("/guide")
	public String showGuide() {
		return ("/home/guide");
	}
	
	@GetMapping("/contact")
	public String showContact() {
		return ("/home/contact");
	}	
	
	
}
