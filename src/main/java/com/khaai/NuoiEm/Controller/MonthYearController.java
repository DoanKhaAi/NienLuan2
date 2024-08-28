package com.khaai.NuoiEm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.khaai.NuoiEm.Service.MonthYearService;

@Controller
public class MonthYearController {
	 @Autowired
	 private MonthYearService monthYearService;
	 
	 @GetMapping("admin/test-create-month-year")
	    public String testCreateMonthYear() {
	        monthYearService.createMonthYearForCurrentMonth();
	        return "MonthYear record created successfully!";
	    }
}
