package com.khaai.NuoiEm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khaai.NuoiEm.Entities.SchoolYear;
import com.khaai.NuoiEm.Service.SchoolYearService;

@Controller
public class SchoolYearController {
	@Autowired
	private SchoolYearService service;
	
	@GetMapping("/admin/schoolYear/list")
	public String listSchoolYear(Model model) {
		List<SchoolYear> listSchoolYears=service.listAll();
		model.addAttribute("listSchoolYears",listSchoolYears);
		return "/schoolYear/listSchoolYear";
	}
	
	@GetMapping("/admin/schoolYear/listDeleted")
	public String listSchoolYearDeleted(Model model) {
		List<SchoolYear> listSchoolYears=service.listDeleted();
		model.addAttribute("listSchoolYears",listSchoolYears);
		return "/schoolYear/listSchoolYearDeleted";
	}
	
	
	@GetMapping("/admin/schoolYear/new")
	public String showNewForm(Model model) {
		model.addAttribute("schoolYear", new SchoolYear());
		model.addAttribute("pageTitle", "Thêm năm học mới");
		return "/schoolYear/formSchoolYear";
	}

	
	@PostMapping("/admin/schoolYear/save")
	public String saveSchoolYear(SchoolYear schoolYear,@RequestParam("startYear") int startYear, RedirectAttributes ra) {
		String result= service.save(schoolYear, startYear);	
		if ("School_year name already exists".equals(result)) {
			ra.addFlashAttribute("save_error", "Năm học này đã tồn tại!");
			return "redirect:/admin/schoolYear/list";
        }
		ra.addFlashAttribute("message", "Năm học mới đã được lưu thành công");
		return "redirect:/admin/schoolYear/list";
	}
	
	@GetMapping("/admin/schoolYear/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
			SchoolYear schoolYear=service.get(id);
			model.addAttribute("schoolYear", schoolYear);
			model.addAttribute("pageTitle", "Chỉnh sửa năm học (ID: "+id+")");
			return "/schoolYear/formSchoolYear";
		
	} 
	
	@GetMapping("/admin/schoolYear/delete/{id}")
	public String deleteSchoolYear(@PathVariable("id") Integer id, RedirectAttributes ra) {
			service.deleted(id);
			ra.addFlashAttribute("message", "Xóa năm học có ID = "+id+" thành công");
		return "redirect:/admin/schoolYear/list";
	} 
	
	@GetMapping("/admin/schoolYear/restore/{id}")
	public String deleteProvince(@PathVariable("id") Integer id, RedirectAttributes ra) {
		service.restore(id);
		ra.addFlashAttribute("message", "Phục hồi năm học thành công");
		return "redirect:/admin/schoolYear/listDeleted";
	} 
}
